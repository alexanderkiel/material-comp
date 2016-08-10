(ns material-comp.format
  (:require [goog.string :as gstr]))

;; ---- Error Formatter -------------------------------------------------------

(defn- normalize
  "Retains symbols in pred."
  [pred]
  (cond
    (seq? pred) (filter some? (map normalize pred))
    (symbol? pred) pred))

(defn- dispatch [_ {:keys [pred via]}]
  (cond-> (normalize pred)
          (seq via) (vector via)))

(defmulti error-formatter
  "Dispatches on normalized pred and optional via of problem.

  When via is empty dispatches on normalized pred. Otherwise first dispatches
  on the vector of normalized pred and via and than falls back to normalized
  pred only."
  {:arglists '([path problem])}
  dispatch)

(defmethod error-formatter :default
  [path problem]
  (let [dispatch-val (dispatch path problem)]
    (if (vector? dispatch-val)
      (error-formatter path (dissoc problem :via))
      (pr-str (:pred problem)))))

;; ---- Error Formatter Impl --------------------------------------------------

(defmethod error-formatter '(<= (count %))
  [_ {:keys [pred val]}]
  (let [max-length (last pred)]
    (gstr/format "Maximale Länge von %s Zeichen um %s Zeichen überschritten"
                 max-length (- (count val) max-length))))

(defmethod error-formatter '(not (blank? %))
  [_ _]
  "Wert erforderlich")
