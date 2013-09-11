(ns workshop-server.submitter-test
  (:use clojure.test)
  (:require [workshop-server.submitter :refer [sandbox-file prepared-test]]))

(prepared-test read-file-prepared-success
                 (is (= 6 (my-reduce + 0 [1 2 3])))
                 (is (= 7 (my-reduce + 1 [1 2 3]))))

(prepared-test read-file-prepared-fail
                 (is (= 6 (my-reduce + 0 [1 2 3])))
                 (is (= 8 (my-reduce + 1 [1 2 3]))))

(deftest read-file-test
  (let [file     "resources/test/test_namespace.clj"
        sb        (sandbox-file file)]

    (is (sb read-file-prepared-success))
    (is (not (sb read-file-prepared-fail)))
    (is (= java.util.Date (type (sb '(new-date)))))))
