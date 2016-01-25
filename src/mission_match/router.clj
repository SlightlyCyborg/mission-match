(ns mission-match.router)
(use '[mission-match.models.missions :as mission])
(use '[clojure.data.json :as json])
(use 'hiccup.core)
(use 'ring.middleware.file)
(use '[mission-match.models.missions :as mission])
(use '[mission-match.models.semanticcomparisons :as sc])


(defn  print-n-return [thing]
  (do 
    (println thing)
    thing))

(defn login-handler [request]
   (let  [args (request :form-params)
          res { :status 200
                :headers {"Content-Type" "text"}} 
          user-exists (> (count
                           (mission/get-mission-by-user (args :login-username))) 0)]
    
    (println args)
    (println ( = (:password (mission/get-mission-by-user (args "login-username")))
              (mission/salt-and-sha-password (args :login-password))))  

    (let [pass-matches    
      (if user-exists
        (= (mission/salt-and-sha-password (args :login-password)) 
          (:password (mission/get-mission-by-user (args :login-username)))) 
        false)]
    (assoc res 
           :body
            (if (and pass-matches user-exists)
                "<p>You have successfully logged in. </p><a href='/'>No time for autoredirect</a>"
                "You entered the wrong password or this accout does not exist")
           :session 
           (if pass-matches
            {:username (args :username)} 
            {}))))) 


(defn handle-new-mission [args]
  (mission/insert-mission args)
  (sc/handle-new-mission-statement
    (mission/get-mission-by-user (args :username))
    (mission/get-other-missions (args :username))))

(defn display [username]
  (map 
    (fn [a-mission]
      [:li 
       [:div {:id "mission-wrapper"} 
        [:ul
          [:li [:label "User:"
            [:p (a-mission :username)]]]
          [:li [:label "Mission Statement:"
            [:p (a-mission :mission)]]]]
        ]]) 
    (mission/get-missions-by-matches
      (sc/get-sorted-matches-by-username username 10)
      username)))

(defn submit-handler [request]
  (let [form-params (request :form-params)
        mission-status (handle-new-mission form-params)]
    {:status 200
    :headers {"Content-Type" "text"}
    :session {:username (form-params "username")}
    :body (json/write-str {:status mission-status})
    }))
 
