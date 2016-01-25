(ns mission-match.core)
(use 'hiccup.core)
(use 'ring.middleware.file)
(use 'ring.middleware.params)
(use '[mission-match.router :as router])
(use 'ring.middleware.session)


(defn make-select [attrs options]
  (into [] (concat [:select attrs] 
                   [[:option {:value ""} (clojure.string/capitalize (get attrs :name ""))]];display name as option
                   options)))

(defn make-options [vals]
  (map (fn [val] [:option {:value val} val])  vals))

(defn build-home [request]
  (html [:html
          [:head
           [:link {:rel "stylesheet" 
                   :href "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
                   :integrity "sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7"
                   :crossorigin "anonymous"
                   }]
           [:link {:rel "stylesheet"
                   :href "site.css"
                   }]
           ]
          [:body
            [:div {:style "position:relative; margin-top:100px; margin-left:100px;" }
              (if (contains? request :session)
              [:h1 (str "Welcom to Mission Match, " ((request :session) :username))]
              [:h1 "Mission Match"])
              [:div {:style "float:left;"}
              [:form {:action "/"}
                [:div {:class "form-group"} 
                [:textarea {:id "mission" :placeholder "Enter your mission..." :name "mission"}]]
                [:div {:class "form-group"} 
                (make-select 
                  {:id "age" :name "age"}
                  (make-options (range 18 35)))]
                [:div {:class "form-group"}
                (make-select
                  {:id "sex" :name "sex"}
                  [[:option {:value "male"} "Male"]
                  [:option {:value "female"} "Female"]])]
                [:div {:class "form-group"} 
                [:input {:id "username" :name "username" :placeholder "A Username"}]]
                [:div {:class "form-group"} 
                [:input {:type "password" :id "password" :name "password" :placeholder "A Password"}]]
                [:button {:id "submit-button"} "Submit Mission"]]
              [:div 
               [:h1 "Log in if you already submitted a mission"]
               [:form {:action "login" :method "post"}
                  [:input {:name "login-username" :placeholder "username"}] 
                  [:input {:type "password" :name "login-password" :placeholder "password"}] 
                  [:button {:id "login-button"} "Login"]

               ]]
              ]
              (if (contains? request :session)
              [:div {:style "float:left; margin-left:50px;"}
              [:ol
                (router/display ((request :session) "username"))]])]
            [:script {:src "//code.jquery.com/jquery-1.11.3.min.js"}]
            [:script {:src "js/app.js"}]
            ]]))



(defn home-handler  [request]
  
    {:status 200
      :headers  {"Content-Type" "text/html"}
      :body (build-home request)})

(defn handler [request]
  (let [routes 
    {"/login" router/login-handler
     "/submit" router/submit-handler
     "/" home-handler }]
  ((routes (request :uri)) request)))


(def main-handler 
  (-> handler 
      (wrap-file "resources/public")
      (wrap-params)
      (wrap-session)))
