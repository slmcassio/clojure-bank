(ns clojure-bank.core
  (:require [clojure-bank.port.cli :as authorisation-cli])
  (:gen-class))

(defn -main
  [& args]
  (authorisation-cli/prompt-operations!))