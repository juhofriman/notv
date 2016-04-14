
(ns notv-backend.core
  (:require [compojure.api.sweet :refer :all]
            [compojure.route :as route]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clojure.java.io :as io]
            [clj-time.format :as f]))

(def formatter (f/formatter "yyyyMMddHHmmss"))

(defn zip-str [s]
  (zip/xml-zip
    (xml/parse (java.io.ByteArrayInputStream. (.getBytes s)))))

(defn xml-channel-to-channel [{attrs :attrs content :content}]
  {:id (:id attrs)
   :name (first (:content (first content)))})

(defn clean-timestamp [raw]
  (clojure.string/replace raw #" \+\d+" ""))

(defn xml-programme-to-programme [{attrs :attrs content :content}]
  { :name (first (:content (first content)))
    :desc (first (:content (second content)))
    :start (f/parse formatter (clean-timestamp (:start attrs)))
    :end (f/parse formatter (clean-timestamp (:stop attrs)))})

(defn get-tvdata-file-location []
  (let [env-path (System/getenv "TVDATA_FILE")]
    (if (empty? env-path)
      (io/file (io/resource "example-tvdata.xml"))
      env-path)))

(defn get-programmes [xmlstr channelId]
  (map
    xml-programme-to-programme
    (filter
      #(and
        (= (:tag %) :programme)
        (= (-> % :attrs :channel) channelId))
      (clojure.zip/children (zip-str xmlstr)))))

(defn get-channels [xmlstr]
  (let [channels (filter #(= (:tag %) :channel) (clojure.zip/children (zip-str xmlstr)))]
    (map xml-channel-to-channel channels)))

(defn get-channels-foo []
  (-> (slurp (get-tvdata-file-location))
      (get-channels)))

(defn get-programmes-foo [channelId]
  (-> (slurp (get-tvdata-file-location))
      (get-programmes channelId)))

(defn is-curl [userAgent]
  (.contains userAgent "curl"))

(def curl-formatter (f/formatter "HH:mm"))

(defn neat-curl-response-programmes [programmes]
  (str
    "NOTV \n\n"
    (apply str (map (fn [{name :name s :start e :end}] (str (f/unparse curl-formatter s) "-" (f/unparse curl-formatter e) " " name "\n")) programmes))
    "\n"))

(defn neat-curl-response-channels [channels]
  (str
    "NOTV \n\n"
    (apply str (map (fn [{id :id name :name}] (str name " /" id "\n")) channels))
    "\n"))

(defapi app
  (undocumented
    (route/resources "/")
    (GET "/api/channels" [:as req]
      (if (is-curl (-> req :headers (get "user-agent")))
        (ok (neat-curl-response-channels (get-channels-foo)))
        (ok (get-channels-foo))))
    (GET "/api/channel/:channelId" [channelId :as req]
      (if (is-curl (-> req :headers (get "user-agent")))
        (ok (neat-curl-response-programmes (get-programmes-foo channelId)))
        (ok (get-programmes-foo channelId))))))
