(ns ch5.reducer
  (:require [ch5.domain :refer (ground?)]
            [clojure.core.reducers :as r]))

(defn ground-weight [products]
  (->> products
       (r/filter ground?)
       (r/map :weight)
       (r/fold +)))


