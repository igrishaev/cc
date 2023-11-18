(ns cc.jetty
  (:import
   java.io.Writer
   org.eclipse.jetty.server.Server)
  (:require
   [ring.adapter.jetty :as jetty]
   [com.stuartsierra.component :as component]))


(def SPEC
  [:map
   [:port {:optional true} :int]
   [:host {:optional true} :string]
   [:join? {:optional true} :boolean]
   [:daemon? {:optional true} :boolean]
   [:http? {:optional true} :boolean]
   [:ssl? {:optional true} :boolean]
   [:ssl-port {:optional true} :int]
   [:max-threads {:optional true} :int]
   [:min-threads {:optional true} :int]
   [:max-queued-requests {:optional true} :int]
   [:thread-idle-timeout {:optional true} :int]
   [:max-idle-time {:optional true} :int]
   [:client-auth {:optional true} [:enum :need :want :none]]
   [:send-date-header? {:optional true} :boolean]
   [:async? {:optional true} :boolean]
   [:async-timeout {:optional true} :int]
   [:output-buffer-size {:optional true} :int]
   [:request-header-size {:optional true} :int]
   [:response-header-size {:optional true} :int]
   [:send-server-version? {:optional true} :boolean]])


(def OVERRIDES
  {:join? false})


(defrecord Component
    [;; init
     config

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
          config]
      (format "< Jetty server, port: %s >" port)))

  component/Lifecycle

  (start [this]
    (if server
      this
      (let [params
            (merge config OVERRIDES)

            server
            (jetty/run-jetty handler params)]

        (assoc this :server server))))

  (stop [this]
    (if server
      (do
        (.stop server)
        (assoc this :server nil))
      this)))


(defmethod print-method Component
  [obj ^Writer writer]
  (.write writer (str obj)))


(defn component
  ([]
   (component nil))

  ([config]
   (map->Component {:config config})))
