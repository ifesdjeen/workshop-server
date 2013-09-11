(ns workshop-server.handlers.home)

(defn index
  [request]
  {:render :html
   :widgets {:main-content 'workshop-server.widgets.home/index-content}})
