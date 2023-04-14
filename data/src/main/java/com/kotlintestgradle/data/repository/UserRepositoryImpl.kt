package com.kotlintestgradle.data.repository

import com.kotlintestgradle.data.DataSource
import com.kotlintestgradle.data.preference.PreferenceManager
import com.kotlintestgradle.data.repository.observable.CartDataObservable
import com.kotlintestgradle.data.repository.observable.UserDataObservable
import com.kotlintestgradle.data.repository.observable.WishListObservable
import com.kotlintestgradle.model.ecom.UserData
import com.kotlintestgradle.model.ecom.common.ProductsData
import com.kotlintestgradle.model.ecom.getcart.CartData
import com.kotlintestgradle.repository.UserRepository
import io.reactivex.Observable
import io.reactivex.observables.ConnectableObservable
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val dataSource: DataSource) :
    BaseRepository(dataSource), UserRepository {
    override fun getMQTTTopic(): String {
        return preference.mqttTopic!!
    }

    override fun getWillopic(): String {
        return preference.willTopic!!
    }

    override fun getFcmTopic(): String {
        return preference.fcmTopic!!
    }

    override fun setAuthToken(authToken: String) {
        preference.authToken = authToken
        println(" token stored in preference " + preference.authToken)
    }

    override fun setLanguageCode(lang: String) {
        preference.languageCode = lang
    }

    override fun getUserId(): String {
        return preference.userId!!
    }

    private val preference: PreferenceManager = dataSource.preference()

    override fun isLogged(): Boolean {
        return preference.isLoggedIn
    }

    override fun setUserId(userId: String) {
        preference.userId = userId
    }

    override fun getUserDataObservable(): ConnectableObservable<UserData> {
        UserDataObservable.getInstance()
        return UserDataObservable.getObservable()
    }


    override fun getCartDataObservable(): ConnectableObservable<CartData?>? {
        CartDataObservable.getInstance()
        return CartDataObservable.getObservable()
    }

    override fun getWishListObservable(): Observable<ProductsData?>? {
        return WishListObservable.getInstance()
    }
}