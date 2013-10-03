(ns workshop-server.entities
  (:import java.util.UUID)
  (:require [clojure.string :refer [join]]
            [workshop-server.routes :as routes]))

(def defaults {:tokens {} :task-completition {} :submission-attempts {}})

(defonce entities (atom defaults))

(defn reset-entities!
  []
  (reset! entities defaults))

(defn register
  [name]
  (let [token (.toString (java.util.UUID/randomUUID))]
    (swap! entities assoc-in [:tokens token] name)
    token))

(def all-entities #(deref entities))
(def tokens #(:tokens (all-entities)))

(defn name-taken?
  [name]
  (some #(= name %) (vals (tokens))))

(defn token-exists?
  [token]
  ((-> (tokens) keys set) token))

(defn submission-attempt
  [token task success results]
  (swap! entities
         (fn [m] (update-in m [:submission-attempts token] (fn [tasks]
                                                            (let [when (java.util.Date.)
                                                                  m    {:token   token
                                                                        :task    task
                                                                        :time    when
                                                                        :success success
                                                                        :results results}]
                                                              (if (empty? tasks)
                                                                (set [m])
                                                                (conj tasks m))))))))

(defn successfull-attempt
  [token task results]
  (submission-attempt token task true results))

(defn failed-attempt
  [token task results]
  (submission-attempt token task false results))

(defn find-name-for-token
  [token]
  (get (tokens) token))

(defn registered-people
  []
  (vals (tokens)))

(defn task-completion-report
  []
  (map (fn [[token attempts]]
         {:username        (find-name-for-token token)
          :user-link       (routes/show-user-path :token token)
          :attempts-count  (str (count attempts))
          :completed-tasks (join ", " (distinct (map :task (filter :success attempts))))})
       (:submission-attempts @entities)))

(defn user-task-report
  [token]
  (map
   (fn [item]
     {:task (:task item)
      :status (:success item)
      :report (get-in item [:results :report])})
   (get (:submission-attempts @entities) token)))
