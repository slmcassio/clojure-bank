(ns clojure-bank.service.transaction-it-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as t]
            [clojure-bank.utils.transaction :as transaction-utils]
            [clojure-bank.service.transaction :refer :all]
            [clojure-bank.db.account :as account-db]
            [clojure-bank.db.transaction :as transaction-db]))

(defn with-db [f]
  (account-db/add-account! {"account" {"activeCard" true "availableLimit" 100 "violations" []}})
  (transaction-db/clear-db)
  (f))

(use-fixtures :each with-db)

(def account {"account" {"activeCard" true "availableLimit" 100 "violations" []}})
(def resulting-account {"account" {"activeCard" true "availableLimit" 80 "violations" []}})
(def account-with-violations {"account" {"activeCard" true "availableLimit" 80 "violations" [transaction-utils/doubled-transaction]}})
(def transaction {"transaction" {"merchant" "Burger King" "amount" 20 "time" (t/date-time 2019 02 13 10 3)}})

(deftest ^:integration test-execute-transaction-operation!
  (testing "execute valid transaction"
    (is (and (= resulting-account (execute-transaction-operation! transaction))
             (= [transaction] (transaction-db/transactions)))))
  (testing "execute invalid transaction"
    (is (and (= account-with-violations (execute-transaction-operation! transaction))
             (= [transaction] (transaction-db/transactions))))))