(ns ca.ch1.protocols)

(defprotocol Cost
  (cost [entity store]))

(extend-protocol Cost
  Recipe
  (cost [recipe store]
    (reduce +$ zero-dollars
            (map #(cost % store) (:ingredients recipe))))

  Ingredient
  (cost [ingredient store]
    (cost-of store ingredient)))

