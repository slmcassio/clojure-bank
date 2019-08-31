(ns clojure-bank.port.cli
  (:require [cheshire.core :refer :all]
            [clojure-bank.adapter.operation :as operation-adapter]))

(defn get-input-seq!
  "Returns the input lines from the standard in as a lazy sequence of strings."
  []
  (line-seq (java.io.BufferedReader. *in*)))

(defn parse-input
  "Parse the input json into a map.
  If parse fails, return an empty string."
  [input]
  (try
    (parse-string input)
    (catch Exception e "")))

(defn process-output!
  "Converts a map into a json.
  Print it using println fn."
  [output]
  (println (if (empty? output)
             output
             (generate-string output))))

(def process-input! (comp process-output!
                          operation-adapter/ingest!
                          parse-input))

(defn prompt-operations!
  "Process each line from the standard input."
  []
  (doseq [input (get-input-seq!)]
    (process-input! input)))