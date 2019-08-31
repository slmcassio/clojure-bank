(ns clojure-bank.port.cli-it-test
  (:require [clojure.test :refer :all]
            [clojure-bank.port.cli :refer :all]
            [clojure-bank.db.account :as account-db]
            [clojure-bank.db.transaction :as transaction-db]))

(defn with-db [f]
  (account-db/clear-db)
  (transaction-db/clear-db)
  (f))

(use-fixtures :each with-db)

(def raw-account-input "{ \"account\": { \"activeCard\": true, \"availableLimit\": 100 } }")
(def raw-transaction-input "{ \"transaction\": { \"merchant\": \"Burger King\", \"amount\": 20, \"time\": \"2019-02-13T10:00:00.000Z\" } }")
(def invalid-input "{")

(def processed-account {"account" {"activeCard" true "availableLimit" 100 "violations" []}})
(def processed-invalid "")

(def account-output-1 "{\"account\":{\"activeCard\":true,\"availableLimit\":100,\"violations\":[]}}\n")
(def account-output-2 "{\"account\":{\"activeCard\":true,\"availableLimit\":80,\"violations\":[]}}\n")
(def invalid-output "\n")

(deftest ^:integration test-process-output!
  (testing "account input"
    (is (= account-output-1 (with-out-str (process-output! processed-account)))))
  (testing "invalid input"
    (is (= invalid-output (with-out-str (process-output! processed-invalid))))))

(deftest ^:integration test-process-input!
  (testing "account input"
    (is (= account-output-1 (with-out-str (process-input! raw-account-input)))))
  (testing "transaction input"
    (is (= account-output-2 (with-out-str (process-input! raw-transaction-input)))))
  (testing "invalid input"
    (is (= invalid-output (with-out-str (process-input! invalid-input))))))

(deftest ^:integration test-prompt-operations!
  (testing "account input"
    (is (= account-output-1 (with-in-str raw-account-input (with-out-str (prompt-operations!))))))
  (testing "transaction input"
    (is (= account-output-2 (with-in-str raw-transaction-input (with-out-str (prompt-operations!))))))
  (testing "invalid input"
    (is (= invalid-output (with-in-str invalid-input (with-out-str (prompt-operations!)))))))
