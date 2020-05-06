(ns workthreads.core
  (:gen-class))

;use ref because of transactional nature
;of peek+pop
(def queue (ref clojure.lang.PersistentQueue/EMPTY))

(defn enqueue!
  [item]
  (dosync (alter queue conj item)))

(defn dequeue!
  []
  (dosync
    (let [v (peek @queue)]
      (alter queue pop)
      v)))

(defn build-work
  ([]
   (build-work (rand-name)))
  ([name]
  {:name name}))

(defn rand-name
  ([]
   (rand-name ["aardvark" "eagle" "hippo" "posum" "snake" "cat" "wolf" "tiger"]))
  ([coll]
  (coll(int (rand (count coll))))))

(build-work (rand-name names))
(build-work)

(repeatedly 4 build-work)
