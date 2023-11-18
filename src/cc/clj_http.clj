(ns cc.clj-http
  (:import
   java.io.Writer)
  (:refer-clojure :exclude [get])
  (:require
   [cc.core :as cc]
   [clj-http.client :as client]
   [clj-http.conn-mgr :as conn-mgr]
   [com.stuartsierra.component :as component]))


(def SPEC
  [:map
   [:throw-entire-message? {:optional true} :boolean]
   [:throw-exceptions {:optional true} :boolean]
   [:as {:optional true} :keyword]
   [:accept {:optional true} :keyword]
   [:cache {:optional true} :boolean]
   [:connection-manager {:optional true}
    [:map
     [:timeout {:optional true} :int]
     [:threads {:optional true} :int]]]])


(defprotocol IComponent

  (request
    [this method url]
    [this method url opt])

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
    [this url opt]))


(defrecord CljHttpClient [;; init
                          config
                          ;; runtime
                          params]

  cc/IComponent

  (spec [this]
    SPEC)

  Object

  (toString [_]
    (format "< Clj-HTTP client >"))

  component/Lifecycle

  (start [this]
    (if (:connection-manager params)
      this
      (let [params
            (update config
                    :connection-manager
                    conn-mgr/make-reusable-conn-manager)]
        (assoc this :params params))))

  (stop [this]
    (some-> params :connection-manager conn-mgr/shutdown-manager)
    (assoc this :params nil))

  IComponent

  (request [this method url]
    (request this method url nil))

  (request [this method url opt]
    (client/request (-> params
                        (merge opt)
                        (assoc :url url
                               :method method))))

  (GET [this url]
    (request this :get url))

  (GET [this url opt]
    (request this :get url opt))

  (HEAD [this url]
    (request this :head url))

  (HEAD [this url opt]
    (request this :head url opt))

  (POST [this url]
    (request this :post url))

  (POST [this url opt]
    (request this :post url opt))

  (PUT [this url]
    (request this :put url))

  (PUT [this url opt]
    (request this :put url opt))

  (PATCH [this url]
    (request this :patch url))

  (PATCH [this url opt]
    (request this :patch url opt))

  (DELETE [this url]
    (request this :delete url))

  (DELETE [this url opt]
    (request this :delete url opt))

  (OPTIONS [this url]
    (request this :options url))

  (OPTIONS [this url opt]
    (request this :options url opt)))


(defmethod print-method CljHttpClient
  [^CljHttpClient obj ^Writer writer]
  (.write writer (.toString obj)))


(defn component
  ([]
   (component nil))
  ([config]
   (map->CljHttpClient {:config config})))


(comment

  (def c1 (component))

  (def c2 (component/start c1))

  (:status (GET c2 "https://grishaev.me"))

  (def c3 (component/stop c2))

  )
