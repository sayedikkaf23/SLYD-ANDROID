//package com.appscrip.myapplication
//
//import android.app.Activity
//import android.app.Application
//import android.content.Context
//import android.util.Log
//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.LifecycleObserver
//import androidx.lifecycle.OnLifecycleEvent
//import androidx.lifecycle.ProcessLifecycleOwner
//import com.appscrip.myapplication.injection.component.DaggerAppComponent
//import com.clevertap.android.sdk.ActivityLifecycleCallback
//import com.facebook.stetho.Stetho
//import dagger.android.AndroidInjector
//import dagger.android.DispatchingAndroidInjector
//import dagger.android.HasActivityInjector
//import javax.inject.Inject
//
///**
// * @author 3Embed
// * This activity is used for  initialization of global state before the first Activity is displayed.
// *@since 1.0 (23-Aug-2019)
// *
// *
// */
//class BaseApplication : Application(), HasActivityInjector, LifecycleObserver {
//    @Inject
//    internal lateinit var activityInjector: DispatchingAndroidInjector<Activity>
//    var appInForeground: Boolean = false
//    //@Inject lateinit var fcmHandler: FcmHandler
//
//    override fun onCreate() {
//        ActivityLifecycleCallback.register(this) // for handling activity lifecycle events
//        super.onCreate()
//
//        instance = this
//        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
//        inject() // used to inject dagger
//        //fcmHandler.initializeFcm() // used to initialize the fcm
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
//    fun onAppBackgrounded() {
//        Log.d(TAG.toString(), "App in background")
//        appInForeground = false
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_START)
//    fun onAppForegrounded() {
//        Log.d(TAG.toString(), "App in foreground")
//        appInForeground = true
//    }
//
//    override fun attachBaseContext(base: Context) {
//        super.attachBaseContext(base)
//        //MultiDex.install(this@BaseApplication)
//    }
//
//    fun inject() {
//        DaggerAppComponent.builder().application(this).build().inject(this)
//        // used for debug mode
//        if (BuildConfig.DEBUG) {
//            // Initialization and configuration entry point for the Stetho debugging system.
//            Stetho.initializeWithDefaults(this)
//        }
//    }
//
//    // Performs members-injection for a concrete subtype
//    override fun activityInjector(): AndroidInjector<Activity> {
//        return activityInjector
//    }
//
//    companion object {
//        private val TAG = BaseApplication::class.java
//        lateinit var instance: BaseApplication
//            private set
//    }
//}