(ns material-comp.util)

;; ---- Events ----------------------------------------------------------------

(defn target-value [e]
  (.. e -target -value))
