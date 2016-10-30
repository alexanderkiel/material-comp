(ns material-comp.format
  (:require [goog.string :as gstr]))

;; ---- Error Formatter -------------------------------------------------------

(defn- normalize
  "Retains symbols in pred."
  [pred]
  (cond
    (seq? pred) (filter some? (map normalize pred))
    (symbol? pred) pred))

(defn- dispatch [id {:keys [pred via]}]
  (cond-> [(normalize pred)]
          id (conj id)
          (and id (seq via)) (conj via)))

(defmulti error-formatter
  "Dispatches on normalized pred and optional id and via of problem.

  Dispatches in this order:

  * [normalized-pred id via]
  * [normalized-pred id]
  * [normalized-pred]"
  {:arglists '([id problem])}
  dispatch)

(defmethod error-formatter :default
  [id problem]
  (let [dispatch-val (dispatch id problem)]
    (case (count dispatch-val)
      3 (error-formatter id (dissoc problem :via))
      2 (error-formatter nil problem)
      (pr-str (:pred problem)))))

;; ---- Error Formatter Impl --------------------------------------------------

(defmethod error-formatter '[(<= (count %))]
  [_ {:keys [pred val]}]
  (let [max-length (last pred)]
    (gstr/format "Maximale Länge von %s Zeichen um %s Zeichen überschritten"
                 max-length (- (count val) max-length))))

(defmethod error-formatter '[(not (blank? %))]
  [_ _]
  "Wert erforderlich")
