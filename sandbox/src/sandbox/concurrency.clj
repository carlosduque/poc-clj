(ns sandbox.concurrency
  (:require [sandbox.parallelism :refer [uuid]])
  (:gen-class))

;Identity and Change of state

;;vars

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

;;;;;;;;;;;;;;;;;;;;;
;;atoms
;;;;;;;;;;;;;;;;;;;;;
;use for synchronized but uncoordinated changes
;compare and set
(def entry (atom {:num 0 :code ""}))
(defn change-it [e]
  (swap! e (comp #(update-in % [:num] inc)
                 #(assoc % :code (uuid)))))

(dotimes [i 100]
  (future (change-it entry)))

(deref entry)



;;agents
;;agents

