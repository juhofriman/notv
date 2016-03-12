
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


(defn get-channels [xmlstr]
  (let [channels (filter #(= (:tag %) :channel) (clojure.zip/children (zip-str xmlstr)))]
    (map xml-channel-to-channel channels)))

(defn get-channels-foo []
  (-> (slurp "resources/example-tvdata.xml")
      get-channels))

(defapi app
  (undocumented
    (route/resources "/")
    (GET "/channels" [] (ok (get-channels-foo)))
    (GET "/hello" []
      :query-params [name :- String]
      (ok {:message (str "Hello, " name)}))))
