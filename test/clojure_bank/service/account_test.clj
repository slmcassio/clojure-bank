(ns clojure-bank.service.account-test
  (:require [clojure.test :refer :all]
            [clojure-bank.service.account :refer :all]
            [clojure-bank.db.account :as account-db]
            [clojure-bank.utils.account :as account-utils]))

(def account {"account" {"activeCard" true "availableLimit" 100 "violations" []}})
(def violations [account-utils/illegal-account-reset])

(deftest test-validate-account
  (testing "valid account"
    (with-redefs [account-db/account (fn [] {})]
      (is (= [] (validate-account account)))))
  (testing "invalid account"
    (with-redefs [account-db/account (fn [] account)]
      (is (= violations (validate-account account))))))