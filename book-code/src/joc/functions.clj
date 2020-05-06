(ns joc.core)

;;anonymous function for creating a set
((fn [x y]
  (println "Making a set")
  #({ x y})) 45 78)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

;; blocks
(do
  (def x 5)
  (def y 4)
  (+ x y)
  [x y])

;; locals
(let [r      5
      pi     3.1415
      r-squared (* r r)]
  (println "radius is" r)
  (* pi r-squared))

