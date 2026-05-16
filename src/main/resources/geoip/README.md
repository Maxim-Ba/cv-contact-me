# GeoIP Database

Поместите файл `GeoLite2-Country.mmdb` в эту директорию.

## Как получить

1. Зарегистрируйтесь на [maxmind.com](https://www.maxmind.com/en/geolite2/signup)
2. В личном кабинете скачайте **GeoLite2 Country** (формат `.mmdb`)
3. Распакуйте архив и скопируйте `GeoLite2-Country.mmdb` сюда:
   `src/main/resources/geoip/GeoLite2-Country.mmdb`

## Настройка

По умолчанию используется `classpath:geoip/GeoLite2-Country.mmdb`.
Можно переопределить через переменную окружения:

```
GEOIP_DB_PATH=/opt/geoip/GeoLite2-Country.mmdb
```

## Без файла

Если файл отсутствует — сервис запускается в штатном режиме,
поле `country` в таблице `feedback` будет `null`.
