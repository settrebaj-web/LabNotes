# Тапсырма 4. Релиз сборкасын оңтайландыру (R8)

`release` және `staging` үшін қосулы:
```kotlin
isMinifyEnabled = true      // R8: кодты кішірейту + обфускация + оптимизация
isShrinkResources = true    // пайдаланылмайтын ресурстарды алып тастау
proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
```

## Өлшемдерді салыстыру (Windows Terminal, жоба түбірінде)
```bat
:: 1) R8-сіз
gradlew.bat assembleRelease -PnoR8
copy app\build\outputs\apk\release\*.apk ..\labnotes-noR8.apk

:: 2) R8-пен
gradlew.bat clean assembleRelease
copy app\build\outputs\apk\release\*.apk ..\labnotes-R8.apk
```
(macOS/Linux: `./gradlew`, `cp`). Keystore әлі жоқ болса, файл `app-release-unsigned.apk` деп аталады — салыстыруға жарайды.

Android Studio: **Build → Analyze APK…** → екі файлды ашып, **Compare with previous APK** батырмасы.

### Нәтиже (LabNotes 0.1.0, release, 24.11.2026)
| Көрсеткіш | R8-сіз (`-PnoR8`) | R8-пен | Айырма |
|---|---|---|---|
| **APK өлшемі** | **9 780 813 Б (9.33 МБ)** | **1 896 766 Б (1.81 МБ)** | **−80.6 % (5.2 есе кіші)** |
| DEX файлдар саны | 3 (multidex) | 1 | −2 |
| classes*.dex (сығылған) | 8 766 КБ | 1 439 КБ | −83.6 % |
| Әдістер (method refs) | 149 091 | 20 003 | −86.6 % |
| Кластар | 19 983 | 3 789 | −81.0 % |
| resources.arsc | 545 КБ | 211 КБ | −61.3 % |
| res/ | 43 КБ | 11 КБ | −74 % |

Қорытынды: өлшемнің негізгі бөлігі — Compose, Firebase және Kotlin кітапханаларының пайдаланылмайтын коды.
R8 оны алып тастады (tree shaking), қалғанын обфускациялап, бір DEX-ке сыйғызды; `shrinkResources`
кітапханалардың қолданылмайтын ресурстарын алды. Келесі жолы өлшеу үшін салыстыру файлдары:
`release-artifacts/app-noR8.apk`, `release-artifacts/app-R8.apk`.
mapping.txt өлшемі: 33.5 МБ (әр релизде сақталуы тиіс — онсыз крэш стектрейсін оқу мүмкін емес).

## Жұмысын тексеру
Release (немесе staging) нұсқасын орнатып, Тапсырма 1 чек-листінің 1-бөлімін қайталаңыз.
Ерекше назар: Firebase, Remote Config, JSON сақтау (R8 рефлексияны бұзуы мүмкін жерлер).

## mapping.txt сақтау
Файл: `app/build/outputs/mapping/release/mapping.txt` — әр сборка сайын жаңа, **нұсқаға байланысты**.
- Жергілікті: `gradlew.bat archiveReleaseMapping` → `release-artifacts/<нұсқа>/mapping.txt`.
- CI: Release workflow оны 90 күндік артефакт ретінде сақтайды.
- Crashlytics плагині оны Firebase-ке автоматты жүктейді; AAB ішінде де бар — Play Console оны өзі алады.

Есепке: Analyze APK салыстыру скриншоты + толтырылған кесте.
