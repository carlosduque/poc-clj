(ns ca.ch2.print)

(defmethod print-method Pair
  [pair ^Writer w]
  (.write w "#ca.ch2.pair.Pair")
  (print-method (vec (seq pair)) w))

(defmethod print-dup Pair
  [pair w]
  (print-method pair w))

