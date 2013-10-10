(ns workshop-server.handlers.home
  (:require [clojure.walk :refer [keywordize-keys]]
            [cheshire.core :as json]
            [workshop-server.entities :as e]
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

(defn submit-report
  [{:keys [form-params] :as all}]
  (let [report (keywordize-keys form-params)]
    (cond
     (empty? (:token report)) (json-error "Submission token can't be empty")
     :else                    (try
                                (e/add-submission-attempt (assoc report :created-at (java.util.Date.)))
                                (json-success {:success true})))))
