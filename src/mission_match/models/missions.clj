(ns mission-match.models.missions
    (:require  [monger.core :as mg]
               [monger.collection :as mc]
               [mount.core mount])
     (:import org.bson.types.ObjectId)) 




(mount.core/defstate conn
    :start  mg/connect 
    :stop   mg/disconnect)


(mount.core/defstate db 
  :start (mg/get-db conn "mission-match"))

(defn existence [] "I exist")

(defn insert-mission [args]
  (mc/insert db "missions" {:_id (ObjectId.) 
                            :text (args :text) 
                            :user-id (args :user-id)}))

(defn get-mission-by-user [user-id]
  (mc/find-one-as-map db "missions" {:user-id user-id}))


(defn test-insert-mission []
  (insert-mission {:text "My mission is to code" :user-id 1})
  (if (= "My mission is to code" (:text (get-mission-by-user 1)))
         (println "Passes")
         (println "Fails")))

