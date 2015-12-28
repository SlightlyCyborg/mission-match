(ns mission-match.db
  (:require [monger.core :as mg]
            [com.stuartsierra.component :as component]))

(defstate conn
    :start  mg/connect 
    :stop   mg/disconnect)

(use '[mission-match.db :refer [conn]])

(mount.core/defstate db 
  :start (mg/get-db conn "mission-match"))
 

(defrecord Database []
  component/Lifecycle

  (start [component]
    (println "Starting Database")
    (let [conn (mg/connect)]
      (assoc component :conn conn))))
