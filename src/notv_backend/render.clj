
(ns notv-backend.render
  (:gen-class))

(defn invoke-from-vector [[func & args]] (apply func args))

(defn render [& a]
  (clojure.string/join
    \newline
    (map
      (fn [function-candidate]
        (cond
          (fn? function-candidate) (function-candidate)
          (vector? function-candidate) (invoke-from-vector function-candidate)
          :else function-candidate))
     a)))
