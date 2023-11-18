(ns cc.core
  (:require
   [com.stuartsierra.component :as component]))


(defmacro with-start [[bind component] & body]
  `(let [~bind (component/start ~component)]
     (try
       ~@body
       (finally
         (component/stop ~bind)))))


#_
{:pool cc.hikari-cp/component}


(defprotocol IComponent
  (spec [this]))


(extend-protocol IComponent
  Object
  (spec [_]))


(defn sys-init
  ([config-map])
  ([config-map using-map]))
