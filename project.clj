(defproject material-comp "0.1-SNAPSHOT"
  :description "Components for Material UI"
  :url "http://git.life.uni-leipzig.local/clojure/material-comp"

  :min-lein-version "2.0.0"

  :dependencies [[cljs-react-material-ui "0.2.27"]
                 [org.omcljs/om "1.0.0-alpha46" :exclusions [cljsjs/react]]]

  :plugins [[lein-cljsbuild "1.1.4"]
            [lein-doo "0.1.7"]]

  :profiles
  {:dev {:dependencies [[org.clojure/clojure "1.9.0-alpha13"]
                        [org.clojure/clojurescript "1.9.293"]]}}

  :cljsbuild
  {:builds
   {:test
    {:source-paths ["src" "test"]
     :compiler
     {:output-to "out/testable.js"
      :main material-comp.runner
      :optimizations :simple}}}})
