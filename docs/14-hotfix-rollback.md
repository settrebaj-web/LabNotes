# Тапсырма 14. Жаңарту, hotfix және «кері қайтару»

## 1.1.0 — түзету + жаңа функция
Жобаның ағымдағы коды 1.1.0 мазмұнын қамтиды: бос жазба тексерісі (`NoteRules.normalize`) және «Жою» батырмасы.
```bash
# version.properties: 1.1.0 ; CHANGELOG: [1.1.0] ; release-notes: 09-whats-new.md-ден
git commit -am "feat: delete notes; fix: reject blank notes (1.1.0)"
git tag -a v1.1.0 -m "1.1.0" && git push && git push origin v1.1.0
```
CI → internal → Approve → production 1% → 10/50/100%.

## 1.2.0 — критикалық қатемен «сәтсіз» релиз (симуляция)
`NotesScreen.kt` ішінде `val visible = ...` жолынан кейін **әдейі** қате қосыңыз:
```kotlin
// 1.2.0: «бірінші табылған жазба» көмекшісі — БАГ: тізім бос болса NoSuchElementException
if (searchEnabled && query.isNotBlank()) {
    Text("Табылды: ${visible.size}. Бірінші: ${visible.first().text}")
}
```
Сәйкес келмейтін сөзді іздегенде (немесе тізім бос кезде) қосымша құлайды.
version.properties → 1.2.0 (versionCode 10200), тег `v1.2.0`, staged rollout 1% → 10%.

### Инцидентке әрекет
1. Crashlytics «New fatal issue» / velocity ескертуі келеді → `NoSuchElementException` NotesScreenKt.
2. **Rollout-ты тоқтату**: Play Console → Production → Manage rollout → **Halt rollout**
   (немесе Actions → Rollout control → `halted`). Жаңа пайдаланушылар 1.2.0 алмайды.
3. **Kill switch** (бірден, сборкасыз): Firebase → Remote Config → `search_enabled = false` → Publish.
   1.2.0 бар пайдаланушыларда іздеу жасырылады → крэш тоқтайды (келесі fetch кезінде).
4. **Hotfix 1.2.1**: `visible.first()` → `visible.firstOrNull()?.text ?: "—"` (немесе блокты алып тастау),
   unit-тест қосу, version.properties → **1.2.1 (versionCode 10201 > 10200)**, тег `v1.2.1`.
5. 1.2.1 internal → тексеру → production (1.2.0-ді ауыстырады) → тұрақты болса `search_enabled = true`.
6. Міндетті жаңарту: Remote Config `min_supported_version_code = 10201` →
   1.2.0 пайдаланушыларына In-App Update (IMMEDIATE) экраны шығады (`UpdateChecker.kt`).
   Тексеру үшін: Play Console → **Internal app sharing** арқылы ескі және жаңа нұсқаларды орнатыңыз.

### Қалпына келтіру уақыты (MTTR) журналы
| Оқиға | Уақыты (сағ:мин) |
|---|---|
| 1.2.0 rollout басталды | |
| Алғашқы крэш / ескерту келді (detect) | |
| Rollout тоқтатылды (halt) | |
| Remote Config kill switch жарияланды (mitigate) | |
| 1.2.1 тегі жасалды | |
| 1.2.1 Play-де қолжетімді (resolve) | |
| **MTTD** (detect − start) | |
| **Mitigation time** (kill switch − detect) | |
| **MTTR** (resolve − detect) | |

## Неге дүкенде нұсқаны пайдаланушы деңгейінде «кері қайтаруға» болмайды
- Play/RuStore тек **versionCode өскен** сборканы қабылдайды; 10100-ді 10200-ден кейін қайта жариялау мүмкін емес.
- Android өзі де орнатылған қосымшаны **төменгі versionCode**-қа «жаңартуға» рұқсат бермейді (downgrade тек
  жою арқылы → пайдаланушы деректері жоғалады; `adb install -d` тек debuggable жинақтар үшін).
- **Halt** тек жаңа таратуды тоқтатады — 1.2.0-ді алып қойған құрылғылардан оны ешкім «қайтарып» ала алмайды.
- Дерекқор схемасы көшірілген болса, ескі код жаңа деректермен үйлесімсіз болуы мүмкін.

## Айналып өту тәсілдері
| Тәсіл | Қалай | Жылдамдығы |
|---|---|---|
| **Roll forward** | Ескі (жұмыс істейтін) кодты жаңа versionCode-пен шығару (мыс. 1.1.0 коды → 1.2.1) | сағаттар |
| **Remote Config / Feature flags** | Мәселелі функцияны сервер жағынан өшіру (`search_enabled`) | минуттар |
| **Kill switch / degradation** | Флаг бойынша функцияның қауіпсіз нұсқасына ауысу | минуттар |
| **Міндетті жаңарту** | In-App Updates IMMEDIATE + `min_supported_version_code` | hotfix шыққан соң |
| **Сервер жағы** | API-де ескі клиентке арнайы жауап, қате деректерді сүзу | минуттар |
| **Кезеңдік шығару** | 1%-да ұстау, әсерді шектеу | профилактика |

Есепке: Crashlytics-тағы 1.2.0 issue, Halt rollout диалогы, Remote Config жариялау, 1.2.1 релизі, In-App Update экраны, MTTR кестесі.
