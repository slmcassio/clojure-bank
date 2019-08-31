(ns clojure-bank.service.transaction
  (:require [clojure-bank.db.transaction :as transaction-db]
            [clojure-bank.service.consolidation :as consolidation-service]
            [clojure-bank.service.validation.doubled-transaction-validation :as doubled-transaction-validation]
            [clojure-bank.service.validation.high-frequency-small-interval-validation :as high-frequency-small-interval-validation]
            [clojure-bank.service.validation.available-limit-validation :as available-limit-validation]
            [clojure-bank.service.validation.card-blocked-validation :as card-blocked-validation]))

(defn validate-transaction
  [transaction]
  (filterv some? (map #(% transaction) [card-blocked-validation/validate
                                        available-limit-validation/validate
                                        high-frequency-small-interval-validation/validate
                                        doubled-transaction-validation/validate])))

(defn execute-transaction-operation!
  [transaction]
  (let [validations (validate-transaction transaction)]
    (if (empty? validations)
      (transaction-db/add-transaction! transaction))
    (consolidation-service/consolidate validations)))


