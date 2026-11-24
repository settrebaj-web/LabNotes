# Changelog

Барлық маңызды өзгерістер осы файлда жазылады.
Формат: [Keep a Changelog](https://keepachangelog.com/ru/1.1.0/), нұсқалау: [SemVer](https://semver.org/lang/ru/).

> Ескерту: 1.1.0–1.2.1 жазбалары — Тапсырма 14 сценарийі. Әр релизді шығарған сайын
> `[Unreleased]` бөлімін жаңа нұсқа атауына көшіріп, күнін нақты күнге ауыстырыңыз.

## [Unreleased]

## [1.2.1] - 2026-12-08
### Fixed
- Іздеу өрісіне бірінші таңбаны енгізгенде, тізім бос болса, қосымша құлайтын қате (1.2.0-дегі критикалық қате, #12).

## [1.2.0] - 2026-12-07 [YANKED]
### Added
- Жазбаларды іздеу (Remote Config `search_enabled` жалауымен басқарылады).
### Known issues
- Бос тізімде іздеу кезінде крэш — rollout 10%-да тоқтатылды, 1.2.1-де түзетілді.

## [1.1.0] - 2026-12-03
### Added
- Жазбаны жою батырмасы.
- Міндетті жаңарту (In-App Updates, Remote Config `min_supported_version_code`).
### Fixed
- Бос немесе тек бос орындардан тұратын жазба сақталатын қате (#3).

## [1.0.0] - 2026-11-28
### Added
- Google Play-дегі алғашқы публикация (internal testing).
- Firebase Crashlytics және Analytics.
- Карточка, құпиялылық саясаты, Data Safety.

## [0.1.0] - 2026-11-24
### Added
- Жазбаларды қосу және тізім.
- debug / staging / release сборка нұсқалары.
- R8, қолтаңба, CI.

[Unreleased]: https://github.com/USERNAME/LabNotes/compare/v1.2.1...HEAD
[1.2.1]: https://github.com/USERNAME/LabNotes/compare/v1.2.0...v1.2.1
[1.2.0]: https://github.com/USERNAME/LabNotes/compare/v1.1.0...v1.2.0
[1.1.0]: https://github.com/USERNAME/LabNotes/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/USERNAME/LabNotes/compare/v0.1.0...v1.0.0
[0.1.0]: https://github.com/USERNAME/LabNotes/releases/tag/v0.1.0
