
(ns notv-backend.xml
  (:gen-class)
  (:require [clojure.xml :as xml]
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

(defn get-channels-from-data []
  (-> (slurp (get-tvdata-file-location))
      (get-channels)))

(defn get-programmes-from-data [channelId]
  (-> (slurp (get-tvdata-file-location))
      (get-programmes channelId)))
