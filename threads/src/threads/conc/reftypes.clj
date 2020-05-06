(ns threads.conc.reftypes
  (:require [threads.util.core :as u])
  (:gen-class))

;;State: clojure's immutable datastructures
;;Identity: clojure's ref types (vars, atoms, refs, agents)

;;Atom: synchronous, uncoordinated
(def even-atom (atom 0 :validator even?))

;;launch several threads affecting the atom
(dotimes [i 5]
  (.start (Thread. (fn [] (swap! even-atom inc))))
  (u/with-new-thread (fn [] (swap! even-atom (partial * 3))))
  (u/with-new-thread (fn [] (swap! even-atom dec))))

;;Refs: synchronous, coordinated
(def amount-ref (ref 1000))
(def message-ref (ref "initial balance"))

(defn withdrawal [amount message]
  (dosync
    (alter amount-ref - amount)
    (ref-set message-ref message)))

(u/with-new-thread (fn [] (withdrawal 100 "Papa John's")))
(u/with-new-thread (fn [] (withdrawal  50 "Lo Saldes")))
(u/with-new-thread (fn [] (withdrawal  75 "Rishtedar")))

;;Agents: asynchronous uncoordinated

(def book-1 "http://www.gutenberg.org/cache/epub/103/pg103.txt")
(def book-2 "http://www.gutenberg.org/cache/epub/996/pg996.txt")

(defn fetch-books [urls]
  (let [agents (map #(agent %) urls)]
    (doseq [agent agents]
      (send-off agent slurp)
      (apply await-for 5000 agents)
      (doall (map #(deref %) agents)))))

(fetch-books [book-1 book-2])
