(defproject cc "0.1.0-SNAPSHOT"

  :description "FIXME: write description"

  :url "http://example.com/FIXME"

  :license
  {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
   :url "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies
  [[com.stuartsierra/component "1.1.0"]]

  :profiles
  {:dev
   {:dependencies

    [[org.clojure/clojure "1.11.1"]

     #_[metosin/malli "0.13.0"]

     [org.clojure/java.jdbc "0.7.11"]
     [com.github.seancorfield/next.jdbc "1.3.894"]
     [org.postgresql/postgresql "42.2.16"]
     [hikari-cp "2.13.0"]

     [ring/ring-jetty-adapter "1.7.1"]

     [im.chit/cronj "1.4.4"]

     [org.clojure/core.cache "1.0.225"]

     ]}})
