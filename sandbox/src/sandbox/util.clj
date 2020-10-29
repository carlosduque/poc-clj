(ns sandbox.util
  (:import java.security.MessageDigest
           java.math.BigInteger)
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

(defmacro with-new-thread [& body]
  `(.start (Thread. (fn [] ~@body))))

(defn md5
  [^String s]
  (->> s
       .getBytes
       (.digest (MessageDigest/getInstance "MD5"))
       (BigInteger. 1)
       (format "%32x")))

(defn create-fetcher [fn-fetcher]
  (fn [url]
    (let [id (md5 url)]
      {:id id
       :url url
       :mime "text/plain"
       :content (fn-fetcher url)})))

