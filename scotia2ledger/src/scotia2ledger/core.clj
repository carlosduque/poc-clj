(ns scotia2ledger.core
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io])
  (:gen-class))

(defn read-csv-file
  [filename]
  (with-open [reader (io/reader filename)]
    (doall
      (csv/read-csv reader))))

(defn csv-data->maps [csv-data]
  (map zipmap
       (->> (first csv-data) ;; First row is the header
            (map keyword) ;; Drop if you want string keys instead
            repeat)
       (rest csv-data)))

(defn -main
  "Read a csv file"
  [& args]
  (println (csv-data->maps (read-csv-file (first args)))))
