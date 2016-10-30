(ns material-comp.runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [material-comp.form-test]))

(doo-tests 'material-comp.form-test)
