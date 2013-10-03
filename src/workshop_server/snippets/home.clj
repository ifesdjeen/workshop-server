(ns workshop-server.snippets.home
    (:require [net.cgrand.enlive-html :as html]
              [clojurewerkz.gizmo.enlive :refer [defsnippet within]]))

(defsnippet index-snippet "templates/home/index.html"
  [*index-content]
  [{:keys [people-online task-completition-reports]}]
  [*people-online html/any-node] (html/replace-vars {:people-online-count (-> people-online count str)})
  (within *task-completion-table [*task-completion-row])
  (html/clone-for [report task-completition-reports]
                  [html/any-node] (html/replace-vars report)))

(defsnippet task-report "templates/tasks/report.html"
  [*content]
  [tasks]
  (within *task-report-list [*task-report-list-item])
  (html/clone-for [task tasks]
                  [*status] (if (:status task)
                              (html/do->
                               (html/add-class "btn-success")
                               (html/content "Success"))
                              (html/do->
                               (html/add-class "btn-danger")
                               (html/content "Failure")))
                  [html/any-node] (html/replace-vars task)))
