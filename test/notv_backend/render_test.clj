(ns notv-backend.render-test
  (:require [clojure.test :refer :all]
            [notv-backend.render :refer :all]))

(defn expected-content
  [expected actual]
  (= expected actual))

(defn nl-separated [& lines]
  (clojure.string/join \newline lines))

(deftest render-test

  (testing "rendering strings directly"
    (is (expected-content
          "hello"
          (render "hello")))
    (is (expected-content
          (nl-separated "hello" "world")
          (render "hello" "world")))
    (is (expected-content
          (nl-separated "hello" "world" "I'm bored")
          (render "hello" "world" "I'm bored"))))

  (testing "rendering functions"
    (is (expected-content
          "hello"
          (render (fn [] "hello"))))
    (is (expected-content
          (nl-separated "hello" "world")
          (render  (fn [] "hello")  (fn [] "world"))))
    (is (expected-content
          (nl-separated "hello" "world" "I'm bored")
          (render "hello" "world"  (fn [] "I'm bored")))))

  (testing "rendering functions with argumets"
    (is (expected-content
          "hello"
          (render [(fn [a] a) "hello"])))
    (is (expected-content
          "hello world"
          (render [(fn [a b] (str a " " b)) "hello" "world"])))
    (is (expected-content
          (nl-separated "hello world" "I'm bored" "lovingly adored")
          (render
            [(fn [a b] (str a " " b)) "hello" "world"]
            "I'm bored"
            [(fn [a] (str "lovingly" " " a)) "adored"])))))          
