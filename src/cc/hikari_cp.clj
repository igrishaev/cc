(ns cc.hikari-cp
  (:import
   java.io.Writer)
  (:require
   [cc.core :as cc]
   [next.jdbc :as jdbc]
   [hikari-cp.core :as hikari-cp]
   [com.stuartsierra.component :as component]))


(def SPEC
  [:map
   [:read-only {:optional true} :boolean]])


(defprotocol IComponent

  (execute!
    [this sql-params]
    [this sql-params opts])

  (execute-one!
    [this sql-params]
    [this sql-params opts]))


(defrecord HickariPool [;; init
                        config
                        ;; runtime
                        pool]

  cc/IComponent

  (spec [_]
    SPEC)

  IComponent

  (execute! [_ sql-params]
    (jdbc/execute! pool sql-params))

  (execute! [_ sql-params opts]
    (jdbc/execute! pool sql-params opts))

  (execute-one! [_ sql-params]
    (jdbc/execute-one! pool sql-params))

  (execute-one! [this sql-params opts]
    (jdbc/execute-one! pool sql-params opts))

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


(defmacro with-transaction
  [[bind component opt] & body]
  (let [tx (gensym "tx")]
    `(jdbc/with-connection [~tx (:pool ~component) ~opt]
       (let [~bind (assoc ~component :pool ~tx)]
         ~@body))))
