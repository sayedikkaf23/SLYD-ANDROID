package com.jio.consumer.domain.interactor.user.handler

import com.kotlintestgradle.model.ecom.UserData
import com.kotlintestgradle.model.ecom.common.ProductsData
import com.kotlintestgradle.model.ecom.getcart.CartData
import com.kotlintestgradle.repository.UserRepository
import io.reactivex.Observable
import io.reactivex.observables.ConnectableObservable
import javax.inject.Inject

/**
 * Created by 3Embed on 29/03/19.
 */
class UserHandlerImpl @Inject constructor(private var userRepository: UserRepository) :
    UserHandler {
    override fun isLogged(): Boolean {
        return userRepository.isLogged()
    }

    override fun getUserId(): String {
        return userRepository.getUserId()
    }

    override fun getMQTTTopic(): String {
        return userRepository.getMQTTTopic()
    }

    override fun getWillTopic(): String {
        return userRepository.getWillopic()
    }

    override fun getFcmTopic(): String {
        return userRepository.getFcmTopic()
    }

    override fun setAuthToken(authToken: String) {
        userRepository.setAuthToken(authToken)
    }

    override fun setLanguageCode(lang: String) {
        userRepository.setLanguageCode(lang)
    }

    override fun setUserId(userId: String) {
        userRepository.setUserId(userId)
    }

    override fun getUserDataObservable(): ConnectableObservable<UserData> {
        return userRepository.getUserDataObservable()
    }

    override fun getCartDataObservable(): ConnectableObservable<CartData?>? {
        return userRepository.getCartDataObservable()
    }

    override fun getWishListObservable(): Observable<ProductsData?>? {
        return userRepository.getWishListObservable()
    }
}