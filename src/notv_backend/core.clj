
(ns notv-backend.core
  (:require [compojure.api.sweet :refer :all]
            [compojure.route :as route]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [clojure.xml :as xml]
            [clojure.zip :as zip]))

(defn zip-str [s]
  (zip/xml-zip
    (xml/parse (java.io.ByteArrayInputStream. (.getBytes s)))))

(defn xml-channel-to-channel [{attrs :attrs content :content}]
  {:id (:id attrs)
   :name (first (:content (first content)))})

(defn xml-programme-to-programme [{attrs :attrs content :content}]
     {:id (:id attrs)
      :name (first (:content (first content)))
      :desc (first (:content (second content)))})


(defn get-programmes [xmlstr channelId]
  (map xml-programme-to-programme (filter #(and (= (:tag %) :programme) (= (-> % :attrs :channel) channelId)) (clojure.zip/children (zip-str xmlstr)))))

(defn get-channels [xmlstr]
  (let [channels (filter #(= (:tag %) :channel) (clojure.zip/children (zip-str xmlstr)))]
    (map xml-channel-to-channel channels)))

(defn get-channels-foo []
  (-> (slurp "/data/tvdata.xml")
      (get-channels)))

(defn get-programmes-foo [channelId]
  (-> (slurp "/data/tvdata.xml")
      (get-programmes channelId)))

(defapi app
  (undocumented
    (route/resources "/")
    (GET "/api/channels" [] (ok (get-channels-foo)))
    (GET "/api/channel/:channelId" [channelId]
      (ok (get-programmes-foo channelId)))))
