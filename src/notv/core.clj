
(ns notv.core
  (:gen-class)
  (:require [notv.render :as render]
            [notv.xml :as xml]
            [notv.util :as util]
            [clj-time.format :as f]
            [clojure.java.io :as io]
            [ring.middleware.json :refer [wrap-json-response]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [org.httpkit.server :refer [run-server]]))

(defn get-tvdata-file-location []
  (let [env-path (System/getenv "TVDATA_FILE")]
    (if (empty? env-path)
      (io/file (io/resource "example-tvdata.xml"))
      env-path)))

(defn get-tvdata
  []
  (slurp (get-tvdata-file-location)))

(defn get-revision
  []
  (if-let [file (io/resource "gitrevision.txt")]
    (slurp file)
    "local"))

(def unformatter (f/formatter "HH:mm"))
(def type-mappings [org.joda.time.DateTime (partial f/unparse unformatter)])

(defn map-types
  [d] 
  (apply util/type-map (cons d type-mappings)))

(defroutes api
  (GET "/status" request {:headers {} :body (get-revision)})
  (GET "/" request {:headers {} :body (xml/get-channels (get-tvdata))})
  (GET "/:id" [id :as request] {:headers {} :body (map map-types (xml/get-programmes (get-tvdata) id))}))
(def app
  (-> api
      wrap-json-response))

(defn start-this-shop []
  (run-server app {:port 5000}))

(defn -main []
  (start-this-shop))
