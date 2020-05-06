(ns joc.core)

;;functional programming techniques
;;structural sharing
(def baselist (list :barnabas :adam))
(def lst1 (cons :willie baselist))
(def lst2 (cons :phoeninx baselist))
lst1
lst2
(= (next lst1) (next lst2))
(identical? (next lst1) (next lst2))

(defn xconj [t v]
  (cond
        (nil? t) {:val v :L nil :R nil}
        (< v (:val t)) {:val (:val t)
                        :L (xconj (:L t) v)
                        :R (:R t)}
        :else {:val (:val t),
               :L (:L t),
               :R (xconj (:R t) v)}))

(def tree1 (xconj nil 5))
tree1
(def tree1 (xconj tree1 3))
tree1
(def tree1 (xconj tree1 2))

(defn xseq [t]
  (when t
    (concat (xseq (:L t)) [(:val t)] (xseq (:R t)))))
(xseq tree1)

(def tree2 (xconj tree1 7))
(xseq tree2)
(identical? (:L tree1) (:L tree2))

;; lazyness
(defn lz-rec-step [s]
  (lazy-seq
            (if (seq s)
              [(first s) (lz-rec-step (rest s))]
              [])))

(lz-rec-step [1 2 3 4])
(class (lz-rec-step [1 2 3 4]))

(defn triangle [n]
  (/ (* n (+ n 1)) 2))

(triangle 10)

(map triangle (range 1 11))

(def tri-nums (map triangle (iterate inc 1)))
(take 10 tri-nums)
(take 10 (filter even? tri-nums))
(nth tri-nums 99)
(double (reduce + (take 1000 (map / tri-nums))))
(take 2 (drop-while #(< % 10000) tri-nums))

