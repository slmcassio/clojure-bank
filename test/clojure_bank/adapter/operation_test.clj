(ns clojure-bank.adapter.operation-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as t]
            [clojure-bank.adapter.operation :refer :all]))

(def raw-account {"account" {"activeCard" true, "availableLimit" 100, "extraField" 1}})
(def raw-transaction {"transaction" {"merchant" "Burger King" "amount" 20 "time" "2019-02-13T10:00:00.000Z" "extraField" 1}})

(def parsed-account {"account" {"activeCard" true "availableLimit" 100 "violations" []}})
(def parsed-transaction {"transaction" {"merchant" "Burger King" "amount" 20 "time" (t/date-time 2019 02 13 10)}})

(def account {"account" {"activeCard" true "availableLimit" 100 "violations" ["violation"]}})
(def output-account {"account" {"activeCard" true "availableLimit" 100 "violations" ["violation"]}})

(deftest test-parse-operation
  (testing "account parser"
    (is (= parsed-account (parse-account raw-account))))
  (testing "transaction parser"
    (is (= parsed-transaction (parse-transaction raw-transaction)))))

(deftest test-parse-operation-back
  (testing "account parser"
    (is (= output-account (parse-account-back account)))))