(ns clojure-bank.service.consolidation-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as t]
            [clojure-bank.service.consolidation :refer :all]
            [clojure-bank.db.account :as account-db]
            [clojure-bank.db.transaction :as transaction-db]
            [clojure-bank.utils.account :as account-utils]))

(def violations [account-utils/illegal-account-reset])

(def account {"account" {"activeCard" true "availableLimit" 30 "violations" []}})
(def resulting-account {"account" {"activeCard" true "availableLimit" 0 "violations" []}})
(def account-with-violations {"account" {"activeCard" true "availableLimit" 0 "violations" violations}})

(def transaction-1 {"transaction" {"merchant" "Burger King" "amount" 20 "time" (t/date-time 2019 02 13 10 3)}})
(def transaction-2 {"transaction" {"merchant" "MC Donald's" "amount" 10 "time" (t/date-time 2019 02 13 10 4)}})

(deftest test-consolidate
  (testing "valid account"
    (with-redefs [account-db/account (fn [] account) transaction-db/transactions (fn [] [transaction-1 transaction-2])]
      (is (= resulting-account (consolidate [])))))
  (testing "invalid account"
    (with-redefs [account-db/account (fn [] account) transaction-db/transactions (fn [] [transaction-1 transaction-2])]
      (is (= account-with-violations (consolidate violations))))))