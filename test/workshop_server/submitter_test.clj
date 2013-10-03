(ns workshop-server.submitter-test
  (:use clojure.test)
  (:require [workshop-server.submitter :refer [sandbox-file prepared-test validate-tests validate-code]]
            clojure.test.tap
            [clojail.core :refer [sandbox]]))

(prepared-test read-file-prepared-success
               (deftest success-test
                 (is (= 6 (my-reduce + 0 [1 2 3])))
                 (is (= 7 (my-reduce + 1 [1 2 3])))))

(prepared-test read-file-prepared-fail
               (deftest fail-test
                 (is (= 6 (my-reduce + 0 [1 2 3])))
                 (is (= 8 (my-reduce + 1 [1 2 3])))))

(deftest read-file-test
  (let [res (validate-tests "resources/test/test_namespace.clj" read-file-prepared-success)]
    (is (= 0 (:error res)))
    (is (= 0 (:fail res)))
    (is (= 2 (:pass res))))

  (let [res (validate-code (slurp "resources/test/test_namespace.clj") read-file-prepared-success)]
    (is (= 0 (:error res)))
    (is (= 0 (:fail res)))
    (is (= 2 (:pass res))))

  (let [res (validate-tests "resources/test/test_namespace.clj" read-file-prepared-fail)]
    (is (= 0 (:error res)))
    (is (= 1 (:fail res)))
    (is (= 1 (:pass res)))))
