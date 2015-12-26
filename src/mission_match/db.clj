(ns mission-match.db
  (:require [mount.core :refer [defstate]]
            [monger.core :as mg]))

(defstate conn
    :start  mg/connect 
    :stop   mg/disconnect)

(use '[mission-match.db :refer [conn]])

(mount.core/defstate db 
  :start (mg/get-db conn "mission-match"))
 
