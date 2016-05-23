
(ns notv-backend.core
  (:gen-class)
  (:require [notv-backend.render :as render]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [org.httpkit.server :refer [run-server]]))

(defn is-curl?
  "True if given compojure request :headers are intepreted as curl sent"
  [{userAgent "user-agent"}]
  (.contains userAgent "curl"))

(defn execute-if-fn-or-return
  "Executes function-candidate with & args if function-candidate is function
   Otherwise just returns function-candidate"
  [function-candidate & args]
  (let [apply-args (or args [])]
    (if (fn? function-candidate)
      (apply function-candidate apply-args)
      function-candidate)))

(defn curl-or-die
  "Creates compojure response for given request and handler function or value"
  [request pass & args]
  (if (is-curl? (:headers request))
    ; Request was by curl - evaluate
    { :status 200
      :headers {"content-type" "text/plain"}
      :body (apply execute-if-fn-or-return pass args)}
    ; Shut doors, this shop only serves curlers
    { :status 403
      :headers {"content-type" "text/plain"}
      :body "This shop serves only customers coming in with user-agent: *curl*"}))

(defroutes app
  (GET "/" request (curl-or-die request render/render-channels))
  (GET "/:id" [id :as request] (curl-or-die request render/render-programmes id))
  (ANY "*" request (curl-or-die request "WHOOPSIE, no droids here")))

(defn start-this-shop []
  (run-server app {:port 5000}))

(defn -main []
  (start-this-shop))
