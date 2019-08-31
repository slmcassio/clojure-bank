(ns clojure-bank.service.validation.card-blocked-validation
  (:require [clojure-bank.db.account :as account-db]
            [clojure-bank.utils.account :as account-utils]
            [clojure-bank.utils.transaction :as transaction-utils]))

(defn validate
  "Checks whether an account has the card blocked.
  If it is blocked, return 'card-blocked'."
  [transaction]
  (if (account-utils/account-active-card (account-db/account))
    nil
    transaction-utils/card-blocked))