(ns payments-csv-generator.vtb
  (:require [clj-time.format :as f]
            [clj-time.coerce :as c]))

(defn generate-vtb-line [idx {creation-time :creation_time
                              price :price
                              uid :uid
                              payer-name :company_name
                              payer-kpp :org_bd_kpp
                              payer-inn :tax_number
                              :as bill}]
  (println "Generate vtb line for bill:" bill)
  (let [date (f/unparse (f/formatter "dd.MM.yyyy") (c/from-sql-date creation-time))]
    [1 ;; Тип
     nil ;; Идентификатор
     date ;; Дата зачисления
     date ;; Дата списания
     date ;; Дата
     idx ;; Номер
     "01" ;; Вид операции
     (bigdec (/ price 100)) ;; Сумма
     "810" ;; Валюта
     (str "Счет номер №" uid) ;; Основание платежа
     "044525187" ;; БИК Банка получателя
     "40702810900190000478" ;; Счет получателя
     "ООО \"ХЭДХАНТЕР\"" ;; Наименование получателя
     "7718620740" ;; ИНН Получателя
     nil ;; Код счета Банка получателя
     "30101810700000000187" ;; Счет Банка получателя
     "БАНК ВТБ (ПАО)" ;; Наименование Банка получателя
     nil ;; Счет Плательщика
     payer-name ;; Наименование Плательщика
     payer-inn ;; ИНН Плательщика
     nil ;; Счет Банка плательщика
     nil ;; БИК Банка плательщика
     nil ;; Наименование Банка плательщика
     nil ;; Очередность платежа
     nil ;; Вид платежа
     nil ;; Срок платежа
     nil ;; Резервное поле 1
     payer-kpp ;; Резервное поле 2
     nil ;; Резервное поле 3
     nil ;; Резервное поле 4
     nil ;; Резервное поле 5
     nil ;; Резервное поле 6
     nil ;; Резервное поле 7
     nil ;; Резервное поле 8
     nil ;; Резервное поле 9
     nil ;; Резервное поле 10
     ]))
