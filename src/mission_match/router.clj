(ns mission-match.router)
(use '[mission-match.models.missions :as mission])
(use 'hiccup.core)
(use 'ring.middleware.file)


(defn submit-handler [request]

  {:status 200
   :headers {"Content-Type" "text"}
   :body (mission/existence)
   })


