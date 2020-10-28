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

(util/with-new-thread (fn [] (transfer account1 account2 13)))
(util/with-new-thread (fn [] (transfer account1 account2 17)))
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



;;agents
;;agents
(def book-1 "http://www.gutenberg.org/cache/epub/103/pg103.txt")
(def book-2 "http://www.gutenberg.org/cache/epub/996/pg906.txt")

(defn fetch-books [urls]
  (let [agents (map agent urls)]
    (doseq [ag agents]
      (send-off ag slurp)
      (apply await-for 5000 agents)
      (doall
             (map deref agents)))))

(fetch-books [book-1 book-2])

