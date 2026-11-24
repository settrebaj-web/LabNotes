# Тапсырма 5. Қосымшаға қол қою

## Keystore жасау
**Android Studio тәсілі:** Build → Generate Signed App Bundle or APK… → Android App Bundle → **Create new…**
- Key store path: `C:\Users\<сіз>\keys\labnotes-upload.jks` (жоба папкасынан **тыс**)
- Alias: `upload`, Validity: 25+ жыл (Google Play талабы — 2033 жылдан кейін аяқталуы керек)
- Certificate: аты-жөн, ұйым (АУЭС), қала (Алматы), ел коды (KZ)

**Терминал тәсілі** (`keytool` Android Studio JBR ішінде бар):
```bash
keytool -genkeypair -v -keystore labnotes-upload.jks -alias upload \
  -keyalg RSA -keysize 4096 -validity 10000 \
  -dname "CN=Torebay, OU=IIADk-24-1b, O=AUES, L=Almaty, C=KZ"
```

Содан кейін `keystore.properties.example` → `keystore.properties` деп көшіріп, жол мен парольдерді жазыңыз.
Gradle `release` сборкасына автоматты қол қояды. Тексеру:
```bash
gradlew.bat assembleRelease
%ANDROID_HOME%\build-tools\36.0.0\apksigner verify --print-certs app\build\outputs\apk\release\app-release.apk
```

## Upload key пен App signing key айырмасы (Play App Signing)
| | Upload key | App signing key |
|---|---|---|
| Кімде | Әзірлеушіде (сізде) | Google-да (Cloud KMS) |
| Неге қол қояды | Play Console-ға жүктелетін AAB | Пайдаланушыларға таратылатын APK-лар |
| Пайдаланушы көре ме | Жоқ | Иә — қосымшаның «тұлғасы» |
| Жоғалса | Play Console → App integrity → **Request upload key reset**: жаңа кілт жасап, сертификатын жібересіз; ~2 күн ішінде ауыстырылады. Қосымша, пайдаланушылар, жаңартулар сақталады | Google-да болғандықтан жоғалмайды |

**Play App Signing-сіз** (мысалы, RuStore немесе GitHub арқылы тікелей APK): сіздің кілтіңіз = қосымшаның тұлғасы.
Ол жоғалса **ешқашан жаңарту шығара алмайсыз** — Android басқа қолтаңбалы APK-ны жаңарту ретінде қабылдамайды;
жаңа applicationId-мен жаңа қосымша шығарып, пайдаланушыларды көшіруге тура келеді. Кілт ұрланса, зиянкес «жаңарту» таратуы мүмкін.
(RuStore-да кілтті ауыстыру тек қолдау қызметі арқылы, дәлелдеумен.)

## Keystore-ды қалай сақтаймын
1. Файл репозиторийден тыс: `C:\Users\<сіз>\keys\` (`.gitignore`-да `*.jks` бар).
2. Парольдер — құпия менеджерде (Bitwarden/KeePassXC), `keystore.properties` тек жергілікті.
3. Резервтік көшірме: шифрланған флешка + шифрланған бұлт (KeePassXC attachment немесе 7z AES-256), 2 түрлі жерде.
4. CI үшін: `base64 -w0 labnotes-upload.jks` → GitHub Secret `KEYSTORE_BASE64` (PowerShell: `[Convert]::ToBase64String([IO.File]::ReadAllBytes("labnotes-upload.jks"))`).
5. Сертификат SHA-256 саусақ ізін `keytool -list -v` арқылы жазып қойыңыз — кілтті сәйкестендіру үшін.

Есепке: Generate Signed Bundle диалогының скриншоты (парольдерсіз!), `apksigner verify` шығысы.
