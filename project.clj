(defproject material-comp "0.1-SNAPSHOT"
  :description "Components for Material UI"
  :url "http://git.life.uni-leipzig.local/clojure/material-comp"

  :min-lein-version "2.0.0"

  :dependencies [[cljs-react-material-ui "0.2.20"]
                 [org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.9.89"]
                 [org.omcljs/om "1.0.0-alpha41" :exclusions [cljsjs/react]]])
