(ns sandbox.async
  (:require [sandbox.util :as util]
            [clojure.core.async :as async :refer [>! <! >!! <!! go chan buffer
                                                  close! thread alts! alts!! timeout]])
  (:gen-class))

;;channels
;are like mini-queues or better yet, like PIPES

(def echo-chan (chan))

;taking the value out of the channel
;futures share the threadpool with agents and since it will block if it can't
;take a value, that thread is one less thread in the pool
@(future (println (<!! echo-chan)))


;;go block
;they run your processes on a thread pool for go blocks (of 2 + number of cores),
;will park (not exactly block) until it reads something out of the channel
;parking means, the process its removed from the thread so the thread can be reused
(go
    (println (<! echo-chan)))

;;put some message in the channel
(>!! echo-chan "ketchup")
(>!! echo-chan "onions")
(>!! echo-chan "pickles")

;channels can be buffered
;which means, it will take a number of values before blocking
(def echo-buffer (chan 2))
(>!! echo-buffer "chipotle")
(>!! echo-buffer "mustard")
(>!! echo-buffer "mayonaise") ;this one will block
(<!! echo-buffer)

;thread
;used for long-running tasks instead of go blocks so not to clog the thread pool
;handling the go blocks
;they return a channel
(let [t (thread (println (<!! echo-chan)))]
  (<!! t))

