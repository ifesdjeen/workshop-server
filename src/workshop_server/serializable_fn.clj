(ns workshop-server.serializable-fn
  "Serializable functions! Check it out."
  (:refer-clojure :exclude [fn])
  (:import java.io.Writer))

(defn- save-env [locals form]
  (let [form (rest (form))
        quoted-form `(quote ~form)]
    (if locals
      `(list `let [~@(for [local locals,
                           let-arg [`(quote ~local)
                                    `(list `quote ~local)]]
                       let-arg)]
             ~quoted-form)
      quoted-form)))

(defmacro fn [& sigs]
  `(do
    ~(save-env (keys &env) &form)))

(defmethod print-method ::serializable-fn [o ^Writer w]
  (print-method (::source (meta o)) w))
