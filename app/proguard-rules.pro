# Тапсырма 4 — R8 ережелері.

# Crashlytics стектрейсінде жол нөмірлері мен файл атауы қалсын
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Пайдаланушы тапсырмасы: релизде отладка логтарын толық алып тастау
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
}

# Firebase және Play кітапханалары өз consumer-rules файлдарымен келеді,
# сондықтан қосымша -keep ережелері қажет емес.
