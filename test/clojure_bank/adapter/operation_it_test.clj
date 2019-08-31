(ns clojure-bank.adapter.operation-it-test
  (:require [clojure.test :refer :all]
            [clojure-bank.adapter.operation :refer :all]
            [clojure-bank.db.account :as account-db]
            [clojure-bank.db.transaction :as transaction-db]))

(defn with-db [f]
  (account-db/clear-db)
  (transaction-db/clear-db)
  (f))

(use-fixtures :each with-db)

(def raw-account {"account" {"activeCard" true, "availableLimit" 100, "extraField" 1}})
(def raw-transaction {"transaction" {"merchant" "Burger King" "amount" 20 "time" "2019-02-13T10:00:00.000Z" "extraField" 1}})
(def processed-account-1 {"account" {"activeCard" true "availableLimit" 100 "violations" []}})
(def processed-account-2 {"account" {"activeCard" true "availableLimit" 80 "violations" []}})

(deftest ^:integration test-process!
  (testing "process account"
    (is (= processed-account-1 (process-account! raw-account))))
  (testing "process transaction"
    (is (= processed-account-2 (process-transaction! raw-transaction)))))

(deftest ^:integration test-ingest!
  (testing "account ingestion"
    (is (= processed-account-1 (ingest! raw-account))))
  (testing "transaction ingestion"
    (is (= processed-account-2 (ingest! raw-transaction)))))


