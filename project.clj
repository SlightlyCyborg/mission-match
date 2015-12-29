(defproject mission_match "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/data.json "0.2.6"]
                 [hiccup "1.0.5"] 
                 [ring "1.4.0"]
                 [mount "0.1.7"]
                 [com.stuartsierra/component "0.3.1"]
                 [com.novemberain/monger "3.0.0-rc2"]
                 ]
  :plugins  [[lein-ring "0.8.11"]]
  :ring  {:handler mission-match.core/main-handler}

)
