(ns clojure-bank.port.cli-test
  (:require [clojure.test :refer :all]
            [clojure-bank.port.cli :refer :all]))

(def raw-account-input "{ \"account\": { \"activeCard\": true, \"availableLimit\": 100 } }")
(def raw-transaction-input "{ \"transaction\": { \"merchant\": \"Burger King\", \"amount\": 20, \"time\": \"2019-02-13T10:00:00.000Z\" } }")
(def invalid-input "{")

(def account {"account" {"activeCard" true "availableLimit" 100}})
(def transaction {"transaction" {"merchant" "Burger King" "amount" 20 "time" "2019-02-13T10:00:00.000Z"}})
(def invalid-output "")

(deftest test-parse-input
  (testing "account parser"
    (is (= account (parse-input raw-account-input))))
  (testing "transaction parser"
    (is (= transaction (parse-input raw-transaction-input))))
  (testing "invalid parser"
    (is (= invalid-output (parse-input invalid-input)))))
