(ns lein-spa.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [lein-spa.core-test]))

(doo-tests 'lein-spa.core-test)
