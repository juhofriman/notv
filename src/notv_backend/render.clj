(ns notv-backend.render
  (:gen-class))

(declare render)

(defn invoke-from-vector
  [[head & tail :as all]]
  (if (fn? head )
    (apply head tail)
    (apply render all)))

(defn evaluate-part
  [part]
  (cond
   (fn? part) (part)
   (vector? part) (invoke-from-vector part)
   :else part))

(defn join-by-nl
  [parts]
  (clojure.string/join \newline parts))

(defn render
  "Renders parts"
  [& parts]
  (-> (map evaluate-part parts)
      (flatten)
      (join-by-nl)))
