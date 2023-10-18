(ns cc.compojure)



(defmacro component
  {:style/indent 1}
  [slots & compojure]

  `(defrecord ~'CompojureRouter [~@slots ~'routes]

     clojure.lang.IFn

     (invoke [_ request#]
       (~'routes request#))

     sfd.Lifecycle

     (start [this#]
       (let [routes# (routes ~@compojure)]
         (assoc this# :routes routes#)))

     (stop [this#]
       ...)))

#_
(component [handler-this
            handler-that
            handler-foo
            not-found]

  (GET "/sdfsf" request (handler-this request))
  (POST "/sdfsf" request (handler-this request))
  (GET "/sdfsf" request (handler-foo request))
  (not-found request))
