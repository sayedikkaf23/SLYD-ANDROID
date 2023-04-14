package com.jio.consumer.domain.interactor.user.handler

import com.kotlintestgradle.model.ecom.UserData
import com.kotlintestgradle.model.ecom.common.ProductsData
import com.kotlintestgradle.model.ecom.getcart.CartData
import io.reactivex.Observable
import io.reactivex.observables.ConnectableObservable

/**
 * Created by 3Embed on 29/03/19.
 */
interface UserHandler {
    fun isLogged(): Boolean
    fun getUserId(): String?
    fun getMQTTTopic(): String
    fun getWillTopic(): String?
    fun getFcmTopic(): String
    fun setAuthToken(authToken: String)
    fun setLanguageCode(lang: String)
    fun setUserId(userId: String)
    fun getUserDataObservable(): Observable<UserData>
    fun getCartDataObservable(): ConnectableObservable<CartData?>?
    fun getWishListObservable(): Observable<ProductsData?>?
}