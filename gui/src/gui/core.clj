(ns gui.core
  (:gen-class
    :extends javafx.application.Application)
  (:import (javafx.application Application)
           (javafx.fxml FXMLLoader)
           (javafx.scene Parent Scene)
           (javafx.stage Stage)))

(defn -main [& args]
  (Application/launch gui.core (into-array String [])))

(defn -start [this primaryStage]
  (let [loc (clojure.java.io/resource "fxml/window.fxml")
        root (FXMLLoader/load ^java.net.URL loc)
        scene (Scene. root 300 250)]
    (.setScene primaryStage scene)
    (.show primaryStage)))

(defn -startxxx [this primaryStage]
  (let [loc (clojure.java.io/resource "resources/fxml/window.fxml")
        fxmlloader (FXMLLoader.)]
    (.setLocation fxmlloader loc)
    (let [root (.load fxmlloader )
          scene (Scene. root 300 250)]
      (.setScene primaryStage scene)
      (.show primaryStage))))

