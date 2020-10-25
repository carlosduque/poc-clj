(ns joc.core)

;; recursion
(defn print-down-from [x]
  (when (pos? x)
    (println x)
    (recur (dec x))))

(print-down-from 7)

;; with an accumulator
(defn sum-down-from [sum x]
  (if (pos? x)
    (recur (+ sum x) (dec x))
    sum))

(sum-down-from 0 9)

;; with loop
(defn sum-down-from-x [initial-x]
  (loop [sum 0, x initial-x]
    (if (pos? x)
      (recur (+ sum x) (dec x))
      sum)))

(sum-down-from-x 9)

;; nil pun: empty collections are true, but using _seq_ on the collection
;; returns false
(seq [1 2 3])

(seq [])

(defn print-seq [s]
  (when (seq s)
    (prn (first s))
    (recur (rest s))))

(print-seq [])

(print-seq [1 2])

