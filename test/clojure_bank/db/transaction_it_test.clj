(ns clojure-bank.db.transaction-it-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as t]
            [clojure-bank.db.transaction :refer :all]
            [clojure-bank.db.transaction :as transaction-db]))

(defn with-db [f]
  (transaction-db/clear-db)
  (f))

(use-fixtures :each with-db)

(def transaction-1 {"transaction" {"merchant" "Burger King" "amount" 20 "time" (t/date-time 2019 02 12 10)}})
(def transaction-2 {"transaction" {"merchant" "MC Donald's" "amount" 10 "time" (t/date-time 2019 02 13 10)}})

(deftest ^:integration test-transaction-db
  (testing "transaction addition"
    (is (= transaction-1 (add-transaction! transaction-1))))
  (testing "second transaction addition"
    (is (= transaction-2 (add-transaction! transaction-2))))
  (testing "transaction reading"
    (is (= [transaction-1 transaction-2] (transactions)))))
