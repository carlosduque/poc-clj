(ns sandbox.concurrency
  (:gen-class))

(defn long-calculation [id seconds]
  (Thread/sleep (* 1000 seconds))
  (str "id:" id "slept for" seconds "seconds"))

;; delay
;; create a unit of work but you may not need it so don't execute yet
(def minion (delay (str "working, working")))

;check status, should be "pending"
minion

(realized? minion)

(deref minion)

;; promise
(def i-owe-you (promise))

