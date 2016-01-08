(ns mission-match.models.missions
    (:require  [monger.core :as mg]
               [monger.collection :as mc]
               [monger.result :as mr]
               [mission-match.db :as db]
               )
     (:import org.bson.types.ObjectId)) 

(defn existence [] "I exist")

(defn insert-mission [args]
  (if (mr/acknowledged?
    (mc/insert (db/get-db) "missions" {
      :_id      (ObjectId.) 
      :mission  (args "mission") 
      :age      (args "age")
      :sex      (args "sex")
      :password (args "password")
      :username (args "username")}))
    "success" "failure")) 

(defn get-mission-by-user [user-id]
  (mc/find-one-as-map (db/get-db) "missions" {:user-id user-id}))


(defn get-missions []
  (mc/find-maps))

(defn clear-collection []
 (mc/remove (db/get-db) "missions")
 (mc/empty? (db/get-db) "missions"))

(defn display [username]
  (map 
    (fn [a-mission]
      [:li (a-mission :mission)]) 
    (mc/find-maps (db/get-db) "missions" {})))

(defn test-insert-mission []
  (insert-mission {:text "My mission is to code" :user-id 1})
  (if (= "My mission is to code" (:text (get-mission-by-user 1)))
         (println "Passes")
         (println "Fails"))
  (mc/remove-by-id (db/get-db) "missions" (:_id (get-mission-by-user 1))))

