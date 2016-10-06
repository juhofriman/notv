
(ns notv.core
  (:gen-class)
  (:require [notv.render :as render]
            [notv.xml :as xml]
            [clojure.java.io :as io]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [org.httpkit.server :refer [run-server]]))

;; tää kuuluis varmaan coreen
(defn get-tvdata-file-location []
  (let [env-path (System/getenv "TVDATA_FILE")]
    (if (empty? env-path)
      (io/file (io/resource "example-tvdata.xml"))
      env-path)))

(defn get-tvdata
  []
  (slurp (get-tvdata-file-location)))

(defroutes app
  (GET "/" request {:headers {"Content-type" "text/plain"} :body (xml/get-channels (get-tvdata))})
  (GET "/:id" [id :as request] {:headers {"Content-type" "text/plain"} :body (xml/get-programmes (get-tvdata) id)}))

(defn start-this-shop []
  (run-server app {:port 5000}))

(defn -main []
  (start-this-shop))
