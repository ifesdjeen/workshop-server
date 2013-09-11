(ns workshop-server.services.jetty
  ^{:doc "Jetty service"}
  (:use [clojurewerkz.gizmo.service])
  (:require [ring.adapter.jetty :as jetty]
            [clojurewerkz.gizmo.config :as config]
            [workshop-server.core :as app-core]))

(defservice jetty-service
  :config #(:jetty config/settings)
  :alive (fn [service]
           (and service
               (state service)
               (.isRunning (state service))))
  :stop (fn [service]
          (.stop (state service)))
  :start (fn [service]
           (reset-state service
                        (jetty/run-jetty #'app-core/app (config service)))))
