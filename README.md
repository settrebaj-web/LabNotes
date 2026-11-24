# LabNotes — Зертханалық жұмыс №8 «Мобильді қосымшаларды жариялау және сүйемелдеу»

Пән: Мобильді қосымшаларды әзірлеу технологиялары · Орындаған: Торебай Ә.Е., ИИАДк-24-1б, АУЭС

**LabNotes** — Kotlin + Jetpack Compose-та жазылған жазбалар қосымшасы. Барлық 15 тапсырма осы бір жобада орындалады.
Жоба дайын конфигурациямен келеді; сізге Android Studio-да іске қосу, кілт жасау, консольдерде жариялау және скриншоттар түсіру қалады.

## Талаптар
- Android Studio (2025.1 Narwhal немесе жаңарақ), JDK 17 (Android Studio ішінде бар)
- Android SDK Platform **36** (SDK Manager → SDK Platforms → Android 16)
- Git, GitHub аккаунты; Google Play үшін — әзірлеуші аккаунты ($25) немесе GitHub Releases / RuStore

## Бастау
1. Архивті ашыңыз → Android Studio → **File → Open** → `LabNotes` папкасы → Trust project.
2. Gradle sync аяқталсын (бірінші рет 3–10 минут, интернет керек). AS «Upgrade AGP» ұсынса — қабылдауға болады.
3. Терминал (Alt+F12): `gradlew.bat ktlintFormat` — код стилін AS нұсқаңызға сәйкестендіреді.
4. Build Variants → **debug** → ▶ Run (эмулятор немесе телефон).
5. Git: **VCS → Enable Version Control → Git**, бірінші коммит, сосын `git tag -a v0.1.0 -m "0.1.0"`.

## Тапсырмалар картасы
| № | Тапсырма | Жобадағы файлдар | Нұсқаулық | Өзіңіз не істейсіз |
|---|---|---|---|---|
| 1 | Дайындық аудиті | `proguard-rules.pro`, манифест | [docs/01](docs/01-release-checklist.md) | Құрылғыларда тексеріп, кестені толтыру |
| 2 | SemVer, versionCode, тегтер | `version.properties`, `app/build.gradle.kts` | [docs/02](docs/02-versioning.md) | v0.1.0, v1.0.0 тегтері |
| 3 | debug / staging / release | `app/build.gradle.kts`, `app/src/debug`, `app/src/staging` | [docs/03](docs/03-build-variants.md) | 3 нұсқаны қатар орнату, скриншот |
| 4 | R8, өлшемді салыстыру, mapping | `app/build.gradle.kts`, `proguard-rules.pro` | [docs/04](docs/04-r8-optimization.md) | `-PnoR8` пен қалыпты сборканы салыстыру |
| 5 | Қол қою, upload vs app signing key | `keystore.properties.example` | [docs/05](docs/05-signing.md) | Keystore жасау |
| 6 | AAB және APK, орнату нұсқаулығы | — | [docs/06](docs/06-aab-apk-install.md) | Екеуін жинап, орнату |
| 7 | Дүкенде жариялау | — | [docs/07](docs/07-publishing.md) | Play internal / GitHub Releases / RuStore |
| 8 | Карточка, саясат, Data Safety | `app/src/main/play/listings`, `store-assets/`, `docs/privacy-policy.html` | [docs/08](docs/08-store-listing.md) | Скриншоттар, GitHub Pages, формалар |
| 9 | CHANGELOG, «Что нового» 2 тілде | `CHANGELOG.md`, `app/src/main/play/release-notes` | [docs/09](docs/09-whats-new.md) | Әр релизде жаңарту |
| 10 | CI | `.github/workflows/ci.yml`, `config/detekt`, `.editorconfig`, `app/src/test` | [docs/10-11](docs/10-11-ci-cd.md) | GitHub-қа push, PR ашу |
| 11 | CD, internal → production | `.github/workflows/release.yml` | [docs/10-11](docs/10-11-ci-cd.md) | Secrets, environment, Play API кілті |
| 12 | Кезеңдік шығару | `.github/workflows/rollout.yml` | [docs/12](docs/12-staged-rollout.md) | Консольде 1%→10% |
| 13 | Crashlytics, Analytics, ескертулер, Vitals | `platform/Telemetry.kt`, «Тест крэш» батырмасы | [docs/13](docs/13-monitoring.md) | Firebase жобасы, retrace |
| 14 | 1.1.0, 1.2.0 (баг), hotfix 1.2.1 | `platform/FeatureFlags.kt`, `platform/UpdateChecker.kt` | [docs/14](docs/14-hotfix-rollback.md) | Сценарийді өткізіп, уақытты жазу |
| 15 | Issues, SLA, қолдау саясаты | `.github/ISSUE_TEMPLATE`, `.github/dependabot.yml` | [docs/15](docs/15-support.md) | 3 issue, 2 пікірге жауап |

## Ұсынылатын орындау реті (уақыт бойынша)
1. **1-күн:** 1–6 (жергілікті), GitHub-қа push → CI (10) жасыл.
2. **2-күн:** Firebase (13), Play Console-да қосымша жасау, бірінші AAB-ны қолмен жүктеу (7), карточка (8).
3. **3-күн:** Secrets + environment (11) → `v1.0.0` тегі → автоматты internal.
4. **4–5-күн:** 1.1.0 → staged rollout (12) → 1.2.0 баг → halt + kill switch → 1.2.1 (14) → issues мен пікірлер (15).

## Жобаның құрылымы
```
app/src/main/java/kz/aues/labnotes/
  MainActivity.kt          – Remote Config fetch, міндетті жаңарту, Compose
  LabNotesApp.kt           – Telemetry init
  data/Note.kt             – NoteRules: тексеру, іздеу, сұрыптау (unit-тесттелген)
  data/NoteRepository.kt   – SharedPreferences + JSON
  platform/Telemetry.kt    – Crashlytics + Analytics (Firebase жоқ болса да жұмыс істейді)
  platform/FeatureFlags.kt – Remote Config: search_enabled, min_supported_version_code
  platform/UpdateChecker.kt– Play In-App Updates (IMMEDIATE)
  ui/NotesScreen.kt, ui/Theme.kt
app/src/{debug,staging}/res – нұсқаға тән атау мен иконка түсі
app/src/main/play/          – Gradle Play Publisher: карточка мәтіндері, «Что нового»
```

## Жиі командалар (Windows)
```bat
gradlew.bat assembleDebug                 :: debug APK
gradlew.bat assembleStaging               :: staging APK (R8 қосулы)
gradlew.bat bundleRelease assembleRelease :: релиз AAB + APK
gradlew.bat ktlintCheck detekt lintDebug testDebugUnitTest
gradlew.bat archiveReleaseMapping         :: mapping.txt → release-artifacts/<нұсқа>/
```

## Ескертулер
- `google-services.json` жоқ болса да жоба жиналады — Firebase жай өшірулі тұрады.
- Keystore жоқ болса `release` қол қойылмай жиналады (`app-release-unsigned.apk`), `staging` debug кілтімен қол қойылады.
- `USERNAME`, `YOUR_EMAIL@example.com` орындарын өз деректеріңізбен ауыстырыңыз (CHANGELOG, privacy-policy, ISSUE_TEMPLATE/config.yml).
