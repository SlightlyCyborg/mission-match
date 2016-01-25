(ns mission-match.models.semanticcomparisons
    (:require  [monger.core :as mg]
               [monger.collection :as mc]
               [monger.result :as mr]
               [monger.query :as q]
               [mission-match.db :as db])
     (:import org.bson.types.ObjectId)) 
(use '[semantic-similarity.core :as ss])


(defn create-matches [new-mission other-missions]
     (map
       (fn [mission2] 
         {:users
          [(new-mission :username)
          (mission2 :username)]

         :score
         (ss/get-sentence-similarity
           (new-mission :mission)
           (mission2 :mission))
         })
       other-missions))

(defn insert-match [match]
  (if (mr/acknowledged?
    (mc/insert (db/get-db) "matches" match))
    "success" "failure")) 

(defn save-matches [matches]
  (doseq [match matches]
    (insert-match match)))

(defn handle-new-mission-statement [mission other-missions]
  (-> mission
    (create-matches other-missions)
    save-matches))

(defn get-matches-by-username [username]
 (mc/find-maps (db/get-db) "matches" {:users username}))  

(defn get-sorted-matches-by-username [username num-to-fetch]
  (let [num-to-fetch (or num-to-fetch 10)]
    (q/with-collection (db/get-db) "matches"
      (q/find  {:users username})
      (q/sort  (array-map :score -1))
      (q/limit num-to-fetch))))


