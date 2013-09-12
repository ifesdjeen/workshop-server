(ns workshop-server.test-suites
  (:require [workshop-server.submitter :refer [prepared-test]]))

(prepared-test getting-started-test
               (deftest my-reduce-test
                 (is (= 6 (my-reduce + 0 [1 2 3])))))
