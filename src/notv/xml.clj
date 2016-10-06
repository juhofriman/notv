
(ns notv.xml
  (:gen-class)
  (:require [clojure.xml :as xml]
            [clojure.zip :as zip]
            [clj-time.format :as f]))

(def formatter (f/formatter "yyyyMMddHHmmss"))

(defn clean-timestamp [raw]
  (clojure.string/replace raw #" \+\d+" ""))
  ;(f/parse formatter (clean-timestamp...)))

(defn- parse-string [s]
   (clojure.xml/parse
     (java.io.ByteArrayInputStream. (.getBytes s))))

(defn- content
  "Gets content [] from tag"
  [{content :content}]
  content)

(defn tag-name-is
  "True if :tag in tag is name"
  [name {tagname :tag}]
  (= name tagname))

(defn map-channel-from-tag [tag]
  "Maps channel {:id id-of-channel :name name-of-channel} out of tag"
  {:id (get-in tag [:attrs :id])
    ; this is just the way xml is constructed :(
   :name (-> tag content first content first)})

(defn map-programme-from-tag [tag]
  "Maps programme out of tag"
  {:channel (get-in tag [:attrs :channel])
    ; this is just the way xml is constructed :(
   :name (-> tag content first content first)
   :description (-> tag content second content first)})

;;;; USED TRANSDUCERS

; Transducer for filtering channel tags out of xml
(def filter-channels
  (filter (partial tag-name-is :channel)))

(def filter-programmes
    (filter (partial tag-name-is :programme)))

; Transducer for mapping channel xml into more usable form
(def map-channels
  (map (partial map-channel-from-tag)))

(def map-programmes
  (map (partial map-programme-from-tag)))

; Publics

(defn get-channels
  "Gets channels from xmlstr"
  [xmlstr]
  (transduce
    (comp
      filter-channels
      map-channels)
    conj
    (content (parse-string xmlstr))))

(defn get-programmes
  [xmlstr id]
  (transduce
    (comp
      filter-programmes
      map-programmes
      (filter #(= (:channel %) id)))
    conj
    (content (parse-string xmlstr))))
