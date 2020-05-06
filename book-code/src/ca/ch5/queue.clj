(ns ch5.queue)

(defn queue
  "Create a new stateful queue"
  []
  (ref clojure.lang.PersistentQueue/EMPTY))

(defn enq
  "Enqueue item in q"
  [q item]
  (dosync
          (alterq conj item)))

(defn deq
  "Dequeue item from q (nil if none)"
  [q]
  (dosync
          (let [item (peek @q)]
            (alter q pop)
            item)))


