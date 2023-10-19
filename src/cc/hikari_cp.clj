(ns cc.hikari-cp
  (:import
   java.io.Writer)
  (:require
   [hikari-cp.core :as hikari-cp]
   [com.stuartsierra.component :as component]))


(defrecord HickariPool [ ;; init
                        config
                        ;; runtime
                        pool]

  Object

  (toString [_]
    (format "< Hickari CP pool >"))

  clojure.lang.IDeref

  (deref [_]
    pool)

  component/Lifecycle

  (start [this]
    (let [pool (hikari-cp/make-datasource config)]
      (assoc this :pool pool)))

  (stop [this]
    (when pool
      (hikari-cp/close-datasource pool))
    (assoc this :pool nil)))


(defmethod print-method HickariPool
  [^HickariPool obj ^Writer writer]
  (.write writer (.toString obj)))


(defn component [options]
  (map->HickariPool {:config options}))
