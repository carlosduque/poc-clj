(ns threads.conc.core
  (:require [threads.util.core :as u])
  (:gen-class))

(def url "http://www.gutenberg.org/cache/epub/103/pg103.txt")
(def don-quixote-url "http://www.gutenberg.org/cache/epub/996/pg996.txt")

(defn get-document-delay
  [url]
  (let [url url
        id (u/md5 url)] ;; should really hash the contents, not the url :-)
    {:id id
     :url url
    :mime "text/plain"
    :content (delay (slurp url))}))

(defn get-document-future
  [url]
  (let [url url
        id (u/md5 url)] ;; should really hash the contents, not the url :-)
    {:id id
     :url url
    :mime "text/plain"
    :content (future (slurp url))}))

(defn get-document
  [url]
  (let [url url
        id (u/md5 url)] ;; should really hash the contents, not the url :-)
    {:id id
     :url url
    :mime "text/plain"
    :content (slurp url)}))

(u/with-new-thread (get-document url))

;;futures
(def f (get-document-future url))
(realized? (:content f))
@(:content f) ;; the contents of the book should already be there!

;;promises
(def p (promise))
(realized? p)
(u/with-new-thread (deliver p (get-document url)))
(realized? p)
@p

;;delays
(def d (get-document-delay url))
(realized? (:content d))
@(:content d) ;; will get the contents of the book now at deref time!
(realized? (:content d))

