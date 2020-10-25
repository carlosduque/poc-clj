(ns work_threads.core)

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

