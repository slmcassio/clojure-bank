(ns clojure-bank.service.validation.resetting-account-validation
  (:require [clojure-bank.db.account :as account-db]
            [clojure-bank.utils.account :as account-utils]))

(defn validate
  "Checks whether an account was already created or not.
  If it's already created, return 'illegal-account-reset'."
  [account]
  (if (empty? (account-db/account))
    nil
    account-utils/illegal-account-reset))