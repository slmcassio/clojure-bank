(ns clojure-bank.db.account)

(def db (atom {}))

(defn add-account!
  "Persist an account.
  Supports only one account at a time.
  Any persisted account will be replaced."
  [account]
  (swap! db (fn [old-account] account)))

(defn account
  []
  @db)

(defn clear-db
  []
  (swap! db (fn [old-account] {})))
