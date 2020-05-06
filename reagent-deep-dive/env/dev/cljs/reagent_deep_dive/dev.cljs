(ns ^:figwheel-no-load reagent-deep-dive.dev
  (:require
    [reagent-deep-dive.core :as core]
    [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(core/init!)
