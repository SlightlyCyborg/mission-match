(ns mission-match.models.missions
    (:require  [monger.core :as mg]
               [monger.collection :as mc]
               [monger.result :as mr]
               [monger.query :as q]
               [mission-match.db :as db])

     (:import org.bson.types.ObjectId)) 

(use 'digest)


(defn salt-and-sha-password [password]
  (digest/sha-256 (str password "shake it like a salt shaker")))

(defn insert-mission [args]
  (if (mr/acknowledged?
    (mc/insert (db/get-db) "missions" {
      :_id      (ObjectId.) 
      :mission  (args "mission") 
      :age      (args "age")
      :sex      (args "sex")
      :password (salt-and-sha-password (args "password"))
      :username (args "username")}))
    "success" "failure")) 


(defn get-mission-by-user [username]
  (mc/find-one-as-map (db/get-db) "missions" {:username username}))


(defn get-other-missions [mission]
  (mc/find-maps (db/get-db) "missions" 
                {:username {"$ne" (mission :username)}}))

(defn get-missions-by-matches [matches username]
    (map 
      #(mc/find-one-as-map (db/get-db) "missions" {:username %1}) 
      (flatten 
        (map
          #(filter
            (fn [matched-user] (if (= username matched-user) false true))
            (:users %1 ))
    matches))))

(defn get-missions []
  (mc/find-maps (db/get-db) "missions" {}))

(defn clear-collection []
 (mc/remove (db/get-db) "missions")
 (mc/empty? (db/get-db) "missions"))



(defn test-insert-mission []
  (insert-mission {:text "My mission is to code" :user-id 1})
  (if (= "My mission is to code" (:text (get-mission-by-user 1)))
         (println "Passes")
         (println "Fails"))
  (mc/remove-by-id (db/get-db) "missions" (:_id (get-mission-by-user 1))))

