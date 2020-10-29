(ns sandbox.concurrency
  (:require [sandbox.util :as util])
  (:gen-class))

;Identity and Change of state

;;vars
;currently ignored

;;refs
;managed reference for sync and coord changes
; atomic, consistent, isolated -> ACI
(def initial-balance 1000)
(def account1 (ref initial-balance))
(def account2 (ref 0))

(defn transfer [a1 a2 amount]
  (dosync
    (alter a1 #(- % amount))
    (alter a2 #(+ % amount))))

(dotimes [i 10]
  (future (transfer account1 account2 (rand-int 25))))

(deref account1)
(deref account2)

;check balance, if not sync'd we'd get a false
(= initial-balance (+ @account1 @account2))

(util/with-new-thread (transfer account1 account2 13))
(util/with-new-thread (transfer account1 account2 17))
(deref account1)
(deref account2)
(= initial-balance (+ @account1 @account2))
;;;;;;;;;;;;;;;;;;;;;
;;atoms
;;;;;;;;;;;;;;;;;;;;;
;use for synchronized but uncoordinated changes
;compare and set
(def entry (atom {:num 0 :code ""}))
(defn change-it [e]
  (swap! e (comp #(update-in % [:num] inc)
                 #(assoc % :code (util/uuid)))))

(dotimes [i 100]
  (future (change-it entry)))

(deref entry)

;;;;;;;;;;;;;;;;;;;;;
;;agents
;use for async and uncoordinated stuff
;each agent has its own queue of actions which are
;evaluated serially in the order they were sent.
(def sum (agent 0))
(dotimes [i 1000]
  (send sum + 1)) ;not parallel, each send queues the action on the agent's queue

(deref sum)
;for parallelism you need multiple agents

;real world
(def don-quixote "http://www.gutenberg.org/cache/epub/996/pg996.epub")
(def dracula "http://www.gutenberg.org/cache/epub/345/pg345.txt")
(defn fetch-books [urls]
  (let [agents (map agent urls)]
    (doseq [ag agents]
      (send-off ag slurp)
      (apply await-for 5000 agents)
      (doall
             (map deref agents)))))

(fetch-books [dracula don-quixote])

