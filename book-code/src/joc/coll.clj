(ns joc.core)

;collection types
(def ds [:willie :barnabas :adam])
ds

(def ds1 (replace {:barnabas :quentin} ds))
ds1
ds

(class (hash-map :a 1))
(seq (hash-map :a 1))
(class (seq (hash-map :a 1)))
(seq (keys (hash-map :a 1)))
(class (keys (hash-map :a 1)))

(vec (range 10))

(let [my-vector [:a :b :c]]
  (into my-vector (range 10)))

(into (vector-of :int) [Math/PI 2 1.3])
(into (vector-of :char) [100 101 102])

(def a-to-j (vec (map char (range 65 75 ))))
a-to-j

(nth a-to-j 4)
(get a-to-j 4)
(a-to-j 4)
(seq a-to-j)
(rseq a-to-j)
(assoc a-to-j 4 "no longer E")

(replace {2 :a 4 :b} [1 2 3 2 3 4])

(def matrix
  [[1 2 3]
   [4 5 6]
   [7 8 9]])
(get-in matrix [1 2])
(assoc-in matrix [1 2] 'x)
(update-in matrix [1 2] * 100)

(defn neighbors
  ([size yx] (neighbors [[-1 0] [1 0] [0 -1] [0 1]]
                        size
                        yx))
  ([deltas size yx]
   (filter (fn [new-yx]
             (every? #(< -1 % size) new-yx))
           (map #(vec (map + yx %))
                deltas))))

(neighbors 3 [0 0])

;; vectors as stacks
(def my-stack [1 2 3])
(peek my-stack)
(pop my-stack)
(conj my-stack 4)
(+ (peek my-stack) (peek (pop my-stack)))

(subvec a-to-j 3 6)

(first {:width 10 :height 20 :depth 15})
(vector? (first {:width 10 :height 20 :depth 15}))

(doseq [[dimension amount] {:width 10 :height 20 :depth 15}]
  (println (str (name dimension) ":") amount "inches"))

;; lists
(cons 1 '(2 3))
(conj '(2 3) 1)

;;queues
;;; first provide a multimethod for pretty printing
(defmethod print-method clojure.lang.PersistentQueue
  [q w]
  (print-method '<- w)
  (print-method (seq q) w)
  (print-method '-< w))

(def schedule
  (conj clojure.lang.PersistentQueue/EMPTY
        :wake-up :shower :brush-teeth))

(peek schedule)
(pop schedule)
(rest schedule)

;; sets
(#{:a :b :c :d} :c)

(#{:a :b :c :d} :e)

(get #{:a 1 :b 2} :b)
(get #{:a 1 :b 2} :z :nothing-doing)

(into #{[]} [()])
(into #{[1 2]} '[(1 2)])

(some #{:b} [:a 1 :b 2])
(some #{1 :b} [:a 1 :b 2])

(sorted-set :b :c :a)
(sorted-set [3 4] [1 2])

(require 'clojure.set)
;(ns my.cool.lib
;  (:require clojure.set))

(clojure.set/intersection #{:humans :fruit-bats :zombies}
                          #{:chupacabra :zombies :humans})

(clojure.set/intersection #{:pez :gum :dots :skor}
                          #{:pez :skor :pocky}
                          #{:pocky :gum :skor})

(clojure.set/union #{:humans :fruit-bats :zombies}
                   #{:chupacabra :zoies :humans})

(clojure.set/union #{:pez :gum :dots :skor}
                   #{:pez :skor :pocky}
                   #{:pocky :gum :skor})

(clojure.set/difference #{1 2 3 4} #{3 4 5 6})

;;maps
(hash-map :a 1, :b 2, :c 3, :d 4, :e 5)
(let [m {:a 1 1 :b [1 2 3] "4 5 6"}]
  [(get m :a) (get m [1 2 3])])

(seq {:a 1 :b 2})
(into {} (map vec '[(:a 1) (:b 2)]))

(apply hash-map [:a 1 :b 2])
(zipmap [:a :b] [1 2])

(sorted-map :thx 1138 :r2d 2)
(sorted-map "bac" 2 "abc" 9)
(sorted-map-by #(compare (subs %1 1) (subs %2 1)) "bac" 2 "abc" 9)

(assoc {1 :int} 1.0 :float)

;;keeping order
(seq (hash-map :a 1 :b 2 :c 3))
(seq (array-map :a 1 :b 2 :c 3))

(defn index [coll]
  (cond
    (map? coll) (seq coll)
    (set? coll) (map vector coll coll)
    :else (map vector (iterate inc 0) coll)))

(index [:a 1 :b 2 :c 3 :d 4])
(index {:a 1 :b 2 :c 3 :d 4})
(index #{:a 1 :b 2 :c 3 :d 4})

(defn pos [e coll]
  (for [[i v] (index coll) :when (= e v)] i))

(defn pos [pred coll]
  (for [[i v] (index coll) :when (pred v)] i))

