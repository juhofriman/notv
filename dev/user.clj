(ns user
  (:require
    [clojure.tools.namespace.repl :as ns-tools]
    [notv.core :refer :all]
    [notv.core-test :refer :all]))

(defn run-tests []
  (clojure.test/run-tests 'notv.core-test))

(def http-app (atom nil))

(defn start-notv []
  (reset! http-app (start-this-shop)))

(defn restart-notv []
  (if @http-app
    (@http-app))
  (ns-tools/refresh :after 'user/start-notv))
