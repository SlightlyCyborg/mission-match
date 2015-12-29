(ns mission-match.db
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [com.stuartsierra.component :as component]))


(defrecord Connection []
  component/Lifecycle

  (start [component]
    (println "Starting Connectio")
    (let [conn (mg/connect) ]
      (assoc component :conn conn)))
  
  (stop [component]
    (println "Stopping Database")))


(defrecord Database [conn]
  component/Lifecycle

  (start [component]
    (println "Starting Database")
    (let [db (mg/get-db (:conn conn) "mission-match")]
      (assoc component :db db))))

(defn new-connection  []
    (map->Connection  {}))

(defn new-database  []
    (map->Database  {}))

(defn db-system []
  (component/system-map
    :conn (new-connection)
    :db (component/using
          (new-database)
          [:conn])))

(def sys (atom (db-system)))

(defn start! []
  (reset! sys (component/start @sys)))

(defn stop! []
  (reset! sys (component/stop @sys)))

(start!)


(defn get-db []
  (:db (:db @sys)))

(defn test-db-system []
  (mc/insert (get-db) "test" {:text "this is a db-system-test"})
  (if
    (= (:text (mc/find-one-as-map (get-db) "test" {}))
      "this is a db-system-test")
    (println "db-system-test passes")
    (println "db-system-test fails"))
  (mc/remove (get-db) "test"))
