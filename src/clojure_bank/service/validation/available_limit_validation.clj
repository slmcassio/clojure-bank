(ns clojure-bank.service.validation.available-limit-validation
  (:require [clojure-bank.db.account :as account-db]
            [clojure-bank.db.transaction :as transaction-db]
            [clojure-bank.utils.account :as account-utils]
            [clojure-bank.utils.transaction :as transaction-utils]))

(defn all-transactions
  "Retrieve all persisted transactions appending the given one at the end."
  [transaction]
  (conj (transaction-db/transactions) transaction))

(defn validate
  "Checks whether an account has available limit.
  If there is no limit, return 'insufficient-limit'."
  [transaction]
  (if (<= (transaction-utils/sum-transactions-amount (all-transactions transaction))
          (account-utils/account-available-limit (account-db/account)))
    nil
    transaction-utils/insufficient-limit))