(ns joc.cli
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(defn exit [status msg]
  (println msg)
  (System/exit status))

(def cli-options
  [["-p" "--port PORT" "Port number"
    :default 80
    :parse-fn #(Integer/parseInt %)
    :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536" ]]
   ["-v" nil "Verbosity level"
    :id :verbosity
    :default 0
    :assoc-fn (fn [opts k v] (update-in opts [k] inc))]
   ["-x" "--xxx WHATEVER", "A description for this option"
    :default 123]
   ["-h" "--help"
    :id :custom-help ]])

(comment (defn parse-args [args]
  (into {} (map (fn [[k v]] [(keyword (.replace k "--" "")) v])
                (partition 2 args)))))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond
      (:custom-help options) (exit 0 summary)
      (not (nil? errors)) (exit 1 errors)
      :else (exit 0 (parse-opts args cli-options)))))
