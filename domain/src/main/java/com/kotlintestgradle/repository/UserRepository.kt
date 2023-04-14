package com.kotlintestgradle.repository

import com.kotlintestgradle.model.ecom.UserData
import com.kotlintestgradle.model.ecom.common.ProductsData
import com.kotlintestgradle.model.ecom.getcart.CartData
import io.reactivex.Observable
import io.reactivex.observables.ConnectableObservable

interface UserRepository {
    fun isLogged(): Boolean
    fun getUserId(): String
    fun getMQTTTopic(): String
    fun getWillopic(): String
    fun getFcmTopic(): String
    fun setAuthToken(authToken: String)
    fun setLanguageCode(lang: String)
    fun setUserId(userId: String)
    fun getUserDataObservable(): ConnectableObservable<UserData>
    fun getCartDataObservable(): ConnectableObservable<CartData?>?
    fun getWishListObservable(): Observable<ProductsData?>?
}