(ns notv.util-test
  (:require [clojure.test :refer :all]
            [notv.util :refer :all]))

(deftest type-mapper-test
  (testing "Without mappings returns values as is"
    (let [m (type-map {:a 1 :b "hello"})]
      (is (= 1 (:a m)))
      (is (= "hello" (:b m)))))
  (testing "Simple type mapper definition"
    (is (= (str 1) (:a (type-map {:a 1} java.lang.Long str))))
    (let [m (type-map {:a 1 :b "hello"} java.lang.Long inc java.lang.String #(.toUpperCase %))]
      (is (= (inc 1) (:a m)))
      (is (= "HELLO" (:b m))))))
