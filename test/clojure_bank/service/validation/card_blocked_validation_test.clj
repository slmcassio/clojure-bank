(ns clojure-bank.service.validation.card-blocked-validation-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as t]
            [clojure-bank.service.validation.card-blocked-validation :refer :all]
            [clojure-bank.db.account :as account-db]
            [clojure-bank.utils.transaction :as transaction-utils]))

(def account {"account" {"activeCard" true "availableLimit" 100 "violations" []}})
(def inactive-account {"account" {"activeCard" false "availableLimit" 100 "violations" []}})

(def transaction {"transaction" {"merchant" "Burger King" "amount" 20 "time" (t/date-time 2019 02 13 10 3)}})

(deftest test-card-blocked-validation
  (testing "valid transaction - card active"
    (with-redefs [account-db/account (fn [] account)]
      (is (= nil (validate transaction)))))
  (testing "invalid transaction - card inactive"
    (with-redefs [account-db/account (fn [] inactive-account)]
      (is (= transaction-utils/card-blocked (validate transaction))))))