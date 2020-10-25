(ns cbt.core-test
  (:require [expectations :refer :all]
            [cbt.core :refer :all]))


;this func should live in cbt.core
(defn palindrome?
  [raw-string]
  (= (seq raw-string)
     (reverse raw-string)))

;; A palindrome should return true
(expect true (palindrome? "avid diva"))
