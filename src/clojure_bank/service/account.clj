(ns clojure-bank.service.account
  (:require [clojure-bank.db.account :as account-db]
            [clojure-bank.service.consolidation :as consolidation-service]
            [clojure-bank.service.validation.resetting-account-validation :as resetting-account-validation]))

(defn validate-account
  [account]
  (filterv some? (map #(% account) [resetting-account-validation/validate])))

(defn execute-account-operation!
  [account]
  (let [validations (validate-account account)]
    (if (empty? validations)
      (account-db/add-account! account))
    (consolidation-service/consolidate validations)))