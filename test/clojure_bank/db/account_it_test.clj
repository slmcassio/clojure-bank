(ns clojure-bank.db.account-it-test
  (:require [clojure.test :refer :all]
            [clojure-bank.db.account :refer :all]
            [clojure-bank.db.account :as account-db]))

(defn with-db [f]
  (account-db/clear-db)
  (f))

(use-fixtures :each with-db)

(def new-account {"account" {"activeCard" true "availableLimit" 100 "violations" []}})

(deftest ^:integration test-account-db
  (testing "account addition"
    (is (= new-account (add-account! new-account))))
  (testing "account reading"
    (is (= new-account (account)))))
