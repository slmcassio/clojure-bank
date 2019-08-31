(ns clojure-bank.utils.account-test
  (:require [clojure.test :refer :all]
            [clojure-bank.utils.account :refer :all]))

(def account {"account" {"activeCard" true "availableLimit" 100 "violations" []}})
(def account-with-violations {"account" {"activeCard" true, "availableLimit" 100 "violations" ["illegal-account-reset"]}})
(def inactive-account {"account" {"activeCard" false "availableLimit" 100 "violations" []}})
(def invalid-account-no-card-info {"account" {"availableLimit" 100 "violations" []}})
(def invalid-account-no-available-info {"account" {"activeCard" false "violations" []}})
(def invalid-account-with-no-violation-info {"account" {"activeCard" true "availableLimit" 100}})
(def invalid-account {})

(deftest test-restrictions
  (testing "restriction values"
    (is (= "illegal-account-reset" illegal-account-reset))))

(deftest test-account-patch
  (testing "account information path"
    (is (= ["account" "activeCard"] active-card-path))
    (is (= ["account" "availableLimit"] available-limit-path))
    (is (= ["account" "violations"] violations-path))))

(deftest test-account-active-card
  (testing "account with active card"
    (is (= true (account-active-card account))))
  (testing "account with inactive card"
    (is (= false (account-active-card inactive-account))))
  (testing "account with no information card"
    (is (= nil (account-active-card invalid-account-no-card-info)))))

(deftest test-account-available-limit
  (testing "account with available limit"
    (is (= 100 (account-available-limit account))))
  (testing "account with no available limit information"
    (is (= nil (account-available-limit invalid-account-no-available-info)))))

(deftest test-account-violations
  (testing "account with no violation"
    (is (= [] (account-violations account))))
  (testing "account with violations"
    (is (= ["illegal-account-reset"] (account-violations account-with-violations))))
  (testing "account with no violation information"
    (is (= nil (account-violations invalid-account-with-no-violation-info)))))

(deftest test-account?
  (testing "valid account"
    (is (= account (account? account))))
  (testing "valid account with violations"
    (is (= account-with-violations (account? account-with-violations))))
  (testing "invalid account"
    (is (= nil (account? invalid-account))))
  (testing "invalid account with no card information"
    (is (= nil (account? invalid-account-no-card-info))))
  (testing "invalid account with no available limit information"
    (is (= nil (account? invalid-account-no-available-info)))))

(deftest test-append-account-violations
  (testing "append violation"
    (is (= account-with-violations (append-account-violations account ["illegal-account-reset"])))))
