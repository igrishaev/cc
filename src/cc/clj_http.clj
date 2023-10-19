(ns cc.clj-http
  (:import
   java.io.Writer)
  (:refer-clojure :exclude [get])
  (:require
   [clj-http.client :as client]
   [clj-http.conn-mgr :as conn-mgr]
   [com.stuartsierra.component :as component]))


(defprotocol IClient

  (GET
    [this url]
    [this url opt])

  (HEAD
    [this url]
    [this url opt])

  (POST
    [this url]
    [this url opt])

  (PUT
    [this url]
    [this url opt])

  (PATCH
    [this url]
    [this url opt])

  (DELETE
    [this url]
    [this url opt])

  (OPTIONS
    [this url]
    [this url opt])

  (request
    [this method url]
    [this method url opt]))


(defrecord CljHttpClient [;; init
                          defaults
                          ;; runtime
                          params
                          cm]

  Object

  (toString [_]
    (format "< Clj-HTTP client >"))

  component/Lifecycle

  (start [this]
    (if cm
      this
      (let [cm
            (conn-mgr/make-reusable-conn-manager {})

            params
            (assoc defaults :connection-manager cm)]

        (assoc this :params params))))

  (stop [this]
    (when cm
      (conn-mgr/shutdown-manager cm))
    (assoc this :cm nil :params nil))

  IClient

  (POST [this url]
    (client/post url params))

  (POST [this url opt]
    (client/post url (merge params opt)))

  (PUT [this url]
    (client/put url params))

  (PUT [this url opt]
    (client/put url (merge params opt))))


(defmethod print-method CljHttpClient
  [^CljHttpClient obj ^Writer writer]
  (.write writer (.toString obj)))


(defn component [defaults]
  (map->CljHttpClient {:defaults defaults}))
