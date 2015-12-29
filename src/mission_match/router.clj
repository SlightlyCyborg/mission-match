(ns mission-match.router)
(use '[mission-match.models.missions :as mission])
(use '[clojure.data.json :as json])
(use 'hiccup.core)
(use 'ring.middleware.file)


(defn submit-handler [request]

  (let [form-params (request :form-params)
        mission-status (mission/insert-mission form-params)]

  {:status 200
   :headers {"Content-Type" "text"}
   :body (json/write-str {:status mission-status})
   }))


