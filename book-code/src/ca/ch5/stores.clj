(ns ch5.stores)

;;(defn query-stores [product stores]
;;  (for [store stores]
;;    (future (query store product))))


(defn query-stores [product stores]
  (let [futures (doall
                       (for [store stores]
                         (future (query store product))))]
    (map deref futures)))

;;promises
(defn launch-timed []
  (let [begin-promise (promise)
        end-promise (promise)]
    (future (deliver begin-promise (System/currentTimeMillis))
            (long-running-task)
            (deliver end-promise (System/currentTimeMillis)))
    (println "task begin at" @begin-promise)
    (println "task end at" @end-promise)))


