# Changelog

Барлық маңызды өзгерістер осы файлда жазылады.
Формат: [Keep a Changelog](https://keepachangelog.com/ru/1.1.0/), нұсқалау: [SemVer](https://semver.org/lang/ru/).

## [Unreleased]

## [1.1.0] - 2026-11-28
### Added
- Алғашқы іске қосуда құпиялылық туралы хабарлама (саясатқа сілтемемен).
### Fixed
- Release нұсқасында экранның төменгі жолында ішкі API адресі көрсетілетін (Тапсырма 1 аудиті).
### Security
- Firebase Analytics қосатын жарнама идентификаторы рұқсаттары (`AD_ID`, `ADSERVICES`) манифесттен алынды.

## [1.0.0] - 2026-11-28
### Added
- GitHub Releases арқылы алғашқы жариялау: internal (pre-release) → production (қолмен растау).
- Қол қойылған release сборкасы (upload key), R8 + shrinkResources (APK 9.33 МБ → 1.81 МБ).
- CI: ktlint, detekt, Android Lint, unit-тесттер; CD: v* тегі бойынша релиз.
- Құпиялылық саясаты (GitHub Pages), issue шаблондары.

## [0.1.0] - 2026-11-24
### Added
- Жазбаларды қосу, жою, іздеу; жарық/қараңғы тема.
- debug / staging / release сборка нұсқалары.

[Unreleased]: https://github.com/settrebaj-web/LabNotes/compare/v1.1.0...HEAD
[1.1.0]: https://github.com/settrebaj-web/LabNotes/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/settrebaj-web/LabNotes/compare/v0.1.0...v1.0.0
[0.1.0]: https://github.com/settrebaj-web/LabNotes/releases/tag/v0.1.0
