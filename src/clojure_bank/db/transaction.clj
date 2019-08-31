(ns clojure-bank.db.transaction)

(def db (atom []))

(defn add-transaction!
  "Persist a transaction."
  [transaction]
  (swap! db (fn [old-transactions] (conj old-transactions transaction)))
  (last @db))

(defn transactions
  []
  @db)

(defn clear-db
  []
  (swap! db (fn [old-transactions] [])))
