(ns ca.ch1.convert
  (:require [ca.ch1.recipe :refer [Ingredient]]))

(defmulti convert
  "Convert quantity from unit1 to unit2, matching on [unit1 unit2]"
  (fn [unit1 unit2 quantity] [unit1 unit2]))

;; lb to oz
(defmethod convert [:lb :oz] [_ _ lb] (* lb 16))

;; oz to lb
(defmethod convert [:oz :lb] [_ _ oz] (/ oz 16))

;; fallthrough
(defmethod convert :default [u1 u2 q]
  (if (= u1 u2)
    q
    (assert false (str "Unknown unit conversion from " u1 " to " u2))))

(defn ingredient+
  "Add two ingredients into a single ingredient, combining their 
  quantities with unit conversion if necessary."
  [{q1 :quantity u1 :unit :as i1} {q2 :quantity u2 :unit}]
  (assoc i1 :quantity (+ q1 (convert u2 u1 q2))))


(ingredient+ (->Ingredient "Spaghetti" 1/2 :lb)
             (->Ingredient "Spaghetti" 4 :oz))

(defprotocol TaxedCost
  (taxed-cost [entity store]))

(extend-protocol TaxedCost
  Object
  (taxed-cost [entity store]
    (if (satisfies? Cost entity)
      (do (extend-protocol TaxedCost
            (class entity)
            (taxed-cost [entity store]
              (* (cost entity store) (+ 1 (tax-rate store)))))
          (taxed-cost entity store))
      (assert false (str "Unhandled entity: " entity)))))

