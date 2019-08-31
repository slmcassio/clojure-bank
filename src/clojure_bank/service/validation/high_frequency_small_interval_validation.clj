(ns clojure-bank.service.validation.high-frequency-small-interval-validation
  (:require [clojure-bank.db.transaction :as transaction-db]
            [clojure-bank.utils.transaction :as transaction-utils]
            [clojure-bank.utils.time :as time-utils]))

(defn all-transactions
  "Retrieve all persisted transactions appending the given one at the end."
  [transaction]
  (conj (transaction-db/transactions) transaction))

(defn validate
  "Checks whether there is a high frequency of transactions on a small amount of time.
  If there is more than 3 transactions on a 2 minute interval, return 'high-frequency-small-interval'.
  It was assumed that the transactions will be received ordered by time, so at this point it will not
  be considered that there could be a transaction with time greater than the one received as parameter."
  [transaction]
  (if (>= 2 (count (time-utils/times-higher-or-equal-to
                     (transaction-utils/transactions-time (all-transactions transaction))
                     (transaction-utils/transaction-time-minus-2 transaction))))
    nil
    transaction-utils/high-frequency-small-interval))