(defproject workshop-server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clojurewerkz/gizmo "1.0.0-alpha2-SNAPSHOT"]
                 [clojail "1.0.6"]
                 [org.clojure/tools.nrepl "0.2.3"]
                 [com.novemberain/monger "1.6.0"]
                 [org.clojure/tools.cli "0.2.2"]]
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.4"]]}}
  :source-paths ["src"]
  :resource-paths ["resources"]
  :main workshop-server.cli-entrypoint)
