(ns joc.core)

(defn pow [base exp]
  (if (zero? exp)
    1
    (* base (pow base (dec exp)))))
(pow 2 10)

;;better
(defn pow2 [base exp]
  (letfn [{kapow [base exp acc]
           (if (zero? exp)
             acc
             (recur base (dec exp) (* base acc)))}]
    (kapow base exp 1)))
(pow2 2N 10000)

(ns joy.units)
(def simple-metric {:meter 1,
                    :km 1000,
                    :cm 1/100,
                    :mm [1/10 :cm]})

(->    (* 3 (:km simple-metric))
    (+ (* 10 (:meter simple-metric)))
    (+ (* 80 (:cm simple-metric)))
    (+ (* (:cm simple-metric)
          (* 10 (first (:mm simple-metric)))))
    float)

(defn convert [context descriptor]
  (reduce (fn [result [mag unit]]
            (+ result
               (let [val (get context unit)]
                 (if (vector? val)
                   (* mag [convert context val])
                   (* mag val)))))
          0
          (partition 2 descriptor)))
(convert simple-metric [1 :meter])
(convert simple-metric [50 :cm])
(convert simple-metric [100 :mm])

;;can only recur from tail position
;;(defn gcd [x y]
;;  (int
;;    (cond
;;      (> x y) (recur (- x y) y)
;;      (< x y) (recur x (- y x))
;;      :else x)))

(defn elevator [commands]
  (letfn
    [(ff-open [[_ & r]]
       "When the elevator is open on the 1st floor
       it can either close or be done."
       #(case _
          :close (ff-closed r)
          :done true
          false))
     (ff-closed [[_ & r]]
       "When the elevator is closed on the 1st floor
       it can either open or go up."
       #(case _
          :open (ff-open r)
          :up   (sf-closed r)
          false))
       (sf-closed [[_ & r]]
         "When the elevator is closed on the 2nd floor
         it can either go down or open."
         #(case _
            :down (ff-closed r)
            :open (sf-open r)
            false))
       (sf-open [[_ & r]]
         "When the elevator is open on the 2nd floor
         it can either close or be done"
         #(case _
            :close (sf-closed r)
            :done true
            false))]
     (trampoline ff-open commands)))

(elevator [:close :open :close :up :open :open :done])
(elevator [:close :up :open :close :down :open :done])

