(ns threads.util.core
  (:import java.security.MessageDigest
           java.math.BigInteger)
  (:gen-class))

(defmacro with-new-thread [& body]
  `(.start (Thread. (fn [] ~@body))))

(defn now []
  (str (java.time.Instant/now)))

(defn md5
  [^String s]
  (->> s
       .getBytes
       (.digest (MessageDigest/getInstance "MD5"))
       (BigInteger. 1)
       (format "%032x")))

