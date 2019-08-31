(ns clojure-bank.utils.account)

(def illegal-account-reset "illegal-account-reset")

(def active-card-path ["account" "activeCard"])
(def available-limit-path ["account" "availableLimit"])
(def violations-path ["account" "violations"])

(defn account-active-card
  [account]
  (get-in account active-card-path))

(defn account-available-limit
  [account]
  (get-in account available-limit-path))

(defn account-violations
  [account]
  (get-in account violations-path))

(defn account?
  [account]
  (if (and (get account "account")
           (boolean? (account-active-card account))
           (number? (account-available-limit account)))
    account))

(defn append-account-violations
  [account violations]
  (assoc-in account
            violations-path
            (into (account-violations account) violations)))