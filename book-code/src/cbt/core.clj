(ns cbt.core
  (:gen-class))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Clojure for the brave and true !"))

(defn -main
  "I don't do a whole lot."
  [& args]
  (println (foo "Hello World:")))

;ch03
;multi-arity
(defn x-chop
  "Describe the kind of chop you're inflicting on someone"
  ([name chop-type]
   (str "I " chop-type " chop " name "! Take that!"))
  ([name]
   (x-chop name "karate")))

;destructuring
(defn chooser
  [[first-choice second-choice & unimportant-choices]]
  (println (str "Your first choice is: " first-choice))
  (println (str "Your second choice is: " second-choice))
  (println (str "We're ignoring the rest of your choices. "
                "Here they are in case you need to cry over them: "
                (clojure.string/join ", " unimportant-choices))))

(defn receive-treasure-location
  [{:keys [lat lng] :as treasure-location}]
  (println (str "Treasure lat: " lat))
  (println (str "Treasure lng: " lng)))

;anonymous function
(map (fn [name] (str "Hi, " name))
     ["Darth Vader" "Mr. Magoo"])

(map #(str "Hi, " %)
     ["Darth Vader" "Mr. Magoo"])

;returning functions: closures
(defn inc-maker
  "Create a custom incrementor"
  [inc-by]
  #(+ % inc-by))

