# Тапсырма 13. Мониторинг және аналитика

Кодта дайын: `platform/Telemetry.kt` (Crashlytics + Analytics), Crashlytics Gradle плагині (mapping-ті автоматты жүктейді),
debug/staging-тегі **«Тест крэш»** және **«Non-fatal»** батырмалары.

## 1. Firebase қосу
1. console.firebase.google.com → **Add project** `LabNotes` → Google Analytics: қосу.
2. **Add app → Android** үш рет (бір жобаға): `kz.aues.labnotes`, `kz.aues.labnotes.debug`, `kz.aues.labnotes.staging`.
3. Соңғы рет жүктелген `google-services.json` (үш клиент та ішінде болады) → `app/` папкасына.
4. Android Studio → **Sync Project with Gradle Files**. Gradle google-services және crashlytics плагиндерін өзі қосады.
   (Немесе: **Tools → Firebase → Crashlytics → Connect to Firebase** ассистенті.)

## 2. Тест крэш және стектрейсті ашу (mapping)
> debug нұсқасында Crashlytics әдейі өшірулі. **staging** қолданыңыз — ол R8-мен обфускацияланған, яғни нақты релизге ұқсас.

1. Build Variants → `staging` → Run. **«Тест крэш»** басыңыз → қосымша құлайды.
2. Қосымшаны **қайта ашыңыз** (есеп келесі іске қосылғанда жіберіледі). 1–5 минут күтіңіз.
3. Firebase → **Crashlytics** → `RuntimeException: Test Crash — LabNotes` issue → стектрейсті ашыңыз.
   Плагин mapping-ті жүктегендіктен, атаулар (`NotesScreenKt.triggerTestCrash`) ашық көрінеді.
4. **Қолмен расшифровка** (mapping-тің не істейтінін көрсету үшін):
   - Android Studio **Logcat**-тан обфускацияланған стектрейсті көшіріп, `stacktrace.txt`-ке сақтаңыз (мыс. `at a.b.a(SourceFile:12)`).
   - Терминалда:
     ```bat
     %ANDROID_HOME%\cmdline-tools\latest\bin\retrace.bat app\build\outputs\mapping\staging\mapping.txt stacktrace.txt
     ```
     (cmdline-tools жоқ болса: SDK Manager → SDK Tools → *Android SDK Command-line Tools* орнатыңыз.)
   - Есепке: «дейін» (обфускацияланған) және «кейін» (расшифрланған) екі нұсқаны қатар қойыңыз.

## 3. Аналитика
Оқиғалар: `app_open` (version), `note_added`, `note_deleted`, `in_app_update_declined`.
Нақты уақытта көру — **DebugView**:
```bat
adb shell setprop debug.firebase.analytics.app kz.aues.labnotes.staging
```
Firebase → Analytics → **DebugView** → жазба қосыңыз, оқиғалар секундтарда пайда болады. Өшіру: `setprop ... .none.`
Альтернатива: AppMetrica (Яндекс) — `io.appmetrica.analytics:analytics` + `AppMetrica.activate()`.

## 4. Ескертулер (минимум екеуі)
Firebase Console → Crashlytics → жоғарғы оң жақтағы ⚙ / **Alerts** (интерфейсте «Project settings → Alerts» деп те орналасуы мүмкін):

| # | Ескерту | Шегі | Арна |
|---|---|---|---|
| 1 | **New fatal issue** | кез келген жаңа fatal | E-mail |
| 2 | **Trending / velocity issue** | бір issue 1 сағатта пайдаланушылардың ≥ 1 %-на әсер етсе | E-mail (+ Slack/Discord интеграциясы — Project settings → Integrations) |
| 3 | **Regressed issue** | жабылған issue қайта пайда болды | E-mail |
| 4 | Crash-free users < 99 % | Play Console → Android vitals ескертулері (немесе Sentry: Alerts → Crash-free session rate) | E-mail |

## 5. Android Vitals
Play Console → **Monitor and improve → Android vitals → Overview**:
| Метрика | Play-дің «нашар мінез-құлық» шегі | Менің мәнім |
|---|---|---|
| User-perceived crash rate | 1.09 % | |
| User-perceived ANR rate | 0.47 % | |
| Slow cold start | > 5 c | |

Ішкі тестерлер аз болса Vitals деректері бос болуы мүмкін — онда іске қосу уақытын жергілікті өлшеңіз:
```bat
adb shell am force-stop kz.aues.labnotes
adb shell am start -W -n kz.aues.labnotes/.MainActivity
```
`TotalTime` (мс) — cold start. 5 рет өлшеп, орташасын жазыңыз. Logcat-та `Displayed kz.aues.labnotes` жолы да осыны көрсетеді.
ANR тексеру: StrictMode немесе Android Studio **App Quality Insights** терезесі (Crashlytics + Vitals бір жерде).

Есепке: Crashlytics issue беті, retrace «дейін/кейін», DebugView, Alerts баптаулары, Vitals/Startup өлшемдері.
