(ns workshop-server.handlers.home
  (:require [workshop-server.entities :as e]
            [workshop-server.submitter :as s]))

(defn index
  [request]
  {:render :html
   :widgets {:main-content 'workshop-server.widgets.home/index-content}})

(defn show-user
  [request]
  {:render :html
   :token  (get-in request [:route-params :token])
   :widgets {:main-content 'workshop-server.widgets.home/task-report}})

(defn json-error
  [& reasons]
  {:render :json :status 400
   :response-hash {:success false :errors reasons}})

(defn json-success
  [hash]
  {:render  :json
   :status  200
   :response-hash hash})

(defn register
  [{:keys [form-params]}]
  (let [name (get form-params "name")]
    (cond
     (empty? name)        (json-error "Name should not be empty.")
     (e/name-taken? name) (json-error "Name is already taken.")
     :else                (json-success {:success true :token (e/register name)}))))

(defn submit-task
  [{:keys [form-params route-params] :as all}]
  (let [code       (get form-params "code")
        task       (get route-params :task)
        token      (get route-params :token)
        test-suite (get (s/all-prepared-tests) (keyword task))]
    (cond
     (empty? code)                 (json-error "Please provide us with some code.")
     (empty? token)                (json-error "Submission token can't be empty")
     (not (e/token-exists? token)) (json-error "We don't know that token")
     (empty? task)                 (json-error "Task name can't be empty.")
     (nil? test-suite)             (json-error (str "Can't find test suite for " task))
     :else                         (try
                                     (let [results (s/validate-code code test-suite)]
                                       (if (and (= 0 (:error results))
                                                (= 0 (:fail results)))
                                         (do
                                           (e/successfull-attempt token task results)
                                           (json-success {:success true :results results}))
                                         (do
                                           (e/failed-attempt token task results)
                                           (json-success {:success false :results results}))))
                                     (catch Exception e
                                       (let [message (.getMessage e)]
                                         (e/failed-attempt token task message)
                                         (json-error "Something bad has happened: " message)))))))
