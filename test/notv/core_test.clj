(ns notv.core-test
  (:require [clojure.test :refer :all]
            [notv.core :refer :all]))

(deftest core-tests

  (testing "is-curl?"
    ; is-curl? takes compojure request :headers map
    (is (true? (is-curl? {"user-agent" "curl user agent line"})))
    (is (false? (is-curl? {"user-agent" "Mozilla firefox"}))))

  (testing "execute-if-fn-or-return"
    (is (= "foo" (execute-if-fn-or-return (fn [] "foo"))))
    (is (= "arg" (execute-if-fn-or-return (fn [a] a) "arg")))
    (is (= "argbarg" (execute-if-fn-or-return (fn [a b] (str a b)) "arg" "barg")))
    (is (= "bar" (execute-if-fn-or-return "bar"))))

  (testing "curl-or-die"

    (let [response (curl-or-die {:headers {"user-agent" "Regular browser"}} "just value")]
      ; Status must be 403
      (is (= 403 (:status response)))
      ; content-type must be text-plain
      (is (= "text/plain" (get-in response [:headers "content-type"]))))

    (let [response (curl-or-die {:headers {"user-agent" "curl"}} "just value")]
      ; body must be evaluated
      (is (= "just value" (:body response)))
      ; Status must be 403
      (is (= 200 (:status response)))
      ; content-type must be text-plain
      (is (= "text/plain" (get-in response [:headers "content-type"]))))))
