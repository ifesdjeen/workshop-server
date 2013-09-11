(ns workshop-server.widgets.shared
  (:require [clojurewerkz.gizmo.widget :refer [defwidget]]
            [workshop-server.snippets.shared :as shared]))

(defwidget header
  :view shared/header-snippet
  :fetch (fn [_]))
