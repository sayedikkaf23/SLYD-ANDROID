package com.appscrip.myapplication

import com.kotlintestgradle.executor.PostExecutionThread
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 *
 * @author 3Embed
 * used to run code to main thread
 * @since 1.0 (23-Aug-2019)
 *
 */

open class UiThread @Inject constructor() : PostExecutionThread {
    override val scheduler: Scheduler = AndroidSchedulers.mainThread()
}
