(ns payments-csv-generator.alfa
  (:require [payments-csv-generator.headers :as h]
            [clojure.pprint]
            [clj-time.format :as f]
            [clj-time.coerce :as c]))

(defn generate-alfa-line [idx {creation-time :creation_time
                               price :price
                               uid :uid
                               company-name :company_name
                               payer-kpp :org_bd_kpp
                               payer-inn :tax_number
                               firstname :pr_bd_firstname
                               lastname :pr_bd_lastname
                               :as bill}
                          statement]
  (println "[АЛЬФА] Генерация платежа для:")
  (clojure.pprint/pprint bill)
  (println)
  (let [date (f/unparse (f/formatter "dd.MM.yyyy") (c/from-sql-date creation-time))
        price (bigdec (/ price 100))]
    ["RUR" ;; Type - Код валюты счета
     date ;; O_date - Дата документа
     idx ;; Number - Номер документа
     price ;; Sum_val - Сумма док-та, в вал. счета
     price ;; Sum_rur - Сумма док-та, в руб. эквивал
     1 ;; Priority - Очередность платежа
     "Электронно" ;; Post - Вид платежа
     (or (:company-name statement) company-name (str firstname " " lastname)) ;; plat_name - Наименование плательщика
     (or (:inn statement) payer-inn "111123333331") ;; plat_inn - ИНН плательщика
     payer-kpp ;; plat_kpp - КПП плательщика
     (or (:payer-bank-account statement) "Счет") ;; plat_acc - Расчетный счет плательщика
     (or (:payer-bank-name statement) "Название") ;; plat_bank - Наименование Банка плательщика
     (or (:payer-bank-bik statement) "БИК") ;; plat_bic - БИК Банка плательщика
     nil ;; plat_ks - Корсчет банка плательщика
     nil ;; pol_name - Получатель
     nil ;; Pol_inn - ИНН получателя
     nil ;; pol_kpp - КПП получателя
     nil ;; pol_acc - Расчетный счет получателя
     "ОАО \"АЛЬФА-БАНК\" Г МОСКВА" ;; pol_bank - Банк получателя
     "44525593" ;; pol_bic - БИК банка получателя
     "30101810200000000000" ;; pol_ks - Корсчет банка получателя
     (if (get statement :include-uid? true)
       (format (get statement :subject "Номер счета %s") uid)
       (get statement :subject "Дефолтное назначение")) ;; text70 - Назначение платежа (str "Счет номер №" uid)
     nil ;; TaxStatus - Статус составителя расчетного документа
     nil ;; TaxKbk - Показатель кода бюджетной классификации
     nil ;; Okato - ОКАТО
     nil ;; TaxReason - Показатель основания налогового платежа
     nil ;; TaxPeriod - Показатель налогового периода
     nil ;; TaxNumber - Показатель номера налогового документа
     nil ;; TaxDate - Показатель даты налогового документа
     nil ;; TaxType - Показатель типа налогового платежа
     nil ;; RecBankDate - Дата отметки банка получателя
     nil ;; PartNumber - Номер частичной оплаты по порядку
     nil ;; ParentNumber - Номер родительского документа
     nil ;; ParentOperType - Вид операции родительского документа
     nil ;; ParentOperDate - Дата родительского документа
     nil ;; Accept - Акцепт
     nil ;; AcceptDays - Срок для акцепта
     nil ;; AcceptEnd - Окончание срока акцепта
     nil ;; RTerms - Условие оплаты
     nil ;; CollectionType - ВидАккредитива
     nil ;; SupplierAccNum - НомерСчетаПоставщика
     nil ;; PayBySubmission - ПлатежПоПредст
     nil ;; AdditionalTerms - ДополнУсловия
     nil ;; Quantity - Количество
     ]))

