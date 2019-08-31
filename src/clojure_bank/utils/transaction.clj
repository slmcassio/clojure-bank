(ns clojure-bank.utils.transaction
  (:import (org.joda.time DateTime))
  (:require [clojure-bank.utils.time :as time-utils]))

(def insufficient-limit "insufficient-limit")
(def card-blocked "card-blocked")
(def high-frequency-small-interval "high-frequency-small-interval")
(def doubled-transaction "doubled-transaction")

(def merchant-path ["transaction" "merchant"])
(def amount-path ["transaction" "amount"])
(def time-path ["transaction" "time"])

(defn transaction-merchant
  [transaction]
  (get-in transaction merchant-path))

(defn transaction-amount
  [transaction]
  (get-in transaction amount-path))

(defn transaction-time
  [transaction]
  (get-in transaction time-path))

(defn transaction?
  [transaction]
  (if (and (get transaction "transaction")
           (string? (transaction-merchant transaction))
           (number? (transaction-amount transaction))
           (instance? DateTime (time-utils/string->time (transaction-time transaction))))
    transaction))

(defn transactions-amount
  [transactions]
  (map transaction-amount transactions))

(def sum #(reduce + %))

(def sum-transactions-amount (comp sum transactions-amount))

(defn transactions-time
  [transactions]
  (map transaction-time transactions))

(defn transaction-time-minus-2
  [transaction]
  (time-utils/subtract-minutes (transaction-time transaction) 2))