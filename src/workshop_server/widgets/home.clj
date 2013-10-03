(ns workshop-server.widgets.home
    (:require [clojurewerkz.gizmo.widget :refer [defwidget]]
              [workshop-server.snippets.home :as snippets]
              [workshop-server.entities :as e]))

(defwidget index-content
  :view snippets/index-snippet
  :fetch (fn [_] {:people-online (e/registered-people)
                 :task-completition-reports (e/task-completion-report)}))


(defwidget task-report
  :view snippets/task-report
  :fetch (fn [{:keys [token]}]
           (e/user-task-report token)))
