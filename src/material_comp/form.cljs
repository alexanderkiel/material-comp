(ns material-comp.form
  "Form elements like validating text fields."
  (:require [cljs.spec :as s]
            [cljs-react-material-ui.core :as ui]
            [clojure.string :as str]
            [material-comp.format :as format]
            [material-comp.util :as u]
            [om.next :as om :refer-macros [defui]]))

(defui ValidatingTextField
  Object
  (initLocalState [_]
    {:raw-value ""})
  (render [this]
    (let [{:keys [id spec error-formatter on-change]
           :or {error-formatter format/error-formatter}
           :as props} (om/props this)
          {:keys [raw-value value]} (om/get-state this)]
      (ui/text-field
        (-> props
            (dissoc :ref :spec :error-formatter :on-change)
            (assoc
              :on-change
              (fn [e]
                (let [tval (u/target-value e)
                      cval (s/conform spec tval)]
                  (om/update-state! this assoc :value cval :raw-value tval)
                  (when on-change (on-change cval))))
              :value raw-value)
            (cond->
              (= ::s/invalid value)
              (assoc :error-text (->> (s/explain-data spec raw-value)
                                      ::s/problems
                                      (map (partial error-formatter id))
                                      (str/join " ")))))))))

(def validating-text-field
  "A validating text field wraps a normal text field.

  It conforms raw input according to spec and shows an error text formatted by
  error formatter on non-conforming input. Conforming values can be obtained by
  listening to on-change.

  Additional props:

  :spec            - a spec to which values must conform
  :error-formatter - a function from id and problem to error string (optional)

  Changed props:

  :on-change - a callback function that is fired when the text field's value
               changes. Gets the conforming value or ::s/invalid instead of the
               event.

  A problem is a map of :pred and :val from s/explain-data.

  If no error formatter is specified the default error formatter from
  material-comp.format is used."
  (om/factory ValidatingTextField))
