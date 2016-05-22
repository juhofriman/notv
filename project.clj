(defproject notv "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-time "0.11.0"]
                 [compojure "1.5.0"]
                 [http-kit "2.1.18"]]
  :main ^:skip-aot notv-backend.core
  :target-path "target/%s"
  :repl-options {:init-ns user}
  :profiles { :dev { :source-paths ["dev"]
                     :dependencies [[javax.servlet/servlet-api "2.5"]
                                    [org.clojure/tools.namespace "0.2.11"]]
                     :plugins []}
              :uberjar { :aot :all :main  notv-backend.core}})
