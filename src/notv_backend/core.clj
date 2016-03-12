
(ns notv-backend.core
  (:require [compojure.api.sweet :refer :all]
            [compojure.route :as route]
            [ring.util.http-response :refer :all]
            [schema.core :as s]))
            
(defroutes static
  (route/resources "/"))

(defapi app
  static
  (GET "/hello" []
    :query-params [name :- String]
    (ok {:message (str "Hello, " name)})))
