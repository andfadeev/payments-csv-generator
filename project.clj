(defproject payments-csv-generator "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.csv "0.1.3"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [org.postgresql/postgresql "9.2-1004-jdbc4"]
                 [honeysql "0.6.3"]
                 [clj-time "0.12.0"]]
  :main payments-csv-generator.core
  :aot [payments-csv-generator.core])



