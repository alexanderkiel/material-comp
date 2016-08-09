(ns material-comp.form
  (:require [cljs.spec :as s]
            [cljs-react-material-ui.core :as ui]
            [material-comp.util :as u]
            [om.next :as om :refer-macros [defui]]))

(defui ValidatingTextField
  Object
  (initLocalState [_]
    {:raw-value ""})
  (render [this]
    (let [{:keys [spec error-formatter on-change]
           :or {error-formatter (comp pr-str :pred)}
           :as props} (om/props this)
          {:keys [raw-value value]} (om/get-state this)]
      (ui/text-field
        (-> props
            (dissoc :ref :spec :error-formatter :on-change)
            (assoc
              :on-change
              (fn [e]
                (let [target-value (u/target-value e)
                      conformed-value (s/conform spec target-value)]
                  (om/update-state! this assoc :value conformed-value
                                    :raw-value target-value)
                  (when on-change (on-change conformed-value))))
              :value raw-value)
            (cond->
              (= ::s/invalid value)
              (assoc :error-text (-> (s/explain-data spec raw-value)
                                     (get-in [::s/problems []])
                                     (error-formatter)))))))))

(def validating-text-field
  "A validating text field wraps a normal text field.

  It conforms raw input according to spec and shows an error text formatted by
  error formatter on non-conforming input. Conforming values can be obtained by
  listening to on-change.

  Additional props:

  :spec            - a spec to which values must conform
  :error-formatter - a function from problem to error string

  Changed props:

  :on-change - a callback function that is fired when the text field's value
               changes. Gets the conforming value or ::s/invalid instead of the
               event.

  A problem is a map of :pred and :val from s/explain-data."
  (om/factory ValidatingTextField))
