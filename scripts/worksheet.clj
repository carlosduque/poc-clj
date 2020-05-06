(ns worksheet)

(def name "Leeeeeeeerrroyyy")

(defn abc [acc e]
  (if (not= (last acc) e)
    (apply str acc e)
    acc))
