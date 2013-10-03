(ns workshop-server.submitter
  (:import [java.io PushbackReader])
  (:require [clojail.core :refer [sandbox]]
            workshop-server.submitter.test-helpers
            clojure.test
            [clojail.testers :refer [secure-tester-without-def]]
            [clojure.java.io :as io]))

(defn read-contents [file]
  "Reads file contents into the liast"
  (with-open [r (PushbackReader. (io/reader file))]
    (loop [acc []]
      (let [current (try (read r) (catch Exception e ::done))]
        (if (not (= ::done current))
          (recur (if (= (first current) 'ns)
                   acc
                   (conj acc current)))
          acc)))))

(defn read-ns-form [file]
  "Reads NS form from file"
  (with-open [r (PushbackReader. (io/reader file))]
    (loop []
      (let [current (try (read r) (catch Exception e ::done))]
        (if (not (= ::done current))
          (if (= (first current) 'ns)
            current
            (recur)))))))

(defn extract-by-kw
  "Extracts parts of namespace definition by keyword"
  [kw]
  (fn [ns-form]
    (assert (sequential? ns-form) "Namespace form is incorrect")
    (loop [[current & rest] ns-form
           acc []]
      (let [acc (if (and (sequential? current)
                         (= kw (first current)))
                  (concat acc (next current))
                  acc)]
        (if (empty? rest)
          acc
          (recur rest acc))))))

(def extract-require (extract-by-kw :require))
(def extract-refer (extract-by-kw :refer))
(def extract-use (extract-by-kw :use))
(def extract-import (extract-by-kw :import))

(defn prepare-ns
  "Prepares parts of namespace so that internal namespace definition would get resolved"
  [requires uses refers]
  (doseq [f requires]
    (require f))
  (doseq [f uses]
    (use f))
  (doseq [f refers]
    (refer f)))


(defn prepare-form
  [sym coll]
  `(doseq [f# [~@(for [a coll] `(quote ~a))]]
     (~sym f#)))

(defn prepare-imports
  [coll]
  `(do
     ~@(for [a coll] (list 'import `(quote ~a)))))

(defmacro prepare-ns-wrapper
  "Prepares a wrapper for namespace initialiser"
  [requires uses refers imports]
  `(list
    `do
    (prepare-form 'require ~requires)
    (prepare-imports ~imports)
    (prepare-form 'use ~uses)
    (prepare-form 'use ['clojure.test])
    (prepare-form 'refer ~refers)))

(defn sandbox-file
  [file]
  (let [contents (read-contents file)
        ns-form  (read-ns-form file)
        requires (extract-require (read-ns-form file))
        refers   (extract-refer (read-ns-form file))
        uses     (extract-use (read-ns-form file))
        imports  (extract-import (read-ns-form file))
        _        (prepare-ns requires uses refers)
        ns-init  (prepare-ns-wrapper requires uses refers imports)
        sb       (sandbox [] :init ns-init :max-defs 1000)]

    (doseq [f contents]
      (sb f))


    sb))

(defmacro prepared-test
  [name & body]
  `(def ~(vary-meta name assoc :prepared-test true)
     (list 'do ~@(for [a body] `(quote ~a))
           '(use 'workshop-server.submitter.test-helpers)
           (list 'workshop-server.submitter.test-helpers/run-tests*))))

(defn prepared-tests-form-file
  [n]
  (let [contents (read-contents (format "src/workshop_server/prepared_tests/%s.clj" n))]
    `(list 'do
           '(use 'workshop-server.submitter.test-helpers)
           '(use 'clojure.test)
           ~@contents
           '(workshop-server.submitter.test-helpers/run-tests*))))

(defn validate-tests
  [file test-suite]
  (let [sb (sandbox-file file)]
    (sb test-suite)))

(defn validate-code
  [code test-suite]
  (let [file (java.io.File/createTempFile "test" ".clj")]
    (spit file code)
    (validate-tests file test-suite)))

(defn all-prepared-tests
  []
  (->> (all-ns)
       (map #(vals (ns-interns %)))
       flatten
       (filter #(:prepared-test (meta %)))
       (map #(vector (keyword (:name (meta %))) (var-get %)))
       (into {})))
