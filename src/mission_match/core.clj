(ns mission-match.core)
(use 'hiccup.core)


(defn make-select [attrs options]
  (into [] (concat [:select attrs] 
                   [[:option {:value ""} (clojure.string/capitalize (get attrs :name ""))]];display name as option
                   options)))

(defn make-options [vals]
  (map (fn [val] [:option {:value val} val])  vals))

(def home
  (html [:html
          [:head]
          [:body
            [:h1 "Mission Match"]
            [:form
              [:textarea {:id "mission" :placeholder "Enter your mission..." :name "mission"}]
              (make-select 
                {:id "age" :name "age"}
                (make-options (range 18 35)))
              (make-select
                {:id "sex" :name "sex"}
                [[:option {:value "male"} "Male"]
                [:option {:value "female"} "Female"]])
              [:input {:id "email" :name "email" :placeholder "Reddit Username"}]
              [:input {:id "password" :name "password" :placeholder "Password"}]
              ]]]))

(defn main-handler  [request]
      {:status 200
       :headers  {"Content-Type" "text/html"}
       :body home})
