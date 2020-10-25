(ns ^:figwheel-no-load reagent-example.dev
  (:require
    [reagent-example.core :as core]
    [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(core/init!)
