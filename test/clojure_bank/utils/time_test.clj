(ns clojure-bank.utils.time-test
  (:require [clojure.test :refer :all]
            [clj-time.core :as t]
            [clojure-bank.utils.time :refer :all]))

(def valid-time-string "2019-02-13T10:00:00.000Z")
(def valid-time (t/date-time 2019 02 13 10))
(def valid-time-minus-59-minutes (t/date-time 2019 02 13 9 1))
(def invalid-time-string "")
(def nil-time-string nil)

(def valid-times [(t/date-time 2019 02 12 10)
                  (t/date-time 2019 02 13 10)
                  (t/date-time 2019 02 14 10)
                  (t/date-time 2019 02 15 10)])

(def higher-times [(t/date-time 2019 02 13 10)
                   (t/date-time 2019 02 14 10)
                   (t/date-time 2019 02 15 10)])

(deftest test-string->time
  (testing "valid string conversion"
    (is (= valid-time (string->time valid-time-string))))
  (testing "invalid string conversion"
    (is (= "" (string->time invalid-time-string))))
  (testing "nil string conversion"
    (is (= "" (string->time nil-time-string)))))

(deftest test-time->string
  (testing "valid time conversion"
    (is (= valid-time-string (time->string valid-time)))))

(deftest test-subtract-minutes
  (testing "subtraction of minutes"
    (is (= valid-time-minus-59-minutes (subtract-minutes valid-time 59)))))

(deftest test-times-higher-than
  (testing "times higher than reference"
    (is (= higher-times (times-higher-or-equal-to valid-times valid-time)))))