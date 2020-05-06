(ns sandbox.core
  (:gen-class))

;; #28
(def n1 '((1 2) 3 [4 [5 6]]))
(def n2 ["a" ["b"] "c"])
(def n3 '((((:a)))))

(defn lat
  [coll]
  (loop [acc '()
         xs coll]
    (println "acc: " acc " xs: " xs)
    (cond
      (empty? xs) (reverse acc)
      (seqable? (first xs)) (recur acc (concat (first xs) (rest xs)))
      :else (recur (cons (first xs) acc) (rest xs)))))

;; #29
(def str1 "HeLlO, WoRlD!")
(def str2 "nothing")
(def str3 "$#A(*&987Zf")
#(apply str (re-seq #"[A-Z]" %))

;; #30
(def mystr "Leeeerrroy")
(def myvec [1 1 2 3 3 2 2 3])
(def mytup [[1 2] [1 2] [3 4] [1 2]])

(#(reduce (fn [acc x]
           (if (not= (last acc) x)
              (conj acc x)
              acc))
         [] %) mystr)

;; #31
#(partition-by identity %)

;; #32
(def l1 [1 2 3])
(def l2 [:a :a :b :b])
(def l3 [[1 2] [3 4]])

(defn duplicater
  [coll]
  (flatten (map (fn [e] (list e e)) coll)))

(#(flatten (map (fn [e] (list e e)) %)) l1)
