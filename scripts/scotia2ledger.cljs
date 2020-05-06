#!/usr/bin/env planck

;clojurescript
(ns ledger.core)

(require '[planck.core :refer [*in* slurp spit]]
         '[planck.io :refer [reader]])

(defn read-csv
  []
  )

(defn read-csv-file
  [filename]
  (let [readr (reader filename)]
    (doall
      (read-csv readr))))

(defn csv-data->maps [csv-data]
  (map zipmap
       (->> (first csv-data)
            (map keyword)
            repeat)
       (rest csv-data)))

(defn -main
  "Read a csv file"
  [& args]
  (println (csv-data->maps (read-csv-file (first args)))))
