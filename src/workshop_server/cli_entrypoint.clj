(ns workshop-server.cli-entrypoint
  (:gen-class)
  (:require [clojure.tools.cli :refer [cli]]
            [compojure.handler :refer [api]]
            [clojurewerkz.gizmo
             [core :refer [require-widgets require-snippets require-handlers
                           require-services register-snippet-reload]]
             [config :refer [load-config!]]
             [service :refer [start-all! all-services]]]))

(alter-var-root #'*out* (constantly *out*))

(defn -main
  [& args]
  (let [[options positional-args banner] (cli args
                                              ["--config" "Path to configuration file to use"])]
    (load-config! (:config options)))
  (require-snippets "workshop-server")
  (require-handlers "workshop-server")
  (require-widgets "workshop-server")
  (require-services "workshop-server")
  (register-snippet-reload "workshop-server")

  (start-all!))
