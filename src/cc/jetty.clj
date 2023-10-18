(ns cc.jetty
  (:import
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


(defn component
  ([options]
   (component options nil))

  ([options using]
   (cond-> (map->JettyServer {:options options})
     using
     (component/using using))))
