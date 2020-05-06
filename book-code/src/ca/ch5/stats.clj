(ns ch5.stats)

(def pageview-stat (agent 0))

(add-watch
           pageview-stat
           :pageview
           (fn [key agent old new]
             (when (zero? (mod new 10))
               (remote-send key new))))

(defn inc-stat [stat]
  (send-off stat inc))

