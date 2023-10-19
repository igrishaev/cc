(ns cc.cache
  (:require
   [com.stuartsierra.component :as component]))


(defrecord Cache [cache]

  clojure.lang.IDeref

  (deref [_]
    cache)

  component/Lifecycle

  (start [this]
    (if cache
      this
      (let [cache
            {:foo 123}]
        (assoc this :cache cache))))

  (stop [this]
    (assoc this :cache nil)))
