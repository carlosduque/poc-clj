(ns sandbox.gui
  (:import [javax.swing JFrame JLabel JTextField JButton]
           [java.awt.event ActionListener WindowListener]
           [java.awt GridLayout]))

(defn swing []
  (let [frame (JFrame. "Fund manager")
        label (JLabel. "Exit on close")]
    (doto frame
      (.add label)
      (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)
      (.addWindowListener
        (proxy [WindowListener] []
          (windowClosing [evt]
            (println "Whoop"))))
      (.setVisible true))))

(defn -main [& args]
  (swing))

(comment
  (do (require 'clojure.pprint)
      (use 'clojure.repl)
      (use 'clojure.java.javadoc)))

(defn celsius []
  (let [frame (JFrame. "Celsius Converter")
        temp-text (JTextField.)
        celsius-label (JLabel. "Celsius")
        convert-button (JButton. "Convert")
        fahrenheit-label (JLabel. "Fahrenheit")]
    (doto frame
      (.setLayout (GridLayout. 2 2 3 3))
      (.add temp-text) (.add celsius-label)
      (.add convert-button) (.add fahrenheit-label)
      (.setSize 300 80) (.setVisible true))))

(defn components [frame]
  (if (nil? frame)
    []
    (let [children (.getComponents frame)
          child-components (map components children)]
      (flatten
        (conj child-components frame)))))

(defn buttons [frame]
  (let [is-button? (fn [child]
                     (instance? javax.swing.JButton child))]
    (filter is-button? (components frame))))

;;- Implement and extend Java Interfaces and classes (but Clojure prefers interfaces)

(def listener
  (reify java.awt.event.ActionListener
    (actionPerformed [this e] prn)))

;; define cel->fahr
;;- functions are callable and Runnable
;;- EVERYTHING is interfaces (=> you can extend clojure in Java)
;;- Primitive arithmetic support for performance

(comment
  (.addActionListener
    convert-button
    (reify java.awt.event.ActionListener
      (actionPerformed [this e]
        (let [cel (Integer/parseInt (.getText temp-text))
              fahr (str (celsius2far cel))]
          (.setText fahrenheit-label fahr))))))

