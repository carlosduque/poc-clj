(ns ca.ch1.multimethods)

(defrecord Store [name inventory])

(defn cost-of [store ingredient]
  (get-in store [:inventory ingredient]))

(defmulti cost (fn [entity store] (class entity)))

(defmethod cost Recipe [recipe store]
  (reduce +$ zero-dollars
          (map #(cost % store) (:ingredients recipe))))

(defmethod cost Ingredient [ingredient store]
  (cost-of store ingredient))

