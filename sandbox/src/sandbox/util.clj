(ns sandbox.util
  (:gen-class))

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
