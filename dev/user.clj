(ns user
  (:require
    [notv-backend.core :refer :all]
    [notv-backend.core-test :refer :all]))

(defn run-tests []
  (clojure.test/run-tests 'notv-backend.core-test))
