(ns threads.async.core
  (:require [threads.util.core :as u]
            [org.clojure/core.async "0.4.500"])
  (:gen-class))

(defn launch-n-go-blocks
  [n]
  (let [c (async/chan)]
    (dotimes [i n]
      (async/go
                (Thread/sleep 10)
                (async

