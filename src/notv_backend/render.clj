
(ns notv-backend.render
  (:gen-class)
  (:require [notv-backend.xml :as noxml]
            [clj-time.format :as f]))

(def formatter (f/formatter "HH:mm"))

(defn render-channels []
  (reduce
    str
    (map
      (fn [{name :name id :id}]
        (str name " / " id "\n"))
      (noxml/get-channels-from-data))))

(defn render-programmes [id]
  (reduce
    str
    (map
      (fn [{name :name start :start end :end}]
        (str (f/unparse formatter start) "-" (f/unparse formatter end) " " name "\n"))
      (noxml/get-programmes-from-data id))))
