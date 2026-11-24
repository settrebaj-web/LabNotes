# Тапсырма 7. Дүкенде жариялау

Арнаны таңдау:
| Арна | Құны | Ескерту |
|---|---|---|
| **Google Play — Internal testing** (ұсынылады) | Әзірлеуші аккаунты бір рет $25 | Тексеру жоқ немесе өте қысқа; 100 тестерге дейін; 11–15 тапсырмалар толық орындалады |
| GitHub Releases | Тегін | Модерация жоқ; Release workflow APK-ны автоматты жүктейді |
| RuStore | Тегін | Тек APK/AAB, тексеру 1–3 күн |

## A. Google Play Console (Internal testing)
1. play.google.com/console → **Create app**: атауы `LabNotes: заметки`, тілі Русский, App, Free.
2. Сол жақ мәзір → **Test and release → Testing → Internal testing → Testers** → e-mail тізімін жасаңыз (өзіңіз + 2 топтас).
3. **Create new release** → Play App Signing ұсынысын қабылдаңыз (Google-generated key) → `app-release.aab` жүктеңіз.
   > Бірінші AAB **қолмен** жүктеледі — Play API жаңа қосымшаға бірінші сборканы қабылдамайды. Келесілерін CI жүктейді.
4. Release name: `1.0.0 (10000)`, Release notes: `app/src/main/play/release-notes/` ішінен көшіріңіз.
5. **Next → Save and publish**. Ескертулерді (warnings) оқыңыз, қателер (errors) болмауы керек.
6. **Publishing overview** бетінде тексеру мәртебесін бақылаңыз.
7. Testers қойындысындағы **Copy link** — тестерлер сілтеме арқылы қатысып, Play-ден орнатады.

> Жеке аккаунт (2023 ж. қарашадан кейін тіркелген) production-ға шығу үшін алдымен **closed testing**-те
> 12 тестермен 14 күн өтуі керек. Зертханада internal/closed жеткілікті; CI-де `PROMOTE_TRACK=alpha` қойыңыз.

## B. GitHub Releases
```bash
git tag -a v1.0.0 -m "1.0.0" && git push origin v1.0.0
```
Release workflow APK-ны жинап, **Releases** бетіне жүктейді. Қолмен: репозиторий → Releases → Draft a new release → тег таңдап, APK тіркеу.

## C. RuStore
console.rustore.ru → жеке әзірлеуші ретінде тіркелу → **Создать приложение** → package `kz.aues.labnotes` →
APK жүктеу (RuStore үшін өз кілтіңізбен қол қойылған `app-release.apk`) → карточка → модерацияға жіберу.

## Есепке қажет скриншоттар (Console)
1. «Create app» формасы / қосымшалар тізімі.
2. Internal testing → Create new release беті (Play App Signing блогымен).
3. AAB жүктелгеннен кейінгі App bundle explorer (versionCode, өлшем, қолдау көрсетілетін құрылғылар саны).
4. Release review беті (ескертулер).
5. Publishing overview / «Available to internal testers» мәртебесі.
6. Телефондағы Play Store бетінде «Внутреннее тестирование» белгісі бар қосымша.
