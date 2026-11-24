# Тапсырма 8. Карточка және құжаттар

Мәтіндер Gradle Play Publisher форматында сақталған: `app/src/main/play/listings/<тіл>/`.
Графика: `store-assets/`.

| Элемент | Талап | Дайын |
|---|---|---|
| Атауы | ≤ 30 таңба | ru: «LabNotes: заметки» (17), en: «LabNotes: quick notes» (21) |
| Қысқа сипаттама | ≤ 80 | ru: 73 таңба, en: 64 таңба |
| Толық сипаттама | ≤ 4000 | ru: 544, en: 479 |
| Иконка | 512×512 PNG, ≤ 1 МБ | `store-assets/icon-512.png` |
| Feature graphic | 1024×500 PNG/JPG | `store-assets/feature-graphic-1024x500.png` |
| Скриншоттар | ≥ 4 (телефон: 2–8 дана, 16:9 немесе 9:16, 320–3840 px) | Төменде қалай түсіру керек |
| Категория | | Productivity (Продуктивность) |
| Байланыс e-mail | міндетті | өз e-mail |
| Құпиялылық саясаты | жарияланған URL | `docs/privacy-policy.html` → GitHub Pages |

## Скриншоттар (≥ 4)
Pixel 9 эмуляторында release немесе staging нұсқасын ашыңыз, бірнеше жазба қосыңыз, эмулятор панеліндегі 📷 батырмасы:
1. Жазбалар тізімі (жарық тема).
2. Жаңа жазба енгізу.
3. Іздеу нәтижесі.
4. Қараңғы тема (эмулятор: Settings → Display → Dark theme).
5. (қосымша) Бос жазба қатесі.

> Staging-тің debug батырмаларын жасыру үшін скриншотты **release** нұсқасынан түсіріңіз.

## Құпиялылық саясатын жариялау (GitHub Pages)
1. `docs/privacy-policy.html` ішіндегі `YOUR_EMAIL@example.com` мекенжайын ауыстырыңыз.
2. GitHub → репозиторий → **Settings → Pages** → Source: *Deploy from a branch*, Branch: `main`, Folder: `/docs` → Save.
3. 1–2 минуттан кейін: `https://<USERNAME>.github.io/LabNotes/privacy-policy.html` — осы сілтемені
   Play Console → **App content → Privacy policy** өрісіне қойыңыз.

## Жас рейтингі (App content → Content rating, IARC сауалнамасы)
Санат: *All other app types* / Utility, Productivity. Барлық сұраққа **Жоқ**: зорлық, қорқыныш, жыныстық мазмұн,
балағат сөздер, есірткі, құмар ойындар, пайдаланушылар арасындағы байланыс/контент алмасу жоқ, орналасуды бөлісу жоқ,
сандық тауар сатып алу жоқ. Күтілетін нәтиже: **3+ (PEGI 3 / ESRB Everyone)**.
Target audience: 13+ (балаларға арналмаған — Families саясатын болдырмау үшін).

## Data Safety формасы (App content → Data safety)
| Сұрақ | Жауап |
|---|---|
| Пайдаланушы деректерін жинайды немесе бөліседі ме? | **Иә** (Firebase арқылы) |
| Барлық деректер тасымалда шифрланған ба? | **Иә** (HTTPS) |
| Пайдаланушы деректерді жоюды сұрай ала ма? | **Иә** (e-mail арқылы; жазбалар қосымшада жойылады) |
| Жазбалар (пайдаланушы мәтіні) | **Жиналмайды** — құрылғыдан шықпайды |

Жиналатын деректер түрлері:
| Санат → түр | Жиналады | Бөлісіледі | Міндетті ме | Мақсат |
|---|---|---|---|---|
| App info and performance → **Crash logs** | Иә | Жоқ¹ | Міндетті | Analytics, App functionality |
| App info and performance → **Diagnostics** | Иә | Жоқ¹ | Міндетті | Analytics |
| App activity → **App interactions** | Иә | Жоқ¹ | Міндетті | Analytics |
| Device or other IDs → **Device or other IDs** (Firebase installation ID) | Иә | Жоқ¹ | Міндетті | Analytics |

¹ Google Firebase — сіздің атыңыздан деректерді өңдейтін сервис-провайдер; Play ережелері бойынша бұл «бөлісу» (sharing) болып саналмайды.
Ephemeral processing: жоқ. Жарнама: жоқ (App content → Ads → «No, my app does not contain ads»).

Есепке: толтырылған Main store listing, Content rating сертификаты, Data safety алдын ала көрінісі (preview) скриншоттары.
