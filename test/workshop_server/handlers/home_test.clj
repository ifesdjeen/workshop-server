(ns workshop-server.handlers.home-test
  (:use clojure.test
        workshop-server.test-suites
        workshop-server.handlers.home)
  (:require [workshop-server.entities :refer [reset-entities! tokens]]))

(deftest register-test
  (testing "When name is given correctly"
    (let [res (register {:form-params {"name" "some name"}})]
      (is (= 200 (:status res)))
      (is (get-in res [:response-hash :token])))
    (reset-entities!))
  (testing "When name is empty"
    (is (= 400 (:status (register {:form-params {"name" "" }}))))
    (reset-entities!))
  (testing "When name is already taken"
    (:status (register {:form-params {"name" "taken" }}))
    (is (= 400 (:status (register {:form-params {"name" "taken" }}))))
    (reset-entities!)))

(deftest submit-task-test
  (testing "Code is empty"
    (let [res (submit-task {:form-params {"code" ""}})]
      (is (= 400 (:status res))))
    (reset-entities!))

  (testing "Task is unrecognised"
    (let [res (submit-task {:form-params {"code" "(ns asd)"}
                            :route-params {:task "some-task"}})]
      (is (= 400 (:status res))))
    (reset-entities!))

  (testing "Code is nice and clean"
    (let [res (submit-task {:form-params {"code" (slurp "resources/test/test_namespace.clj")}
                            :route-params {:task "getting-started-test"}})]
      (is (= 200 (:status res)))
      (is (= 1 (get-in res [:response-hash :results :pass]))))
    (reset-entities!))

  (testing "Code is bad"
    (let [res (submit-task {:form-params {"code" "olololo"
                                          "task" "getting-started-test"}})]
      (is (= 400 (:status res))))
    (reset-entities!)))
