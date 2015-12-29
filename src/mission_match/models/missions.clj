(ns mission-match.models.missions
    (:require  [monger.core :as mg]
               [monger.collection :as mc]
               [mission-match.db :as db])
     (:import org.bson.types.ObjectId)) 

(defn existence [] "I exist")

(defn insert-mission [args]
  (mc/insert (db/get-db) "missions" {:_id (ObjectId.) 
                            :text (args :text) 
                            :user-id (args :user-id)})
  
  )

(defn get-mission-by-user [user-id]
  (mc/find-one-as-map (db/get-db) "missions" {:user-id user-id}))


(defn test-insert-mission []
  (insert-mission {:text "My mission is to code" :user-id 1})
  (if (= "My mission is to code" (:text (get-mission-by-user 1)))
         (println "Passes")
         (println "Fails"))
  (mc/remove-by-id (db/get-db) "missions" (:_id (get-mission-by-user 1))))

