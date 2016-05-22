(ns user
  (:require
    [clojure.tools.namespace.repl :as ns-tools]
    [notv-backend.core :refer :all]
    [notv-backend.core-test :refer :all]))

(defn run-tests []
  (clojure.test/run-tests 'notv-backend.core-test))

(def http-app (atom nil))

(defn start-notv []
  (reset! http-app (start-this-shop)))

(defn restart-notv []
  (if @http-app
    (@http-app))
  (ns-tools/refresh :after 'user/start-notv))
