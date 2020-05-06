(ns ca.ch2.search)

(def units [:lb :oz :kg])

(some #{:oz} units)

(defn contains-val?
  [coll val]
  (reduce
          (fn [ret elem] (if (= val elem) (reduced true) ret))
          false coll))


