(ns workshop-server.widgets.home
    (:require [clojurewerkz.gizmo.widget :refer [defwidget]]
              [workshop-server.snippets.home :as snippets]))

(defwidget index-content
  :view snippets/index-snippet
  :fetch (fn [_]))
