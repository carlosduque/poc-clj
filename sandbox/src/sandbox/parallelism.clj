(ns sandbox.parallelism
  (:require [sandbox.util :refer :all])
  (:gen-class))

;;;;;;;;;;;;;;;;;;;;;;;;;
;; PARAllELISM
; Executing more than one task at the same time
; this file shows the use of threads for parallel processing

;;;;;;;;;;;;;;;;;;;;;;;;;
;;; THREAD: a typical thread
(.start (Thread. (println "I am unique:" (.getName (Thread/currentThread)))))

;;; POOL: with a thread pool
(import 'java.util.concurrent.Executors)
(def processors (.availableProcessors (Runtime/getRuntime)))
(defonce pool (Executors/newFixedThreadPool processors))
(defn submit-task [^Callable task]
  (.submit pool task))

(dotimes [num 10]
  (submit-task
    (fn [i] (println "#" i ":" (.getName (Thread/currentThread))))))


;;;;;;;;;;;;;;;;;;;;;;;;;
;;; COMMUNICATIONS
;;; communicating between threads / getting results

;;;;;;;; delay
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

;; real world
(def dracula "http://www.gutenberg.org/cache/epub/345/pg345.txt")
(def delayed-fetcher (create-fetcher #(delay (slurp %))))
(def doc-fetched (delayed-fetcher dracula))
(realized? (:content doc-fetched))
@(:content doc-fetched) ;; will get the contents of the book now at deref time!
(realized? (:content doc-fetched))

;;;;;;;; promise
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

;;;;;;; future
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

;; real world
(def future-fetcher (create-fetcher #(future (slurp %))))
(def doc-fetched (future-fetcher dracula))
(realized? (:content doc-fetched)) ;content may not be there yet, it's been retrieved
(:content doc-fetched) ;; content is already there fetched by a future

;;;;;;;;;;;;;;;;;;;;;;;;;
;;; REDUCERS
;;; skip non-parallel natural processing of sequences by using reducers
