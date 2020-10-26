(ns sandbox.parallelism
  (:gen-class))

;; PARAllELISM
; Executing more than one task at the same time
; this file shows the use of threads for parallel processing

;;; defining some useful functions
(defn uuid []
  (.toString (java.util.UUID/randomUUID)))

(defn this-time []
  (let [now (java.time.LocalDateTime/now)
        fmt (java.time.format.DateTimeFormatter/ofPattern "HH:mm:ss.SSS")]
  (.format now fmt)))

(defn long-calculation [id]
  (let [uuid (uuid)
        random-time-in-ms (* 1000 (+ 1 (int (rand 5))))
        tm (this-time)
        result (str id "|" random-time-in-ms "ms|" tm "|" uuid)]
    (Thread/sleep random-time-in-ms)
    (println "slept" random-time-in-ms "ms")
    result))

;;; a typical thread
(.start (Thread. (println "I am unique:" (.getName (Thread/currentThread)))))

;;; with a thread pool
(import 'java.util.concurrent.Executors)
(def processors (.availableProcessors (Runtime/getRuntime)))
(defonce pool (Executors/newFixedThreadPool processors))
(defn submit-task [^Callable task]
  (.submit pool task))

(dotimes [num 10]
  (submit-task
    (fn [i] (println "#" i ":" (.getName (Thread/currentThread))))))


;;; communicating between threads / getting results
;;;;;;;;;;;;;;;;;;;;;;;;;
;; delay
;;;;;;;;;;;;;;;;;;;;;;;;;
;; object that represents a unit of work but you may not need it
;; so it doesn't execute yet
(def work-to-do (delay (long-calculation "delay")))
;check status, should be "pending"
work-to-do
;realization should still be false
(realized? work-to-do)
;launch a new thread and ask it to DO the work
(.start (Thread. (deref work-to-do))) ;
;realization should be true now
(realized? work-to-do)

(deref work-to-do)

;;;;;;;;;;;;;;;;;;;;;;;;;
;; promise
;;;;;;;;;;;;;;;;;;;;;;;;;
;; object that represents a value that will be delivered
;; later and probably by some other thread

(def iou (promise))
;check status, should be "pending"
iou
;realization should still be false
(realized? iou)
;launch a new thread which will deliver on the promise
(.start (Thread. (deliver iou (long-calculation "promise")))) ;
;realization should still be true
(realized? iou)
(deref iou)

;;;;;;;;;;;;;;;;;;;;;;;;;
;; future
;;;;;;;;;;;;;;;;;;;;;;;;;
;; represents the result of a function
;; that will execute in another thread
(future (println "running in another thread !!!"))

;we usually want to keep a reference to get the value out
(defn very-long [a b]
  (Thread/sleep 5000)
  (* a b))

(defn slow-run []
  (let [x (very-long 11 13)
        y (very-long 5 3)
        z (very-long 9 84)]
    (* x y z)))

(time (slow-run))

(defn fast-run []
  (let [x (future (very-long 11 13))
        y (future (very-long 5 3))
        z (future (very-long 9 84))]
    (* @x @y @z)))

(time (fast-run))

