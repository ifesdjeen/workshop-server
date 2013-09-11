(defn my-reduce
  [f default coll]
  (if (empty? coll)
    default
    (reduce f (f default (first coll)) (next coll))))
