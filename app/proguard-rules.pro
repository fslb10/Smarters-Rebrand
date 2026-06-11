# Keep Gson model classes (fields are populated by reflection).
-keep class com.example.iptvplayer.data.model.** { *; }

# Retrofit / OkHttp / Gson standard rules
-keepattributes Signature, *Annotation*
-dontwarn okhttp3.**
-dontwarn retrofit2.**
