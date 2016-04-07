
(ns notv-backend.core
  (:require [compojure.api.sweet :refer :all]
            [compojure.route :as route]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [clojure.xml :as xml]
            [clojure.zip :as zip]
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
  (-> (slurp "/data/tvdata.xml")
      (get-channels)))

(defn get-programmes-foo [channelId]
  (-> (slurp "/data/tvdata.xml")
      (get-programmes channelId)))

(defn is-curl [userAgent]
  (.contains userAgent "curl"))

(defn neat-curl-response [programmes]
  (str
    "NOTV \n\n"
    (apply str (map (fn [{name :name}] (str name "\n")) programmes))
    "\n"))

(defapi app
  (undocumented
    (route/resources "/")
    (GET "/api/channels" [] (ok (get-channels-foo)))
    (GET "/api/channel/:channelId" [channelId :as req]
      (if (is-curl (-> req :headers (get "user-agent")))
        (ok (neat-curl-response (get-programmes-foo channelId)))
        (ok (get-programmes-foo channelId))))))
