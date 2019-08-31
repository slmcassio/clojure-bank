(ns clojure-bank.service.account-it-test
  (:require [clojure.test :refer :all]
            [clojure-bank.service.account :refer :all]
            [clojure-bank.db.account :as account-db]))

(defn with-db [f]
  (account-db/clear-db)
  (f))

(use-fixtures :each with-db)

(def account {"account" {"activeCard" true "availableLimit" 100 "violations" []}})
(def account-with-violations {"account" {"activeCard" true "availableLimit" 100 "violations" ["illegal-account-reset"]}})

(deftest ^:integration test-execute-account-operation!
  (testing "execute valid account"
    (is (and (= account (execute-account-operation! account))
             (= account (account-db/account)))))
  (testing "execute invalid account"
    (is (and (= account-with-violations (execute-account-operation! account))
             (= account (account-db/account))))))