# payments-csv-generator

Утилита для генерации тестовых выгрузок из Альфа-банка или ВТБ


## Cборка

Для сборки нужен [Leiningen](http://leiningen.org/)

Из корня проекта запустить:

```
#> lein uberjar
```

## Использование

Запускать из командной строки с нужными параметрами:

```
#> java -jar target/payments-csv-generator-0.1.0-SNAPSHOT-standalone.jar -Hts41.pyn.ru --database master 
```

Параметры:

```
    -b, --bank BANK_TYPE        alfa       alfa or vtb 
    -H, --hostname DB_HOST      localhost  Database hostname
    -P, --port DB_PORT          5432       Database port
    -d, --database DB_NAME      hh         Database name
    -u, --username DB_USER      hh         Database user
    -p, --password DB_PASSWORD  123        Database password
    -i, --ids IDs                          Bill ids (optional)
    -h, --help                             Help info
````

Если указать id счетов, то в выгрузку попадут только они, если не указывать - отберутся все подходящие счета.
