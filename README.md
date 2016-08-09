# Material Components

Components for [Material UI][1].

## Install

```clojure
[material-comp "0.1-SNAPSHOT"]
```

## Usage

```clojure
(use 'material-comp.form)
```

## Validating Text Field

### Typical Name Field:

The parent component keeps a temporary `:project` in it's local state and populates the name into it. In case the name of invalid, the projects name will contain the value `::s/invalid`. On submit, the whole project can be validated by a map validating spec.

```clojure
(s/def :project/name (s/and string? #(<= 1 (count %) 50)))

(defn- max-length-error-formatter [max-length]
  (fn [{:keys [val]}]
    (gstr/format "Maximale Länge von %s Zeichen um %s Zeichen überschritten"
                 max-length (- (count val) max-length))))

(form/validating-text-field
  {:floating-label-text "Name"
   :spec :project/name
   :error-formatter
   (fn [{:keys [val] :as p}]
     (if (zero? (count val))
       "Name erforderlich"
       ((max-length-error-formatter 50) p)))
   :on-change #(om/update-state! this update :project assoc :project/name %)})
```

[1]: <http://www.material-ui.com>
