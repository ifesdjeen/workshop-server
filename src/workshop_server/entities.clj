(ns workshop-server.entities
  (:import java.util.UUID))

(def defaults {:tokens {}})

(defonce entities (atom defaults))

(defn reset-entities!
  []
  (reset! entities defaults))

(defn register
  [name]
  (let [token (.toString (java.util.UUID/randomUUID))]
    (swap! entities assoc-in [:tokens token] name)
    token))

(def all-entities #(deref entities))
(def tokens #(:tokens (all-entities)))

(defn name-taken?
  [name]
  (some #(= name %) (vals (tokens))))
