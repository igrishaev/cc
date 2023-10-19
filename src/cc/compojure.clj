(ns cc.compojure)



(defmacro component
  {:style/indent 2}
  [ClassName slots & compojure]

  `(defrecord ~ClassName [~@slots ~'-routes]

     clojure.lang.IFn

     (invoke [_ request#]
       (~'-routes request#))

     sfd.Lifecycle

     (start [this#]
       (let [routes# (routes ~@compojure)]
         (assoc this# :-routes routes#)))))

#_
(component CompojureHandler [handler-this
                             handler-that
                             handler-foo
                             not-found]

  (GET "/sdfsf" request (handler-this request))
  (POST "/sdfsf" request (handler-this request))
  (GET "/sdfsf" request (handler-foo request))
  (not-found request))
