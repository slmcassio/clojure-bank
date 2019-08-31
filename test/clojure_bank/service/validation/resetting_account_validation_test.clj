(ns clojure-bank.service.validation.resetting-account-validation-test
  (:require [clojure.test :refer :all]
            [clojure-bank.service.validation.resetting-account-validation :refer :all]
            [clojure-bank.utils.account :as account-utils]
            [clojure-bank.db.account :as account-db]))

(def account {"account" {"activeCard" true "availableLimit" 100 "violations" []}})

(deftest test-validate
  (testing "valid account"
    (with-redefs [account-db/account (fn [] {})]
      (is (= nil (validate account)))))
  (testing "invalid account"
    (with-redefs [account-db/account (fn [] account)]
      (is (= account-utils/illegal-account-reset (validate account))))))