(ns user
  (:require
    [notv-backend.core :refer :all]
    [notv-backend.core-test :refer :all]
    [clojure.tools.namespace.repl :refer [refresh]]))

(defn run-tests []
  (refresh)
  (clojure.test/run-tests 'notv-backend.core-test))

(defn reload []
  (refresh))
