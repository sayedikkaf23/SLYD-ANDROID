package dependencies

object Deps {
    private const val path = "../commonFiles/gradleScript/"
    const val dependency = "./gradleScript/dependencies.gradle"
    val common = "${path}common.gradle"

    object Module {
        val data = ":data"
        val cache = ":cache"
        val remote = ":remote"
        val domain = ":domain"
        val stripe = ":stripe"
        val calling = ":calling"
    }

    object Facebook {
        val stetho = "com.facebook.stetho:stetho:${Version.stetho}"
        val stethoNetwork = "com.facebook.stetho:stetho-okhttp3:${Version.stetho}"
    }

    object LeakCanary {
        val leakcanary_debug = "com.squareup.leakcanary:leakcanary-android:${Version.leakCanary}"
        val leakcanary_release_no_op =
                "com.squareup.leakcanary:leakcanary-android-no-op:${Version.leakCanary}"
    }

    object Lifecycle {
        val extension = "android.arch.lifecycle:extensions:${Version.lifecycle}"
        val annotation_compliler = "android.arch.lifecycle:compiler:${Version.lifecycle}"
        val testing_core_testing = "android.arch.core:core-testing:${Version.lifecycle}"
        // ViewModel and LiveData
        val lifeCycleExtension =
                "androidx.lifecycle:lifecycle-extensions:${Version.lifecycleVersion}"
    }

    val easyPermission = "pub.devrel:easypermissions:${Version.easyPermission}"

    object Glide {
        val glide = "com.github.bumptech.glide:glide:${Version.glide}"
        val annotationProcessor = "com.github.bumptech.glide:compiler:${Version.glideCompiler}"
    }

    object Gson {
        val gson = "com.google.code.gson:gson:${Version.gson}"
    }

    object AndroidApi {
        val androidMlKit = "com.google.firebase:firebase-ml-vision:${Version.androidMlKit}"
    }

    object Kotlin {
        val kotlin_stdlib_jdk7 = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Version.kotlin}"
        val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.1"
    }

    object OkHttp3 {
        val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Version.retrofit_log}"
        val okHttp3 = "com.squareup.okhttp3:okhttp:3.12.1"
    }

    object Retrofit2 {
        val adapterRxjava2 = "com.squareup.retrofit2:adapter-rxjava2:${Version.retrofit}"
        val converterGson = "com.squareup.retrofit2:converter-gson:${Version.retrofit}"
        val retrofit = "com.squareup.retrofit2:retrofit:${Version.retrofit}"
    }

    object ButterKnife {
        val butterKnife = "com.jakewharton:butterknife:${Version.butterknife}"
        val annotationProcessor = "com.jakewharton:butterknife-compiler:${Version.butterknife}"
    }

    object AndroidX {
        val fragment = "androidx.fragment:fragment:${Version.androidx}"
        val annotation = "androidx.annotation:annotation:${Version.androidx}"
        val core = "androidx.core:core:${Version.androidx}"
        val constraintlayout = "androidx.constraintlayout:constraintlayout:${Version.androidx_112}"
        val materialDesign = "com.google.android.material:material:${Version.materialDesign}"
        val support_recyclerview_v7 = "androidx.recyclerview:recyclerview:${Version.recyclerView}"
        val appcompat = "androidx.appcompat:appcompat:${Version.androidx_100beta01}"
        val vectordrawable = "androidx.vectordrawable:vectordrawable:${Version.androidx_100beta01}"
    }

    object Room {
        val runtime = "androidx.room:room-runtime:${Version.room}"
        val rxjava2 = "androidx.room:room-rxjava2:${Version.room}"
        val annotationProcessor = "androidx.room:room-compiler:${Version.room}"
    }

    object RxJava {
        val rxAndroid = "io.reactivex.rxjava2:rxandroid:${Version.rxAndroid}"
        val rxjava2 = "io.reactivex.rxjava2:rxjava:${Version.rx}"
        val rxrelay2 = "com.jakewharton.rxrelay2:rxrelay:${Version.rxRelay}"
        val rxBinding = "com.jakewharton.rxbinding2:rxbinding:${Version.rxBinding}"
    }

    object Dagger {
        val dagger2 = "com.google.dagger:dagger:${Version.dagger2}"
        val daggerAndroid = "com.google.dagger:dagger-android:${Version.dagger2}"
        val daggerAndroidSupport = "com.google.dagger:dagger-android-support:${Version.dagger2}"
        val processor = "com.google.dagger:dagger-android-processor:${Version.dagger2}"
        val compiler = "com.google.dagger:dagger-compiler:${Version.dagger2}"
    }

    object Test {
        val test_junit = "junit:junit:${Version.junit}"
        val android_test_espresso_core =
                "com.android.support.test.espresso:espresso-core:${Version.espresso}"
        val android_test_espresso_contrib =
                "com.android.support.test.espresso:espresso-contrib:${Version.espresso}"
        val android_test_room = "android.arch.persistence.room:testing:${Version.room}"
        val android_test_rules = "com.android.support.test:rules:${Version.rules}"
        val android_test_runner = "com.android.support.test:runner:${Version.runner}"
    }

    object Analytics {
        val claverTap = "com.clevertap.android:clevertap-android-sdk:${Version.claverTapVersion}"
    }

    object Support {
        val supportV4 = "com.android.support:support-v4:${Version.supportLib}"
    }

    object Firebase {
        val firebaseCore = "com.google.firebase:firebase-core:${Version.firebaseVersion}"
        val firebasePerf = "com.google.firebase:firebase-perf:${Version.firebasePerfVersion}"
        val firebaseMessaging =
                "com.google.firebase:firebase-messaging:${Version.firebaseMessagingVersion}"
        val crashlytics = "com.crashlytics.sdk.android:crashlytics:${Version.crashlyticsVersion}"
    }

    object Lottie {
        val lottie = "com.airbnb.android:lottie:${Version.lottieVersion}"
    }

    object PlayServices {
        val location = "com.google.android.gms:play-services-location:${Version.playServices}"
        val gcm = "com.google.android.gms:play-services-gcm:${Version.playServices}"
        val places = "com.google.android.gms:play-services-places:${Version.playServices}"
        val playService = "com.google.android.gms:play-services-base:${Version.playServices}"
        val placesApi = "com.google.android.libraries.places:places:${Version.placesApiVersion}"
    }

    val javax = "javax.inject:javax.inject:${Version.javaxInject}"
    val javaxjsr250 = "javax.annotation:jsr250-api:${Version.javaxAnnotation}"
    val parceler = "org.parceler:parceler-api:${Version.parcelerVersion}"
    val parcelerProcessor = "org.parceler:parceler-api:${Version.parcelerVersion}"

    object CloveRock {
        val cloveRock = "com.caverock:androidsvg:${Version.cloveRockSVGVersion}"
    }

    //android security
    val security = "androidx.security:security-crypto:${Version.security}"

    //MQTT
    object MQTT {
        val mqttClient = "org.eclipse.paho:org.eclipse.paho.client.mqttv3:${Version.mqttClient}"
        val mqttService = "org.eclipse.paho:org.eclipse.paho.android.service:${Version.mqttService}"
    }

    //FCM
    object FCM {
        val fcmMessaging = "com.google.firebase:firebase-messaging:${Version.fcmMessaging}"
    }
}
