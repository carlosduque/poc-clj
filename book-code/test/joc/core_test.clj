(ns joc.core-test
  (:require [clojure.test :refer :all]
            [joc.core :refer :all]))


;;;this func should live in joc.core
;;(defn palindrome?
;;  [raw-string]
;;  (= (seq raw-string)
;;     (reverse raw-string)))
;;
;;(deftest palindrome-test
;;  (testing "simple"
;;    (is (palindrome? "avid diva"))))
;;
;;
;;
;;(run-tests)
