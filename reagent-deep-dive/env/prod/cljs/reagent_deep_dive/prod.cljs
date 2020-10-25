(ns reagent-deep-dive.prod
  (:require [reagent-deep-dive.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
