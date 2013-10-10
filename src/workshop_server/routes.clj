(ns workshop-server.routes
    (:use [clojurewerkz.route-one.compojure])
    (:require [compojure.core :as compojure]
              [compojure.route :as route]))

(compojure/defroutes main-routes
  (GET root "/" request (workshop-server.handlers.home/index request))

  (POST reports "/reports" request (workshop-server.handlers.home/submit-report request))
  (GET favicon "/favicon.ico" _ (fn [_] {:render :nothing}))
  (route/not-found "Page not found"))
