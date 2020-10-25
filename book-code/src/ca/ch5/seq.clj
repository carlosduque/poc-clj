(ns ch5.seq
  (:use 'ch5.domain))

(defn ground-weight [products]
  (->> products
       (filter ground?)
       (map :weight)
       (reduce +)))


