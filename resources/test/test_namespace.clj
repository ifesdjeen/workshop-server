(ns ns-def
  (:require clojure.set)
  (:require [clojure.pprint :refer [pprint]])
  (:import java.util.Date)
  (:use clojure.test))

(defn my-reduce
  [f default coll]
  (if (empty? coll)
    default
    (reduce f (f default (first coll)) (next coll))))

(defn my-map
  [f coll]
  (if (empty? coll)
    '()
    (cons (f (first coll)) (my-map f (next coll)))))

(defn new-date
  []
  (Date.))
