(ns payments-csv-generator.core
  (:gen-class)
  (:require [clj-time
             [core :as t]
             [format :as tf]]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [clojure.tools.cli :refer [parse-opts]]
            [payments-csv-generator
             [db :as db]
             [headers :as h]
             [vtb :as vtb]
             [alfa :as alfa]]))

(defn- create-file [filename content separator]
  (let [filename (str "csv/" filename "_" (tf/unparse (tf/formatter "dd_MM_yyyy") (t/now)) ".csv")]
    (with-open [out-file (io/writer filename :encoding "windows-1251")]
      (csv/write-csv out-file content :separator separator))))

(defmulti generate-csv-file (fn [bank bills] bank))

(defmethod generate-csv-file "alfa"
  [_ bills]
  (println "Start generating ALFA file")
  (create-file "alfa_payments"
               (concat [h/alfa-headers-en h/alfa-headers-ru]
                       (into [] (map-indexed alfa/generate-alfa-line bills)))
               \tab))

(defmethod generate-csv-file "vtb"
  [_ bills]
  (println "Start generating VTB file")
  (create-file "vtb_payments"
               (concat
                [h/vtb-headers]
                (into [] (map-indexed vtb/generate-vtb-line bills))
                [h/vtb-footer])
               \;))

(def cli-opts
  [["-b" "--bank BANK_TYPE" "alfa or vtb"
    :id :bank
    :default "alfa"
    :parse-fn (fn [arg] (str/lower-case (str/trim arg)))]
   ["-H" "--hostname DB_HOST" "Database hostname"
    :id :db-host
    :parse-fn (fn [arg] (str/lower-case (str/trim arg)))
    :default "localhost"]
   ["-P" "--port DB_PORT" "Database port"
    :id :db-port
    :parse-fn (fn [arg] (str/lower-case (str/trim arg)))
    :default "5432"]
   ["-d" "--database DB_NAME" "Database name"
    :id :db-name
    :parse-fn (fn [arg] (str/lower-case (str/trim arg)))
    :default "hh"]
   ["-u" "--username DB_USER" "Database user"
    :id :db-user
    :parse-fn (fn [arg] (str/lower-case (str/trim arg)))
    :default "hh"]
   ["-p" "--password DB_PASSWORD" "Database password"
    :id :db-password
    :parse-fn (fn [arg] (str/lower-case (str/trim arg)))
    :default "123"]
   ["-i" "--ids IDs" "Bill ids (optional)"
    :id :ids
    :parse-fn (fn [ids-str] (map #(bigint %) (clojure.string/split ids-str #",")))
    ]
   ["-h" "--help" "Help info"]])

(defn -main [& args]
  (let [cli (parse-opts args cli-opts)]
    (if (java.lang.Boolean/valueOf (get-in cli [:options :help]))
      ;; help
      (println (:summary cli))
      ;; else
      (let [opts (:options cli)
            bank (:bank opts)
            db-spec (db/db-spec opts)
            bills (if (seq (:ids opts))
                    (db/get-bills-by-ids db-spec (:ids opts))
                    (db/get-bills db-spec))]
        (println "Found" (count bills) "bills")
        (generate-csv-file bank bills)
        (println "Finished generating file")))))

