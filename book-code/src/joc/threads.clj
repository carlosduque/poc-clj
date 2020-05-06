(ns cli-foo.thread-joc
  (:require [org.clojure/core.async "0.4.474"])
  (:gen-class))

(def later (delay (prn "hello")))
(@later)


;; future
(def foo (promise))
(future
  (prn "child thread doing stuff...")
  (Thread/sleep 10000)
  (deliver foo :bar))

(@foo)

;;timeout and fallback values
(def f (future
         (Thread/sleep 10000)
         (println "done")
         100))

(deref f 500 "fail")


;;atom: thread safe and retriable
(def counter (atom 0))
(swap! counter inc) ;;1
(swap! counter #(+ 2 %)) ;;3
(swap! counter #(+ 2 %1 %2) 3) ;;8

;;atom with validator
(def ctr (atom 0))
(set-validator! ctr #(even? %))
;; (def ctr (atom 0 :validator #(even? %)))
(swap! ctr inc)
(swap! ctr #(+ 2 %))

;;watching an atom for changes
(def state (atom {}))
(defn state-change [key atom old new]
  (prn (format "key: %s, atom: %s, old val: %s, new val %s" key atom old new)))

(add-watch state :foo state-change)
(swap! state assoc :bar "baz")
