(ns payments-csv-generator.db
  (:require [honeysql.core :as sql]
            [honeysql.helpers :as h]
            [clojure.java.jdbc :as j]
            [honeysql.format :as f]))

(defn db-spec [{:keys [:db-url :db-user :db-password]}]
  {:classname "org.postgresql.Driver"
   :subprotocol "postgresql"
   :subname db-url
   :user db-user
   :password db-password})

(def bills-q (-> (h/select :b.bill_id
                           :b.uid
                           :b.creation_time
                           :b.price
                           :bd.org_bd_kpp
                           :bd.company_name
                           :p.tax_number
                           :bd.pr_bd_firstname
                           :bd.pr_bd_lastname)
                 (h/from [:bill :b])
                 (h/merge-left-join [:general_bank_detail :bd]
                                    [:= :b.bank_detail_id :bd.bank_detail_id])
                 (h/merge-left-join [:payer :p]
                                    [:= :bd.payer_id :p.payer_id])
                 ;; (h/where [:= :b.payment_time nil]
                 ;;          [:= :b.offshore false]
                 ;;          [:= :b.is_deleted false]
                 ;;          [:= :b.status 0]
                 ;;          [:= :b.currency "RUR"]
                 ;;          [:<> :bd.org_bd_kpp nil]
                 ;;          [:<> :p.tax_number nil])
                 (h/order-by :creation_time)
                 (sql/build)))

(defn get-bills [db]
  (j/query db (-> bills-q
                  (f/format))))

(defn get-bills-by-ids [db ids]
  (j/query db (-> bills-q
                  (h/merge-where [:in :b.bill_id ids])
                  (f/format))))

