(ns workshop-server.widgets.home
    (:require [clojurewerkz.gizmo.widget :refer [defwidget]]
              [workshop-server.snippets.home :as snippets]
              [workshop-server.entities :as e]))

(defwidget index-content
  :view snippets/index-snippet
  :fetch (fn [_] {:people-online-count (e/unique-tokens)
                 :test-runs (e/test-runs)
                 :successfull-tests (e/successfull-tests)
                 :functions-in-progress (e/functions-in-progress)}))
