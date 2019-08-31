(ns clojure-bank.service.validation.high-frequency-small-interval-validation-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as t]
            [clojure-bank.service.validation.high-frequency-small-interval-validation :refer :all]
            [clojure-bank.db.transaction :as transaction-db]
            [clojure-bank.utils.transaction :as transaction-utils]))

(def transaction-1 {"transaction" {"merchant" "Burger King" "amount" 10 "time" (t/date-time 2019 02 13 10 1)}})
(def transaction-2 {"transaction" {"merchant" "MC Donald's" "amount" 20 "time" (t/date-time 2019 02 13 10 1)}})
(def transaction-3 {"transaction" {"merchant" "Guaco" "amount" 30 "time" (t/date-time 2019 02 13 10 2)}})
(def transaction-4 {"transaction" {"merchant" "Starbucks" "amount" 40 "time" (t/date-time 2019 02 13 10 4)}})

(deftest test-all-transactions
  (testing "join the current transaction with the ones on database"
    (with-redefs [transaction-db/transactions (fn [] [transaction-1])]
      (is (= [transaction-1 transaction-2] (all-transactions transaction-2)))))
  (testing "join the current transaction with empty database"
    (with-redefs [transaction-db/transactions (fn [] [])]
      (is (= [transaction-1] (all-transactions transaction-1))))))

(deftest test-high-frequency-small-interval-validation
  (testing "valid transaction - only one transaction in less than 2 minutes"
    (with-redefs [transaction-db/transactions (fn [] [transaction-1])]
      (is (= nil (validate transaction-2)))))
  (testing "valid transaction - new transaction after 2 minutes interval"
    (with-redefs [transaction-db/transactions (fn [] [transaction-1 transaction-2])]
      (is (= nil (validate transaction-4)))))
  (testing "invalid transaction - third transaction inside 2 minutes interval"
    (with-redefs [transaction-db/transactions (fn [] [transaction-1 transaction-2])]
      (is (= transaction-utils/high-frequency-small-interval (validate transaction-3))))))
