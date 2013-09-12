(ns workshop-server.submitter.test-helpers
  (:require [clojure.test :refer [*test-out* run-tests]]))

(defn run-tests*
  []
  (let [s (new java.io.StringWriter)]
    (binding [*test-out* s]
      (let [results (run-tests)]
        (assoc results :report (str s))))))
