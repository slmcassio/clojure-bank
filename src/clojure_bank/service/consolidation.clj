(ns clojure-bank.service.consolidation
  (:require [clojure-bank.db.account :as account-db]
            [clojure-bank.db.transaction :as transaction-db]
            [clojure-bank.utils.account :as account-utils]
            [clojure-bank.utils.transaction :as transaction-utils]))

(defn deduct-amount
  [account transactions]
  (update-in account account-utils/available-limit-path
             #(- % (transaction-utils/sum-transactions-amount transactions))))

(defn consolidate
  [validations]
  (let [deducted-account (deduct-amount (account-db/account) (transaction-db/transactions))]
    (if (empty? validations)
      deducted-account
      (account-utils/append-account-violations deducted-account validations))))