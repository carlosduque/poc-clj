(ns ch5.domain)

(defn ground? [product]
  (= :ground (:class product)))

