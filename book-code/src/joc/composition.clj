(ns joc.core)

(map [:chthon :phthor :beowulf :grendel] #{0 3})

;; compose functions
(def fifth (comp first rest rest rest rest))
(fifth [1 2 3 4 5])

(defn fnth [n]
  (apply comp
         (cons first
               (take (dec n) (repeat rest)))))

((fnth 5) '[a b c d e])

(map (comp
       keyword
       #(.toLowerCase %)
       name)
     '(a B C))

((partial + 5) 100 200)

;; reverse truth with complement
(let [truthiness (fn [v] v)]
  [((complement truthiness) true)
   ((complement truthiness) 42)
   ((complement truthiness) false)
   ((complement truthiness) nil)])

((complement even?) 2)

;; function as data
(defn join
  {:test (fn []
           (assert
             (= (join "," [1 2 3]) "1,3,3")))}
  [sep s]
  (apply str (interpose sep s)))

;;shorthand version for metadata
(defn ^:private ^:dynamic sum [nums]
  (map + nums))

;;functions as arguments
(sort [1 5 7 0 -42 13])
(sort ["z" "x" "a" "aa"])
(sort [(java.util.Date.) (java.util.Date. 100)])
(sort [[1 2 3] [-1 0 1] [3 2 1]])
(sort > [7 1 4])
;;will throw a cast class exception
;;(sort ["z" "x" "a" "aa" 1 5 8])
;;throws persistent array map cast exception
;;(sort [{:age 99} {:age 13} {:age 7}])

(sort-by second [[:a 7] [:c 13] [:b 21]])
(sort-by str ["z" "x" "a" "aa" 1 5 8])
(sort-by :age [{:age 99} {:age 13} {:age 7}])

(def plays [{:band "Burial"     :plays 979  :loved 9}
            {:band "Eno"        :plays 2333 :loved 15}
            {:band "Bill Evans" :plays 979  :loved 9}
            {:band "Magma"      :plays 2665 :loved 31}])

(def sort-by-loved-ratio (partial sort-by #(/ (:plays %) (:loved %))))
(sort-by-loved-ratio plays)

(defn columns [column-names]
  (fn [row]
    (vec (map row column-names))))

(columns [:plays :loved :band])

;;functions as return values
(sort-by (columns [:plays :loved :band]) plays)

((columns [:plays :loved :band])
 {:band "Burial" :plays 979 :loved 9})

;;pure functions
(defn keys-apply [f ks m]
  (let [only (select-keys m ks)]
    (zipmap (keys only)
            (map f (vals only)))))

(keys-apply #(.toUpperCase %) #{:band} (plays 0))

(defn manip-map [f ks m]
  (merge m (keys-apply f ks m)))

(manip-map #(int (/ % 2)) #{:plays :loved} (plays 0))

;;Named arguments
(defn slope
  [& {:keys [p1 p2] :or {p1 [0 0] p2 [1 1]}}]
  (float (/ (- (p2 1) (p1 1))
            (- (p2 0) (p1 0)))))

(slope :p1 [4 15] :p2 [3 21])

(slope :p2 [2 1])

;;constraining with pre/post conditions
(defn slope2 [p1 p2]
  {:pre [(not= p1 p2) (vector? p1) (vector? p2)]
   :post [(float? %)]}
  (/ (- (p2 1) (p1 1))
     (- (p2 0) (p1 0))))

;;assertion fails
;;(slope2 [10 10] [10 10])

