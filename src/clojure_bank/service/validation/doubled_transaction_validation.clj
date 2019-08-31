(ns clojure-bank.service.validation.doubled-transaction-validation
  (:require [clojure-bank.db.transaction :as transaction-db]
            [clojure-bank.utils.transaction :as transaction-utils]
            [clojure-bank.utils.time :as time-utils]))

(defn all-transactions
  "Retrieve all persisted transactions appending the given one at the end."
  [transaction]
  (conj (transaction-db/transactions) transaction))

(defn similar-transactions
  [transaction]
  (filterv #(and (= (transaction-utils/transaction-amount %)
                    (transaction-utils/transaction-amount transaction))
                 (= (transaction-utils/transaction-merchant %)
                    (transaction-utils/transaction-merchant transaction))) (all-transactions transaction)))

(defn validate
  "Checks whether there is a doubled-transaction on a small amount of time.
  If there is more than 2 similar transactions on a 2 minute interval, return 'doubled-transaction'.
  It was assumed that the transactions will be received ordered by time, so at this point it will not
  be considered that there could be a transaction with time greater than the one received as parameter."
  [transaction]
  (if (>= 1 (count (time-utils/times-higher-or-equal-to
                     (transaction-utils/transactions-time (similar-transactions transaction))
                     (transaction-utils/transaction-time-minus-2 transaction))))
    nil
    transaction-utils/doubled-transaction))

