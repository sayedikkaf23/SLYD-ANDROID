# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:/Android  ADT/adt-bundle-windows-x86_64-20130717/adt-bundle-windows-x86_64-20130717/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keep class android.support.v7.widget.SearchView { *; }
-dontwarn org.**
-dontwarn retrofit2.Platform$Java8
-keepnames class com.fasterxml.jackson.** {
*;
}
-keepnames interface com.fasterxml.jackson.** {
    *;
}
-keep class com.couchbase.touchdb.** { *; }

-dontwarn com.squareup.okhttp.*
-dontwarn android.support.**
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# Gson specific classes
-keep class sun.misc.Unsafe { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
-dontwarn com.google.gson.**

-dontwarn com.google.common.collect.MinMaxPriorityQueue
-dontwarn sun.misc.Unsafe


-keep class org.webrtc.**  { *; }
-keep class org.appspot.apprtc.**  { *; }
-keep class de.tavendo.autobahn.**  { *; }

-keepclasseswithmembernames class * {
    native <methods>;}
    -keepclassmembers class ** {
        @com.squareup.otto.Subscribe public *;
        @com.squareup.otto.Produce public *;
    }
   -keep class com.couchbase.lite.store.SQLiteStore { *; }
      -keep class chat.hola.com.app.Database.SQLiteStore { *; }
      # Multiple image picker
      # Glide
      -keep public class * implements com.bumptech.glide.module.GlideModule
      -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
          **[] $VALUES;
          public *;
      }
      # support-v7-appcompat
      -keep public class android.support.v7.widget.** { *; }
      -keep public class android.support.v7.internal.widget.** { *; }
      -keep public class android.support.v7.internal.view.menu.** { *; }
      -keep public class * extends android.support.v4.view.ActionProvider {
          public <init>(android.content.Context);
      }
      # support-design
      -dontwarn android.support.design.**
      -keep class android.support.design.** { *; }
      -keep interface android.support.design.** { *; }
      -keep public class com.google.android.material.R$* { *; }

  -keep class android.support.graphics.drawable.** { *; }

            -keep class android.support.v8.renderscript.** { *; }

#For U-crop
-dontwarn chat.hola.com.app.CameraWithRecentMedia.UCrop.**
-keep class chat.hola.com.app.CameraWithRecentMedia.UCrop.** { *; }
-keep interface chat.hola.com.app.CameraWithRecentMedia.UCrop** { *; }

#For custom camera
-keep public class chat.hola.com.app.CameraWithRecentMedia.** { *; }
-keepattributes *Annotation*
-keep public class chat.hola.com.app.cameraActivities.** { *; }

#uCrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

#RxJava
-keep class rx.schedulers.Schedulers {
    public static <methods>;
}
-keep class rx.schedulers.ImmediateScheduler {
    public <methods>;
}
-keep class rx.schedulers.TestScheduler {
    public <methods>;
}
-keep class rx.schedulers.Schedulers {
    public static ** test();
}
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-keep class com.google.**
-dontwarn com.google.**
-dontwarn sun.misc.**

-dontwarn okio.**
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *; }
-keep interface com.squareup.okhttp3.** { *; }
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefaul

-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**
-dontoptimize
-dontpreverify

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

#For camera kit
-keepclasseswithmembers class com.camerakit.preview.CameraSurfaceView {
    native <methods>;
}

#adding for fixing signed build error
-ignorewarnings
-keep class * {
    public private *;
}

# RenderScript
-keepclasseswithmembernames class * {
native <methods>;
}
-keep class androidx.renderscript.** { *; }

#cloudinary
#-keep class com.cloudinary.** { *; }

#amazon aws
#-keepnames class com.amazonaws.**
#-keepnames class com.amazon.**
#-keep class com.amazonaws.services.**.*Handler
#-dontwarn com.fasterxml.jackson.**
#-dontwarn org.apache.commons.logging.**
#-dontwarn org.apache.http.**
#-dontwarn com.amazonaws.http.**
#-dontwarn com.amazonaws.metrics.**

#io card
-keep class io.card.payment.**{*;}