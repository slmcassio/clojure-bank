(ns clojure-bank.service.validation.doubled-transaction-validation-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as t]
            [clojure-bank.service.validation.doubled-transaction-validation :refer :all]
            [clojure-bank.db.transaction :as transaction-db]
            [clojure-bank.utils.transaction :as transaction-utils]))

(def transaction-1 {"transaction" {"merchant" "Guaco" "amount" 20 "time" (t/date-time 2019 02 13 10 1)}})
(def transaction-2 {"transaction" {"merchant" "Dunkin' Donuts" "amount" 90 "time" (t/date-time 2019 02 13 10 1)}})
(def transaction-3 {"transaction" {"merchant" "Guaco" "amount" 90 "time" (t/date-time 2019 02 13 10 1)}})
(def transaction-4 {"transaction" {"merchant" "Guaco" "amount" 90 "time" (t/date-time 2019 02 13 10 3)}})
(def transaction-5 {"transaction" {"merchant" "Guaco" "amount" 90 "time" (t/date-time 2019 02 13 10 4)}})

(deftest test-all-transactions
  (testing "join the current transaction with the ones on database"
    (with-redefs [transaction-db/transactions (fn [] [transaction-1])]
      (is (= [transaction-1 transaction-2] (all-transactions transaction-2)))))
  (testing "join the current transaction with empty database"
    (with-redefs [transaction-db/transactions (fn [] [])]
      (is (= [transaction-1] (all-transactions transaction-1))))))

(deftest test-similar-transactions
  (testing "similar transactions"
    (with-redefs [transaction-db/transactions (fn [] [transaction-1 transaction-2 transaction-3])]
      (is (= [transaction-3 transaction-4] (similar-transactions transaction-4))))))

(deftest test-doubled-transaction-validation
  (testing "valid transaction - not doubled transaction inside 2 minutes interval"
    (with-redefs [transaction-db/transactions (fn [] [transaction-1 transaction-2 transaction-3])]
      (is (= nil (validate transaction-5)))))
  (testing "invalid transaction - doubled transaction inside 2 minutes interval"
    (with-redefs [transaction-db/transactions (fn [] [transaction-1 transaction-2 transaction-3])]
      (is (= transaction-utils/doubled-transaction (validate transaction-4))))))