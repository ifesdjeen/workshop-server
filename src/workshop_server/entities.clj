(ns workshop-server.entities
  (:import java.util.UUID)
  (:require [clojure.string :refer [join]]
            [clj-time.core :as clj-time]
            [clj-time.coerce :as time-coerce]))

(defonce submission-attempts (atom []))

(defn add-submission-attempt
  [attempt]
  (spit "reports" attempt :append true)
  (swap! submission-attempts conj attempt))

(defn initialise-submission-attempts
  []
  (when (.exists (clojure.java.io/file "reports"))
    (with-open [r (java.io.PushbackReader.
                   (clojure.java.io/reader "reports"))]
      (binding [*read-eval* false]
        (loop [form (try (read r) (catch Exception e ::end))]
          (swap! submission-attempts conj form)
          (let [next (try (read r) (catch Exception e ::end))]
            (if-not (= ::end next)
              (recur next))))))))

(defn unique-tokens
  []
  (count (distinct (map :token @submission-attempts))))

(defn test-runs
  []
  (count @submission-attempts))

(defn successfull-tests
  []
  (try
    (->> submission-attempts
        deref
        (map (comp  #(Integer/parseInt %) :pass))
        (reduce +))
    (catch Exception e
      0)))

(defn functions-in-progress
  []
  (let [mins-ago (clj-time/ago (clj-time/minutes 5))]
    (->> submission-attempts
         deref
         (filter #(clj-time/after? (time-coerce/from-date (:created-at %)) mins-ago))
         (map :not-implemented-fn-names)
         flatten
         distinct
         (join ", "))))
