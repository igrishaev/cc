(ns cc.jetty
  (:import
   java.io.Writer
   org.eclipse.jetty.server.Server)
  (:require
   [ring.adapter.jetty :as jetty]
   [com.stuartsierra.component :as component]))


(defrecord JettyServer [;; init
                        options
                        ;; deps
                        handler
                        ;; runtime
                        ^Server server]

  clojure.lang.IDeref

  (deref [_]
    server)

  Object

  (toString [_]
    (let [{:keys [port]}
          options]
      (format "< Jetty server on port %s >" port)))

  component/Lifecycle

  (start [this]
    (if server
      this
      (let [options+
            (assoc options
                   :join? false)
            server
            (jetty/run-jetty handler options+)]
        (assoc this :server server))))

  (stop [this]
    (if server
      (do
        (.stop server)
        (assoc this :server nil))
      this)))


(defmethod print-method JettyServer
  [obj ^Writer writer]
  (.write writer (str obj)))


(defn component
  ([options]
   (component options nil))

  ([options using]
   (cond-> (map->JettyServer {:options options})
     using
     (component/using using))))
