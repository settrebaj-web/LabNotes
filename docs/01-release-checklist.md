# Тапсырма 1. Релизге дайындық аудиті (чек-лист)

Қосымша: **LabNotes** (`kz.aues.labnotes`) — құрылғыда сақталатын жазбалар. Тексеру күні: ____ . Нұсқа: ____ .
Белгілеу: ✅ орындалды, ⚠️ ескертумен, ❌ орындалмады.

## 1. Функционалдылық
| # | Тексеру | Нәтиже |
|---|---|---|
| 1.1 | Жазба қосу, бос жазба қабылданбайды («Бос жазбаны сақтауға болмайды») | |
| 1.2 | Жазбаны жою | |
| 1.3 | Іздеу (регистрге тәуелсіз), бос тізімде іздеу | |
| 1.4 | Қосымшаны қайта ашқанда жазбалар сақталған | |
| 1.5 | Экранды бұру, қараңғы тема | |
| 1.6 | Желісіз режимде іске қосу (ұшақ режимі) | |

## 2. Белгілі қателер (known issues)
| ID | Сипаттама | Маңыздылығы | Релизді блоктай ма? |
|---|---|---|---|
| — | Жоқ / толтырыңыз | | |

## 3. Әртүрлі құрылғылар мен ОС нұсқаларында тестілеу
minSdk = 26 (Android 8.0), targetSdk = 36 (Android 16).

| Құрылғы / эмулятор | Android (API) | Экран | Нәтиже |
|---|---|---|---|
| Pixel 2 эмуляторы | 8.0 (26) — минимум | 5" | |
| Pixel 6 эмуляторы | 12 (31) | 6.4" | |
| Pixel 9 эмуляторы | 16 (36) — target | 6.3" | |
| Нақты телефон: ____ | ____ | | |
| Планшет эмуляторы (Pixel Tablet) | 14 (34) | 10.9" | |

Android Studio: **Tools → Device Manager → Create Virtual Device** арқылы жасаңыз.

## 4. Сұралатын рұқсаттар (permissions)
| Рұқсат | Неге қажет | Қауіпті (runtime) ме? |
|---|---|---|
| `INTERNET` | Crashlytics, Analytics, Remote Config | Жоқ (normal) |
| `ACCESS_NETWORK_STATE`, `WAKE_LOCK` | Firebase кітапханалары манифестке өзі қосады | Жоқ |
| `BIND_GET_INSTALL_REFERRER_SERVICE` | Firebase Analytics (орнату көзі) | Жоқ |
| `DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` | AndroidX Core қосымшаның ішкі қолтаңбалы рұқсаты | Жоқ |
| ~~`AD_ID`, `ACCESS_ADSERVICES_AD_ID`, `ACCESS_ADSERVICES_ATTRIBUTION`~~ | Firebase Analytics қосады. **Аудит нәтижесі: жарнама жоқ → `tools:node="remove"` арқылы алынды**, Data Safety-де «Advertising ID жиналмайды» | — |

Аудит кезінде merged manifest-тен (`app/build/outputs/logs/manifest-merger-release-report.txt`) табылды.

Тексеру: **Build → Analyze APK… → AndroidManifest.xml** — біріктірілген (merged) манифесттегі барлық рұқсатты көріңіз.

## 5. Сторонние кітапханалар және лицензиялар
| Кітапхана | Нұсқа | Лицензия |
|---|---|---|
| AndroidX Core, Lifecycle, Activity | 1.16 / 2.9 / 1.10 | Apache 2.0 |
| Jetpack Compose (BOM 2025.05.01), Material 3 | — | Apache 2.0 |
| Firebase Crashlytics / Analytics / Remote Config (BOM 33.14.0) | — | Apache 2.0 (SDK) + Firebase ToS |
| Play In-App Updates (`app-update`) | 2.1.0 | Play Core SDK Terms of Service |
| Kotlin stdlib | 2.0.21 | Apache 2.0 |
| JUnit (тек тест) | 4.13.2 | EPL 1.0 |

Толық тізім: `./gradlew app:dependencies --configuration releaseRuntimeClasspath`. Release APK-ға кіретін транзитивті тәуелділіктер саны 161 (`app/build/outputs/sdk-dependencies/release/sdkDependencies.txt`).

## 6. Кодта API кілттері мен парольдер жоқ
- [ ] `git grep -nE "(password|apiKey|secret|BEGIN PRIVATE KEY)"` — нәтиже тек мысал файлдарында.
- [ ] `keystore.properties`, `*.jks`, `play-service-account.json`, `google-services.json` — `.gitignore` ішінде.
- [ ] API_KEY `local.properties` (жергілікті) немесе `LABNOTES_API_KEY` (CI) арқылы беріледі.

## 7. Отладка логтарын жою
- [ ] Release-те `Log.v/d/i` R8 ережесімен (`-assumenosideeffects`) алынып тасталады — `proguard-rules.pro`.
- [ ] Тексеру: release APK-ны **Analyze APK** арқылы ашып, `classes.dex` ішінде "event:" жолын іздеу → жоқ.
- [ ] Тест крэш батырмасы тек debug/staging-те (`BuildConfig.SHOW_DEBUG_TOOLS`).
- [ ] `isDebuggable = false` release-те (әдепкі).

## Қорытынды
Релизге дайын: **иә / жоқ**. Жауапты: Торебай Ә.Е.
