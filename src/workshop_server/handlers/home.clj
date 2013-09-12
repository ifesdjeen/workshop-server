(ns workshop-server.handlers.home
  (:require [workshop-server.entities :as e]
            [workshop-server.submitter :as s]))

(defn index
  [request]
  {:render :html
   :widgets {:main-content 'workshop-server.widgets.home/index-content}})

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
  [{:keys [form-params]}]
  (let [code       (get form-params "code")
        task       (get form-params "task")
        test-suite (get (s/all-prepared-tests) (keyword task))]
    (cond
     (empty? code)     (json-error "Please provide us with some code.")
     (empty? task)     (json-error "Task name can't be empty.")
     (nil? test-suite) (json-error (str "Can't find test suite for " task))
     :else             (try
                         (json-success {:success true :results (s/validate-code code test-suite)})
                         (catch Exception e
                           (json-error "Something bad has happened: " (.getMessage e)))))))
