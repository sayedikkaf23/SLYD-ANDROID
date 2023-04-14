# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
-keep class io.agora.**{*;}
-keepclassmembers class ai.deepar.ar.DeepAR { *; }
##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
# For using GSON @Expose annotation
-keepattributes *Annotation*
# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
##---------------End: proguard configuration for Gson  ----------
-keep,allowoptimization,allowobfuscation public class io.isometrik.gs.** {*;}
-flattenpackagehierarchy io.isometrik.gs
#-keep public class io.isometrik.gs.** {*;}
