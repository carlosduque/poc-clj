(reduce + [1 2 3 4])
(def person (create-struct :name :age :sex))
(struct person "Carlos" 39 "Male")

(def ls
  (lazy-seq
      (do
          (prn "body executed")
          (lazy-seq (do (prn "next body executed") [:a :b :c])))))


