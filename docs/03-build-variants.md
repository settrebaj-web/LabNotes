# Тапсырма 3. Сборка нұсқалары және конфигурациялар

Үш build type (`app/build.gradle.kts`):

| Нұсқа | applicationId | Атауы | Иконка фоны | API_BASE_URL | R8 | Қолтаңба | Debug құралдары |
|---|---|---|---|---|---|---|---|
| debug | `kz.aues.labnotes.debug` | LabNotes DEV | жасыл | dev-api… | жоқ | debug | бар |
| staging | `kz.aues.labnotes.staging` | LabNotes STG | қызғылт сары | staging-api… | бар | release (болмаса debug) | бар |
| release | `kz.aues.labnotes` | LabNotes | көк | api… | бар | release | жоқ |

Суффикстер арқасында үш нұсқа бір телефонда қатар орнатылады.
Ресурстар бөлек: `app/src/debug/res`, `app/src/staging/res` (strings.xml + colors.xml) main-ды үстінен жазады.

## Android Studio-да ауыстыру
**View → Tool Windows → Build Variants** → `app` модулі үшін *debug / staging / release* таңдаңыз → ▶ Run.
Экранның төменгі жағында `v0.1.0-staging (100) · https://staging-api…` жолы нұсқаны көрсетеді.

## Құпиялар репозиторийден тыс
| Құпия | Жергілікті | CI |
|---|---|---|
| API кілті | `local.properties`: `labnotes.apiKey=...` | Secret `LABNOTES_API_KEY` |
| Keystore паролі | `keystore.properties` | Secrets `KEYSTORE_*` |
| Firebase конфигі | `app/google-services.json` | Secret `GOOGLE_SERVICES_JSON` |
| Play API кілті | `play-service-account.json` | Secret `PLAY_SERVICE_ACCOUNT_JSON` |

Барлығы `.gitignore` ішінде. Кодта қолжетімділік: `BuildConfig.API_KEY`.
Ескерту: APK ішіндегі кез келген жол декомпиляцияланады — шын құпия кілттер серверде болуы керек;
`BuildConfig` тек репозиторийге түспеуін қамтамасыз етеді.

Есепке скриншоттар: Build Variants терезесі; телефондағы үш иконка қатар; әр нұсқаның төменгі жолы.
