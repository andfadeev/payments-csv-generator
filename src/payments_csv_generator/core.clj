(ns payments-csv-generator.core
  (:require [clj-time
             [core :as t]
             [format :as tf]]
            [clojure.edn :as edn]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [payments-csv-generator
             [db :as db]
             [headers :as h]
             [vtb :as vtb]
             [alfa :as alfa]])
  (:gen-class))

(defn- create-file [filename content separator encoding]
  (let [filename (str "csv/" filename "_" (tf/unparse (tf/formatter "dd_MM_yyyy") (t/now)) ".csv")]
    (with-open [out-file (io/writer filename :encoding encoding)]
      (csv/write-csv out-file content :separator separator))))

(defmulti filename (fn [bank] bank))

(defmethod filename :alfa
  [bank]
  "alfa_payments")

(defmethod filename :vtb
  [bank]
  "vtb_payments")

(defmulti headers (fn [bank] bank))

(defmethod headers :alfa
  [bank]
  [h/alfa-headers-en h/alfa-headers-ru])

(defmethod headers :vtb
  [bank]
  [h/vtb-headers])

(defmulti footer (fn [bank] bank))

(defmethod footer :alfa
  [bank]
  nil)

(defmethod footer :vtb
  [bank]
  [h/vtb-footer])

(defmulti separator (fn [bank] bank))

(defmethod separator :alfa
  [bank]
  \tab)

(defmethod separator :vtb
  [bank]
  \;)

(defmulti generate-line (fn [bank idx bill statement] bank))

(defmethod generate-line :alfa
  [bank idx bill statement]
  (alfa/generate-alfa-line idx bill statement))

(defmethod generate-line :vtb
  [bank idx bill statement]
  (vtb/generate-vtb-line idx bill statement))

(defn group-bills [bills]
  [(reduce (fn [prev next]
             (-> prev
                 (update-in [:price] + (:price next))
                 (update-in [:uid] (fn [u1 u2] (str u1 ", " u2)) (:uid next))
                 (update-in [:bill_id] (fn [u1 u2] (str u1 ", " u2)) (:bill_id next)))) bills)])

(defn -main [& args]
  (let [config (edn/read-string (slurp "resources/config.edn"))
        db (db/db-spec (:db config))
        bank (:bank config)
        statements (:statements config)
        rows (mapcat (fn [statement]
                    (let [bill-ids (:bills statement)
                          bills (db/get-bills-by-ids db bill-ids)
                          bills (if (get statement :multiple? false) (group-bills bills) bills)]
                      (map-indexed (fn [idx bill] (generate-line bank idx bill statement)) bills))) statements)]
    (create-file (filename bank)
                 (concat (headers bank) rows (footer bank))
                 (separator bank)
                 (:encoding config))))
