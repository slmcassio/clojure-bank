(ns clojure-bank.utils.time
  (:require [clj-time.format :as f]
            [clj-time.core :as t]))

(def date-time-formatter (f/formatters :date-time))

(defn string->time
  [string]
  (try
    (f/parse date-time-formatter string)
    (catch Exception e "")))

(defn time->string
  [time]
  (f/unparse date-time-formatter time))

(defn subtract-minutes
  [time minutes]
  (t/minus time (t/minutes minutes)))

(defn times-higher-or-equal-to
  [times reference]
  (filter #(or (t/after? % reference)
                (t/equal? % reference)) times))