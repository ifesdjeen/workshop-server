(ns workshop-server.snippets.home
    (:require [net.cgrand.enlive-html :as html]
              [clojurewerkz.gizmo.enlive :refer [defsnippet within replace-vars]]))

(defsnippet index-snippet "templates/home/index.html"
  [*index-content]
  [{:keys [people-online-count test-runs successfull-tests functions-in-progress]}]
  [*people-online html/any-node]           (replace-vars {:people-online-count people-online-count})
  [*test-runs html/any-node]               (replace-vars {:test-runs test-runs})
  [*successfull-tests html/any-node]       (replace-vars {:successfull-tests successfull-tests})
  [*functions-in-progress html/any-node]   (replace-vars {:functions-in-progress functions-in-progress}))
