package com.kotlintestgradle.data.interactor

import com.kotlintestgradle.data.preference.PreferenceManager
import com.kotlintestgradle.remote.RemoteInteractor

/**
 * @author 3Embed
 * @since 1.0 (23-Aug-2019)
 */
class RemoteInteractorImpl(private val preferenceManager: PreferenceManager) : RemoteInteractor {
    override fun clearToken() {
        preferenceManager.clearPreference()
    }
}