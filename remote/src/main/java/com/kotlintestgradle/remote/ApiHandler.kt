package com.kotlintestgradle.remote

import com.kotlintestgradle.remote.model.common.Header
import com.kotlintestgradle.remote.model.request.LoginRequest
import com.kotlintestgradle.remote.model.request.PostCallRequest
import com.kotlintestgradle.remote.model.request.ecom.AddAddressRequest
import com.kotlintestgradle.remote.model.request.ecom.AddProductToCartRequest
import com.kotlintestgradle.remote.model.request.ecom.AddToWishListRequest
import com.kotlintestgradle.remote.model.request.ecom.ApplyPromoCodeRequest
import com.kotlintestgradle.remote.model.request.ecom.CancelOrderRequest
import com.kotlintestgradle.remote.model.request.ecom.GetOrderHistoryRequest
import com.kotlintestgradle.remote.model.request.ecom.GetSellerListRequest
import com.kotlintestgradle.remote.model.request.ecom.MakeAddressDefaultRequest
import com.kotlintestgradle.remote.model.request.ecom.NotifyProductAvailabilityRequest
import com.kotlintestgradle.remote.model.request.ecom.PdpRequest
import com.kotlintestgradle.remote.model.request.ecom.PlaceOrderRequest
import com.kotlintestgradle.remote.model.request.ecom.ProductRateAndReviewRequest
import com.kotlintestgradle.remote.model.request.ecom.ReviewLikeDisLikeRequest
import com.kotlintestgradle.remote.model.request.ecom.UpdateCartRequest
import com.kotlintestgradle.remote.model.response.calling.CallingResponse
import com.kotlintestgradle.remote.model.response.dashboard.ParticipantsListResponse
import com.kotlintestgradle.remote.model.response.dashboard.UsersListResponse
import com.kotlintestgradle.remote.model.response.ecom.CommonModel
import com.kotlintestgradle.remote.model.response.ecom.deliveryfee.DeliveryData
import com.kotlintestgradle.remote.model.response.ecom.getaddress.AddressDetails
import com.kotlintestgradle.remote.model.response.ecom.getcart.CartDetails
import com.kotlintestgradle.remote.model.response.ecom.getratable.RatableDetails
import com.kotlintestgradle.remote.model.response.ecom.location.IpAddressToLocationDetails
import com.kotlintestgradle.remote.model.response.ecom.pdp.ProductDetails
import com.kotlintestgradle.remote.model.response.ecom.promocode.ApplyPromoCodeListData
import com.kotlintestgradle.remote.model.response.ecom.sellerlist.SellerDetails
import com.kotlintestgradle.remote.model.response.ecom.similarproduct.GetSimilarProducts
import com.kotlintestgradle.remote.model.response.ecom.wallet.WalletDataDetails
import com.kotlintestgradle.remote.model.response.ecom.wishlist.WishListDetails
import com.kotlintestgradle.remote.model.response.help.HelpListDetails
import com.kotlintestgradle.remote.model.response.login.LoginResponse
import com.kotlintestgradle.remote.model.response.orderdetails.MasterOrderDetails
import com.kotlintestgradle.remote.model.response.orderdetails.OrderDetails
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.MasterOrdersDetail
import com.kotlintestgradle.remote.model.response.orderdetails.pharmacy.StoreOrdersItem
import com.kotlintestgradle.remote.model.response.orderhistory.OrderHistoryDetails
import com.kotlintestgradle.remote.model.response.tracking.TrackingListOrderStatusData
import io.reactivex.Single

/**
 * @author 3Embed

 * @since 1.0 (23-Aug-2019)
 */
interface ApiHandler {
    fun getLogin(lan: String?, request: LoginRequest): Single<LoginResponse>
    fun getUsersList(lan: String?, authToken: String?): Single<UsersListResponse>
    fun getParticipantsInCall(
        lan: String?,
        authToken: String?,
        callId: String
    ): Single<ParticipantsListResponse>

    fun postCall(
        lan: String?,
        authToken: String?,
        callRequest: PostCallRequest
    ): Single<CallingResponse>

    fun answerCall(
        lan: String?,
        authToken: String?,
        callId: String,
        sessionId: String
    ): Single<CallingResponse>

    fun inviteMembers(
        lan: String?,
        authToken: String?,
        callId: String,
        inviteMembers: String
    ): Single<CallingResponse>

    fun disconnectCall(
        lan: String?,
        authToken: String?,
        callId: String,
        callFrom: String
    ): Single<CallingResponse>

    fun getHelp(): Single<HelpListDetails>

    fun addProductToCart(token: String, request: AddProductToCartRequest): Single<CommonModel>

    fun deleteAddress(token: String, addressID: String?): Single<CommonModel>

    fun getAddress(token: String, userId: String): Single<AddressDetails>

    fun editAddress(token: String, request: AddAddressRequest): Single<CommonModel>

    fun addAddress(header: String, request: AddAddressRequest): Single<CommonModel>

    fun makeAddressDefault(header: String, request: MakeAddressDefaultRequest): Single<CommonModel>

    fun calculateDeliveryFee(header: String, cartId: String?, deliveryAddressId: String): Single<DeliveryData>

    fun getProductDetails(header: String, latitude: Double, longitude: Double, ipAddress: String,
                          city: String, country: String, pdpRequest: PdpRequest): Single<ProductDetails?>?

    fun notifyProductAvailability(token: String?,
                                  request: NotifyProductAvailabilityRequest?): Single<CommonModel?>?

    fun updateCart(header: String, request: UpdateCartRequest?, currencySymbol: String,
                   currencyCode: String): Single<CommonModel?>?

    fun getCartProducts(isDine: Boolean, token: String, userId: String?,
                        currSymbol: String, currencyCode: String): Single<CartDetails?>?

    fun getWalletAmt(header: String, userId: String, userType: String): Single<WalletDataDetails?>?

    fun placeOrder(header: String, request: PlaceOrderRequest?, currSymbol: String, currencyCode: String): Single<CommonModel?>?

    fun ipAddressToLocation(ipAddress: String, currencySymbol: String, currencyCode: String): Single<IpAddressToLocationDetails?>?

    fun applyPromoCode(applyPromoCodeRequest: ApplyPromoCodeRequest?): Single<ApplyPromoCodeListData?>?

    fun reviewLikeDisLike(header: String, request: ReviewLikeDisLikeRequest?): Single<CommonModel?>?

    fun addProductToWishList(header: String, request: AddToWishListRequest?): Single<CommonModel?>?

    fun deleteWishListProduct(header: String,
                              request: AddToWishListRequest?): Single<CommonModel?>?

    fun rateAndReviewProduct(header: String,
                             reviewRequest: ProductRateAndReviewRequest?, ipAddress: String,
                             latitude: Double, longitude: Double): Single<CommonModel?>?

    fun getRatableAttributes(header: String, storeCatId: String?, driverId: String?, orderId: String?, productId: String?): Single<RatableDetails?>?

    fun getSellerList(header: String, request: GetSellerListRequest): Single<SellerDetails?>?

    fun getSimilarProducts(header: String, itemId: String?, limit: Int?,
                           loginType: Int, zoneId: String?, userId: String?): Single<GetSimilarProducts?>?

    fun getWishList(header: String, sortType: Int, searchQuery: String?): Single<WishListDetails?>?

    fun clearAllWishListItems(header: String): Single<CommonModel?>?

    fun getOrderHistory(
        platform:Int,
        token: String?,
        lang: String?,
        currency: String?,
        currencyCode: String,
        request: GetOrderHistoryRequest?
    ): Single<OrderHistoryDetails?>?

    fun cancelOrder(
        platform:Int,
        token: String?,
        lang: String?,
        currency: String?,
        currencyCode: String,
        request: CancelOrderRequest?
    ): Single<CommonModel?>?

    fun getOrderDetails(
        platform:Int,
        token: String?,
        lang: String?,
        type: String?,
        currency: String?,
        currencyCode: String,
        orderId: String?
    ): Single<MasterOrderDetails?>?

    fun getMasterOrderDetail(
        platform:Int,
        token: String?,
        lang: String?,
        currency: String?,
        currencyCode: String,
        type: String?,
        orderId: String?
    ): Single<MasterOrdersDetail?>?

    fun getStoreOrderDetail(
        platform:Int,
        token: String?,
        lang: String?,
        currency: String?,
        currencyCode: String,
        type: String?,
        orderId: String?
    ): Single<StoreOrdersItem?>?
    fun getDeliveryStatus(
        platform: Int,
        token: String?,
        lang: String?,
        currency: String?,
        currencyCode: String,
        productOrderId: String?
    ): Single<TrackingListOrderStatusData?>?
    fun getPackageDetails(
        platform: Int,
        token: String?,
        lang: String?,
        currency: String?,
        currencyCode: String,
        packId: String?,
        productOrderId: String?
    ): Single<OrderDetails?>?
}