(ns workshop-server.routes
    (:use [clojurewerkz.route-one.compojure])
    (:require [compojure.core :as compojure]
              [compojure.route :as route]))

(compojure/defroutes main-routes
  (GET root "/" request (workshop-server.handlers.home/index request))

  (POST register "/register" request (workshop-server.handlers.home/register request))
  (POST create-user "/users" request (workshop-server.handlers.home/register request))
  (GET  show-user "/users/:token" request (workshop-server.handlers.home/show-user request))
  (POST submit-task "/users/:token/tasks/:task" request (workshop-server.handlers.home/submit-task request))


  (GET favicon "/favicon.ico" _ (fn [_] {:render :nothing}))
  (route/not-found "Page not found"))
