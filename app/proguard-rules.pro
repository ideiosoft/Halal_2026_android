# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class com.dapl.halaladvisor.Model.** { *; }

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature

# OkHttp
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-keepattributes Signature
-keepattributes Annotation

# Okio
-dontwarn okio.**
-keep class okio.** { *; }

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * extends com.google.gson.TypeAdapter

-keep class com.shockwave.**