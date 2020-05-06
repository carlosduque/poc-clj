(ns joc.core)

(import [java.io bufferedReader InputStreamReader]
        [java.net URL])
(defn joc-www []
  (-> "http://joyofclojure.com/hello" URL.
      .openStream
      InputStreamReader.
      BufferedReader.))

;; (let [stream (joc-www)]
;;   (with-open [page stream]
;;     (println (.readLine page))
;;     (print "The stream will now close..."))
;;   (println "but let's read from it anyway.")
;;   (.readLine stream))

(defmacro with-resource [binding close-fn & body]
  `(let ~binding
     (try
       (do ~@body)
       (finally
         (~close-fn ~(binding 0))))))

(let [stream (joc-www)]
  (with-resource [page stream]
    #(.close %)
    (.readLine page)))


