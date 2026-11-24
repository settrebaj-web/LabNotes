import com.github.triplet.gradle.androidpublisher.ReleaseStatus
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.play.publisher)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
}

// ---------- Firebase (Тапсырма 13) ----------
// google-services.json болмаса, жоба Firebase-сіз де жиналады (CI, жаңа клон).
if (file("google-services.json").exists()) {
    apply(plugin = "com.google.gms.google-services")
    apply(plugin = "com.google.firebase.crashlytics")
}

// ---------- Құпия деректерді оқу (Тапсырма 3, 5) ----------
fun loadProps(name: String) = Properties().apply {
    val f = rootProject.file(name)
    if (f.exists()) f.inputStream().use { load(it) }
}
val localProps = loadProps("local.properties")
val keystoreProps = loadProps("keystore.properties")

/** Алдымен keystore.properties, болмаса CI айнымалысы (GitHub Secrets). */
fun signingValue(key: String, env: String): String? = keystoreProps.getProperty(key) ?: System.getenv(env)

val releaseStoreFile = signingValue("storeFile", "KEYSTORE_PATH")
val hasReleaseKey = releaseStoreFile != null && rootProject.file(releaseStoreFile).exists()

// ---------- Версия (Тапсырма 2) ----------
val versionProps = loadProps("version.properties")
val vMajor = versionProps.getProperty("VERSION_MAJOR").toInt()
val vMinor = versionProps.getProperty("VERSION_MINOR").toInt()
val vPatch = versionProps.getProperty("VERSION_PATCH").toInt()
require(vMinor in 0..99 && vPatch in 0..99) { "MINOR/PATCH 0..99 аралығында болуы керек" }
val appVersionName = "$vMajor.$vMinor.$vPatch"
val appVersionCode = vMajor * 10_000 + vMinor * 100 + vPatch

// -PnoR8 — оптимизациясыз салыстыру жинағы үшін (Тапсырма 4)
val disableR8 = project.hasProperty("noR8")

android {
    namespace = "kz.aues.labnotes"
    compileSdk = 36

    defaultConfig {
        applicationId = "kz.aues.labnotes"
        minSdk = 26
        targetSdk = 36
        versionCode = appVersionCode
        versionName = appVersionName

        val apiKey = localProps.getProperty("labnotes.apiKey") ?: System.getenv("LABNOTES_API_KEY") ?: ""
        buildConfigField("String", "API_KEY", "\"$apiKey\"")
        buildConfigField("String", "API_BASE_URL", "\"https://api.labnotes.example.kz/\"")
        buildConfigField("boolean", "SHOW_DEBUG_TOOLS", "false")
    }

    signingConfigs {
        create("release") {
            if (hasReleaseKey) {
                storeFile = rootProject.file(releaseStoreFile!!)
                storePassword = signingValue("storePassword", "KEYSTORE_PASSWORD")
                keyAlias = signingValue("keyAlias", "KEY_ALIAS")
                keyPassword = signingValue("keyPassword", "KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            buildConfigField("String", "API_BASE_URL", "\"https://dev-api.labnotes.example.kz/\"")
            buildConfigField("boolean", "SHOW_DEBUG_TOOLS", "true")
        }
        getByName("release") {
            isMinifyEnabled = !disableR8
            isShrinkResources = !disableR8
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = if (hasReleaseKey) signingConfigs.getByName("release") else null
        }
        create("staging") {
            initWith(getByName("release"))
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            matchingFallbacks += "release"
            buildConfigField("String", "API_BASE_URL", "\"https://staging-api.labnotes.example.kz/\"")
            buildConfigField("boolean", "SHOW_DEBUG_TOOLS", "true")
            // Кілт болмаса — debug кілтімен қол қойылады, тестке орнатуға жарайды
            signingConfig = if (hasReleaseKey) signingConfigs.getByName("release") else signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    lint {
        abortOnError = true
        checkReleaseBuilds = true
        htmlReport = true
    }
}

// ---------- Автожариялау: Gradle Play Publisher (Тапсырма 11) ----------
play {
    track.set("internal")
    defaultToAppBundles.set(true)
    // Play-де әлі жарияланбаған (draft) қосымша үшін: ./gradlew publishReleaseBundle -PplayDraft
    releaseStatus.set(if (project.hasProperty("playDraft")) ReleaseStatus.DRAFT else ReleaseStatus.COMPLETED)
    val creds = System.getenv("PLAY_CREDENTIALS_PATH") ?: rootProject.file("play-service-account.json").path
    if (file(creds).exists()) serviceAccountCredentials.set(file(creds))
}
android {
    playConfigs {
        register("staging") { enabled.set(false) }
    }
}

// ---------- Линтерлер (Тапсырма 10) ----------
ktlint {
    version.set("1.5.0")
    android.set(true)
}
detekt {
    buildUponDefaultConfig = true
    config.setFrom(rootProject.file("config/detekt/detekt.yml"))
}

// mapping.txt файлын версия бойынша сақтау (Тапсырма 4, 13)
tasks.register<Copy>("archiveReleaseMapping") {
    description = "mapping.txt файлын release-artifacts/<version>/ ішіне көшіреді"
    dependsOn("minifyReleaseWithR8")
    from(layout.buildDirectory.file("outputs/mapping/release/mapping.txt"))
    into(rootProject.layout.projectDirectory.dir("release-artifacts/$appVersionName"))
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.fragment.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.config)
    implementation(libs.play.app.update)

    debugImplementation(libs.androidx.ui.tooling)
    testImplementation(libs.junit)
}
