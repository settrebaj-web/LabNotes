# Тапсырма 2. Нұсқалау (SemVer)

## Ережелер
`versionName = MAJOR.MINOR.PATCH` — [Semantic Versioning 2.0.0](https://semver.org/lang/ru/).

| Бөлік | Қашан көтеріледі | Мысал |
|---|---|---|
| **MAJOR** | Үйлесімсіз өзгеріс: деректер форматы ауысты (ескі жазбалар көшіруді қажет етеді), minSdk айтарлықтай көтерілді, негізгі функция алынды | 1.4.2 → 2.0.0 |
| **MINOR** | Кері үйлесімді жаңа функция: іздеу, жою, жаңа экран | 1.1.0 → 1.2.0 |
| **PATCH** | Тек қате түзету, функционалды өзгеріссіз (hotfix) | 1.2.0 → 1.2.1 |

`0.y.z` — алғашқы әзірлеу, тұрақты API жоқ. `1.0.0` — дүкенге алғашқы шығару.
MAJOR көтерілсе MINOR=PATCH=0; MINOR көтерілсе PATCH=0.

## versionCode ережесі (Android)
```
versionCode = MAJOR * 10000 + MINOR * 100 + PATCH
```
| versionName | versionCode |
|---|---|
| 0.1.0 | 100 |
| 1.0.0 | 10000 |
| 1.1.0 | 10100 |
| 1.2.0 | 10200 |
| 1.2.1 | 10201 |

Неге бұлай: Google Play/RuStore тек **қатаң өсетін** versionCode қабылдайды; формула SemVer-ден автоматты шығады,
сондықтан қолмен қателесу мүмкін емес. Шектеу: MINOR, PATCH ≤ 99 (Gradle-да `require` тексерісі бар).
Бір нұсқаны дүкенге қайта жүктеу керек болса (мыс. қол қою қатесі) — PATCH көтеріледі, versionCode-ты қайталауға болмайды.
iOS үшін аналог: `CFBundleShortVersionString` = versionName, `CFBundleVersion` = versionCode.

Нұсқа бір жерде сақталады: `version.properties`. `app/build.gradle.kts` оны оқып есептейді.

## Git тегтері
```bash
# 0.1.0 — қазіргі күй
git tag -a v0.1.0 -m "0.1.0: алғашқы прототип"
# version.properties → 1.0.0 өзгертіп, CHANGELOG жаңартып:
git commit -am "chore(release): 1.0.0"
git tag -a v1.0.0 -m "1.0.0: алғашқы релиз"
git push origin main --tags
```
Android Studio-да: **Git → New Tag…** (немесе Terminal). Тексеру: `git tag -n`.

## Менің нәтижем
```
$ git log --oneline --decorate
0d366d4 (HEAD -> main, tag: v1.0.0) chore(release): 1.0.0
51b7e55 (tag: v0.1.0) chore: LabNotes 0.1.0 — build variants, R8, signing, CI
```
v0.1.0 → versionCode 100, v1.0.0 → versionCode 10000 (`version.properties` бойынша Gradle есептеді).

Есепке скриншот: `git log --oneline --decorate` шығысы, онда `tag: v0.1.0`, `tag: v1.0.0` көрінеді.
