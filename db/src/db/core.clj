(ns db.core
  (:gen-class)
  (:require [clojure.java.jdbc :as j]
            [clojure.pprint :as pp]))

(def db-spec {:dbtype "db2"
               :classname "com.ibm.db2.jcc.DB2Driver"
               ;:subprotocol "" ;the prefix 'jdbc:' is added automatically
               ;:subname "xyz;ABC_DE=3"
               :dbname "mydb"
               :host "192.168.0.1"
               :port 5432
               :user "myuser"
               :password "s3cret"})
(def qry "select * from mytable where id = 99999")

(defn -main
  [& args]
  (let [arg (first args)
        records (j/query db-spec arg)]
    (doseq [r records]
      (pp/pprint r))))

