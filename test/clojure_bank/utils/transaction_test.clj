(ns clojure-bank.utils.transaction-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as t]
            [clojure-bank.utils.transaction :refer :all]))


(def raw-transaction {"transaction" {"merchant" "Burger King" "amount" 20 "time" "2019-02-13T10:00:00.000Z"}})

(def transaction-1 {"transaction" {"merchant" "Burger King" "amount" 20 "time" (t/date-time 2019 02 13 10 3)}})
(def transaction-2 {"transaction" {"merchant" "MC Donald's" "amount" 10 "time" (t/date-time 2019 02 13 10 4)}})

(def invalid-transaction-no-merchant {"transaction" {"amount" 20, "time" "2019-02-13T10:00:00.000Z"}})
(def invalid-transaction-no-amount {"transaction" {"merchant" "Burger King" "time" "2019-02-13T10:00:00.000Z"}})
(def invalid-transaction-no-time {"transaction" {"merchant" "Burger King" "amount" 20}})
(def invalid-transaction {})

(deftest test-restrictions
  (testing "restriction values"
    (is (= "insufficient-limit" insufficient-limit))
    (is (= "card-blocked" card-blocked))
    (is (= "high-frequency-small-interval" high-frequency-small-interval))))

(deftest test-transaction-patch
  (testing "transaction information path"
    (is (= ["transaction" "merchant"] merchant-path))
    (is (= ["transaction" "amount"] amount-path))
    (is (= ["transaction" "time"] time-path))))

(deftest test-transaction-merchant
  (testing "transaction with merchant"
    (is (= "Burger King" (transaction-merchant raw-transaction))))
  (testing "transaction with no merchant"
    (is (= nil (transaction-merchant invalid-transaction-no-merchant)))))

(deftest test-transaction-amount
  (testing "transaction with amount"
    (is (= 20 (transaction-amount raw-transaction))))
  (testing "transaction with no amount"
    (is (= nil (transaction-amount invalid-transaction-no-amount)))))

(deftest test-transaction-time
  (testing "transaction with time"
    (is (= "2019-02-13T10:00:00.000Z" (transaction-time raw-transaction))))
  (testing "transaction with no time"
    (is (= nil (transaction-time invalid-transaction-no-time)))))

(deftest test-transaction?
  (testing "valid transaction"
    (is (= raw-transaction (transaction? raw-transaction))))
  (testing "invalid transaction"
    (is (= nil (transaction? invalid-transaction))))
  (testing "invalid transaction with no merchant"
    (is (= nil (transaction? invalid-transaction-no-merchant))))
  (testing "invalid transaction with no amount"
    (is (= nil (transaction? invalid-transaction-no-amount))))
  (testing "invalid transaction with no time"
    (is (= nil (transaction? invalid-transaction-no-time)))))

(deftest test-transactions-amount
  (testing "transaction amount extraction"
    (is (= [20 10] (transactions-amount [raw-transaction transaction-2]))))
  (testing "empty transaction amount extraction"
    (is (= [] (transactions-amount [])))))

(deftest test-sum
  (testing "sum"
    (is (= 30 (sum [20 10]))))
  (testing "empty sum"
    (is (= 0 (sum [])))))

(deftest test-sum-transactions-amount
  (testing "sum"
    (is (= 30 (sum-transactions-amount [raw-transaction transaction-2]))))
  (testing "empty sum"
    (is (= 0 (sum-transactions-amount [])))))

(deftest test-transactions-time
  (testing "transaction time extraction"
    (is (= [(t/date-time 2019 02 13 10 3) (t/date-time 2019 02 13 10 4)] (transactions-time [transaction-1 transaction-2]))))
  (testing "empty transaction time extraction"
    (is (= [] (transactions-amount [])))))

(deftest test-transaction-time-minus-2
  (testing "transaction time extraction"
    (is (t/equal? (t/date-time 2019 02 13 10 1) (transaction-time-minus-2 transaction-1)))))