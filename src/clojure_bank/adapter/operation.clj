(ns clojure-bank.adapter.operation
  (:require [clojure-bank.service.account :as account-service]
            [clojure-bank.service.transaction :as transaction-service]
            [clojure-bank.utils.account :as account-utils]
            [clojure-bank.utils.transaction :as transaction-utils]
            [clojure-bank.utils.time :as time-utils]))

(defn parse-account
  [account]
  {"account" {
              "activeCard"     (account-utils/account-active-card account)
              "availableLimit" (account-utils/account-available-limit account)
              "violations"     []}})

(defn parse-transaction
  [transaction]
  {"transaction" {
                  "merchant"   (transaction-utils/transaction-merchant transaction)
                  "amount"     (transaction-utils/transaction-amount transaction)
                  "time"       (time-utils/string->time (transaction-utils/transaction-time transaction))}})

(defn parse-account-back
  [account]
  {"account" {
              "activeCard"     (account-utils/account-active-card account)
              "availableLimit" (account-utils/account-available-limit account)
              "violations"     (account-utils/account-violations account)}})

(def process-account! (comp parse-account-back
                            account-service/execute-account-operation!
                            parse-account))

(def process-transaction! (comp parse-account-back
                                transaction-service/execute-transaction-operation!
                                parse-transaction))

(defn ingest!
  [operation]
  (cond
    (account-utils/account? operation) (process-account! operation)
    (transaction-utils/transaction? operation) (process-transaction! operation)
    :else ""))