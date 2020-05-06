(ns joc.core)

;;closures
(def times-two
  (let [x 2]
    (fn [y] (* y x))))

(times-two 5)

(defn times-n [n]
  (let [x n]
    (fn [y] (* y x))))

(times-n 4)

(def times-four (times-n 4))
(times-four 10)

(defn times-x [x]
  (fn [y] (* y x)))

(defn divisible [denom]
  (fn [num]
    (zero? (rem num denom))))

((divisible 3) 6)
((divisible 3) 7)

(filter (divisible 4) (range 10))

(defn filter-divisible [denom s]
  (filter (fn [num] (zero? (rem num denom))) s))
  ;; (filter #(zero? (rem % denom)) s))
(filter-divisible 4 (range 10))

(def bearings [{:x  0 :y  1}  ;north
               {:x  1 :y  0}  ;east
               {:x  0 :y -1}  ;south
               {:x -1 :y 0}]) ;west

(defn forward [x y bearing-num]
  [(+ x (:x (bearings bearing-num)))
   (+ y (:y (bearings bearing-num)))])

(forward 5 5 0)

(forward 5 5 1)

(forward 5 5 2)

(defn bot [x y bearing-num]
  {:coords [x y]
   :bearing ([:north :east :south :west] bearing-num)
   :forward (fn [] (bot (+ x (:x (bearings bearing-num)))
                        (+ y (:y (bearings bearing-num)))
                        bearing-num))})

(:coords (bot 5 5 0))
(:bearing (bot 5 5 0))
(:coords ((:forward (bot 5 5 0))))

(defn obot [x y bearing-num]
  {:coords   [x y]
   :bearing  ([:north :east :south :west] bearing-num)
   :forward  (fn [] (obot (+ x (:x (bearings bearing-num)))
                          (+ y (:y (bearings bearing-num)))
                         bearing-num))
   :turn-right (fn [] (obot x y (mod (+ 1 bearing-num) 4)))
   :turn-left  (fn [] (obot x y (mod (- 1 bearing-num) 4)))})

(:bearing ((:forward ((:forward ((:turn-right (obot 5 5 0))))))))
(:coords ((:forward ((:forward ((:turn-right (obot 5 5 0))))))))


(defn mirror-bot [x y bearing-num]
  {:coords   [x y]
   :bearing  ([:north :east :south :west] bearing-num)
   :forward  (fn [] (mirror-bot (- x (:x (bearings bearing-num)))
                                (- y (:y (bearings bearing-num)))
                                 bearing-num))
   :turn-right (fn [] (mirror-bot x y (mod (- 1 bearing-num) 4)))
   :turn-left  (fn [] (mirror-bot x y (mod (+ 1 bearing-num) 4)))})

(:bearing ((:forward ((:forward ((:turn-right (mirror-bot 5 5 0))))))))
(:coords ((:forward ((:forward ((:turn-right (mirror-bot 5 5 0))))))))


