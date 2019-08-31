(ns clojure-bank.service.validation.available-limit-validation-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as t]
            [clojure-bank.service.validation.available-limit-validation :refer :all]
            [clojure-bank.db.account :as account-db]
            [clojure-bank.db.transaction :as transaction-db]
            [clojure-bank.utils.transaction :as transaction-utils]))

(def account {"account" {"activeCard" true "availableLimit" 100 "violations" []}})

(def transaction-1 {"transaction" {"merchant" "Burger King" "amount" 20 "time" (t/date-time 2019 02 13 10 3)}})
(def transaction-2 {"transaction" {"merchant" "MC Donald's" "amount" 10 "time" (t/date-time 2019 02 13 10 4)}})
(def transaction-3 {"transaction" {"merchant" "Guaco" "amount" 90 "time" (t/date-time 2019 02 13 10 1)}})

(deftest test-all-transactions
  (testing "join the current transaction with the ones on database"
    (with-redefs [transaction-db/transactions (fn [] [transaction-2])]
      (is (= [transaction-2 transaction-1] (all-transactions transaction-1)))))
  (testing "join the current transaction with empty database"
    (with-redefs [transaction-db/transactions (fn [] [])]
      (is (= [transaction-1] (all-transactions transaction-1))))))

(deftest test-available-limit-validation
  (testing "valid transaction - available limit"
    (with-redefs [account-db/account (fn [] account) transaction-db/transactions (fn [] [transaction-2])]
      (is (= nil (validate transaction-1)))))
  (testing "invalid transaction - unavailable limit"
    (with-redefs [account-db/account (fn [] account) transaction-db/transactions (fn [] [transaction-3])]
      (is (= transaction-utils/insufficient-limit (validate transaction-1))))))