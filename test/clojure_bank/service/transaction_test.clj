(ns clojure-bank.service.transaction-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as t]
            [clojure-bank.utils.transaction :as transaction-utils]
            [clojure-bank.service.transaction :refer :all]
            [clojure-bank.db.account :as account-db]
            [clojure-bank.db.transaction :as transaction-db]))


(def account {"account" {"activeCard" true "availableLimit" 100 "violations" []}})
(def inactive-account {"account" {"activeCard" false "availableLimit" 100 "violations" []}})

(def transaction-1 {"transaction" {"merchant" "Burger King" "amount" 20 "time" (t/date-time 2019 02 13 10 3)}})
(def transaction-2 {"transaction" {"merchant" "MC Donald's" "amount" 10 "time" (t/date-time 2019 02 13 10 4)}})
(def transaction-3 {"transaction" {"merchant" "Guaco" "amount" 90 "time" (t/date-time 2019 02 13 10 5)}})

(def violations [transaction-utils/card-blocked transaction-utils/insufficient-limit transaction-utils/high-frequency-small-interval transaction-utils/doubled-transaction])

(deftest test-validate-transaction
  (testing "valid transaction"
    (with-redefs [account-db/account (fn [] account) transaction-db/transactions (fn [] [transaction-1])]
      (is (= [] (validate-transaction transaction-2)))))
  (testing "invalid transaction"
    (with-redefs [account-db/account (fn [] inactive-account) transaction-db/transactions (fn [] [transaction-1 transaction-2 transaction-3])]
      (is (= violations (validate-transaction transaction-3))))))