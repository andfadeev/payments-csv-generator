# Payments CSV Generator

Утилита для генерации тестовых выгрузок из Альфа-банка или ВТБ

## Окружение

Для использования нужена утилита [lein](http://leiningen.org/).

## Использование

Скопировать файл `config.edn.example` и добавить/изменить необходимые параметры:

```
=> cp config.edn.example config.edn
```
Описание `edn` формата: [https://github.com/edn-format/edn](https://github.com/edn-format/edn)

Запустить генерацию через `lein`:

```
=> lein run
```

## Результат
Файлы лежат в папке `csv`.

# Todo
- Добавить `spec` для конфига
- Поддержать генерацию платежек для физ. лиц
- Автоконвертация `sql.time` в `java.time.*`
- Auto `snake_case` to `kebab-case` for sql result sets

Pull requests are welcome!
