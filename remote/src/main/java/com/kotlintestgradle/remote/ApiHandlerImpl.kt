package com.kotlintestgradle.remote

import android.os.Build
import android.text.TextUtils
import android.util.ArrayMap
import com.kotlintestgradle.remote.RemoteConstants.ADDRESS_ID
import com.kotlintestgradle.remote.RemoteConstants.CART_ID
import com.kotlintestgradle.remote.RemoteConstants.CURR_CODE
import com.kotlintestgradle.remote.RemoteConstants.CURR_SYMB
import com.kotlintestgradle.remote.RemoteConstants.DELIVERY_ADDRESS_ID
import com.kotlintestgradle.remote.RemoteConstants.DRIVER_ID
import com.kotlintestgradle.remote.RemoteConstants.ECOM_CAT_ID
import com.kotlintestgradle.remote.RemoteConstants.IP_ADDRESS
import com.kotlintestgradle.remote.RemoteConstants.ITEM_ID
import com.kotlintestgradle.remote.RemoteConstants.LOGIN_TYPE
import com.kotlintestgradle.remote.RemoteConstants.NUM
import com.kotlintestgradle.remote.RemoteConstants.ORDER_ID
import com.kotlintestgradle.remote.RemoteConstants.PACK_ID
import com.kotlintestgradle.remote.RemoteConstants.PARENT_PRODUCT_ID
import com.kotlintestgradle.remote.RemoteConstants.PLATFORM
import com.kotlintestgradle.remote.RemoteConstants.PRODUCT_ID
import com.kotlintestgradle.remote.RemoteConstants.PRODUCT_ORDER_ID
import com.kotlintestgradle.remote.RemoteConstants.RETAILER_LOGIN_TYPE
import com.kotlintestgradle.remote.RemoteConstants.STORE_CAT
import com.kotlintestgradle.remote.RemoteConstants.STORE_CATEGORY_ID
import com.kotlintestgradle.remote.RemoteConstants.TYPE
import com.kotlintestgradle.remote.RemoteConstants.USER_ID
import com.kotlintestgradle.remote.RemoteConstants.USER_ID_KEY
import com.kotlintestgradle.remote.RemoteConstants.USER_TYPE
import com.kotlintestgradle.remote.RemoteConstants.ZONE_ID_KEY
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
import java.util.*

/**
 * @author 3Embed
 *used for retrofit
 * @since 1.0 (23-Aug-2019)
 */
class ApiHandlerImpl(
  private var restApi: RestApi,
  private val deviceId: String
) : ApiHandler {
  override fun getLogin(lan: String?, request: LoginRequest): Single<LoginResponse> {
    return restApi.loginAPI(lan, request)
  }

  override fun getUsersList(lan: String?, authToken: String?): Single<UsersListResponse> {
    return restApi.getUsersList(lan, authToken)
  }

  override fun getParticipantsInCall(
    lan: String?,
    authToken: String?,
    callId: String
  ): Single<ParticipantsListResponse> {
    return restApi.getParticipantsList(lan, authToken, callId)
  }

  override fun postCall(
    lan: String?,
    authToken: String?,
    callingRequest: PostCallRequest
  ): Single<CallingResponse> {
    return restApi.postCall(lan, authToken, BuildConfig.FCM_SERVER_KEY, callingRequest)
  }

  override fun answerCall(
    lan: String?,
    authToken: String?,
    callId: String,
    sessionId: String
  ): Single<CallingResponse> {
    return restApi.answerCall(lan, authToken, callId, sessionId)
  }

  override fun inviteMembers(
    lan: String?,
    authToken: String?,
    callId: String,
    inviteMember: String
  ): Single<CallingResponse> {
    return restApi.inviteMembers(lan, authToken, BuildConfig.FCM_SERVER_KEY, callId, inviteMember)
  }

  override fun disconnectCall(
    lan: String?,
    authToken: String?,
    callId: String,
    callFrom: String
  ): Single<CallingResponse> {
    return restApi.disconnectCall(lan, authToken, callId, callFrom)
  }

  override fun getHelp(): Single<HelpListDetails> {
    return restApi.getHelp(
      Locale.getDefault().language,
      PLATFORM, CURR_SYMB, CURR_CODE
    )
  }

  override fun addProductToCart(
    token: String,
    request: AddProductToCartRequest
  ): Single<CommonModel> {
    return restApi.addProductToCart(
      token, Locale.getDefault().language,
      PLATFORM, CURR_SYMB, CURR_CODE, request
    )
  }

  override fun deleteAddress(token: String, addressID: String?): Single<CommonModel> {
    var userIdMapper: ArrayMap<String, String>? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      userIdMapper = ArrayMap()
      userIdMapper[ADDRESS_ID] = addressID
    }
    return restApi.deleteAddress(
      token,
      Locale.getDefault().language,
      PLATFORM,
      CURR_SYMB,
      CURR_CODE,
      userIdMapper!!
    )
  }

  override fun getAddress(header: String, userId: String): Single<AddressDetails> {
    var userIdMapper: ArrayMap<String?, String?>? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      userIdMapper = ArrayMap()
      userIdMapper[USER_ID] = userId
    }
    return restApi.getAddress(
      header,
      Locale.getDefault().language,
      PLATFORM,
      CURR_SYMB,
      CURR_CODE,
      userIdMapper
    )
  }

  override fun editAddress(token: String, request: AddAddressRequest): Single<CommonModel> {
    return restApi.editAddress(
      token,
      Locale.getDefault().language,
      PLATFORM,
      CURR_SYMB,
      CURR_CODE,
      request
    )
  }

  override fun addAddress(token: String, request: AddAddressRequest): Single<CommonModel> {
    return restApi.addAddress(
      token,
      Locale.getDefault().language,
      PLATFORM,
      CURR_SYMB,
      CURR_CODE,
      request
    )
  }

  override fun makeAddressDefault(
    token: String,
    request: MakeAddressDefaultRequest
  ): Single<CommonModel> {
    return restApi.makeDefaultAddress(
      token,
      Locale.getDefault().language,
      PLATFORM,
      CURR_SYMB,
      CURR_CODE,
      request
    )
  }

  override fun calculateDeliveryFee(
    header: String,
    cartId: String?,
    deliveryAddressId: String
  ): Single<DeliveryData> {
    var userIdMapper: ArrayMap<String, String>? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      userIdMapper = ArrayMap()
      userIdMapper[CART_ID] = cartId
      userIdMapper[DELIVERY_ADDRESS_ID] = deliveryAddressId
    }
    return restApi.calculateDeliveryFee(
      header,
      Locale.getDefault().language,
      PLATFORM,
      CURR_SYMB,
      CURR_CODE,
      userIdMapper
    )
  }

  override fun getProductDetails(
    header: String, latitude: Double, longitude: Double, ipAddress: String,
    city: String, country: String, pdpRequest: PdpRequest
  ): Single<ProductDetails?>? {
    var hashReq: ArrayMap<String?, String?>? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      hashReq = ArrayMap()
    }
    assert(hashReq != null)
    hashReq!![PRODUCT_ID] = pdpRequest.getProductId()
    hashReq[PARENT_PRODUCT_ID] = pdpRequest.getParentProductId()
    return restApi.getProductDetails(
      header,
      Locale.getDefault().language, ipAddress,
      latitude, longitude, PLATFORM, city, country, hashReq
    )
  }

  override fun notifyProductAvailability(
    token: String?,
    request: NotifyProductAvailabilityRequest?
  ): Single<CommonModel?>? {
    return restApi.notifyProductAvailability(token, request)
  }

  override fun updateCart(
    token: String, request: UpdateCartRequest?, currencySymbol: String,
    currencyCode: String
  ): Single<CommonModel?>? {
    return restApi.updateCart(
      token, Locale.getDefault().language,
      PLATFORM, currencySymbol, currencyCode, request
    )
  }

  override fun getCartProducts(
    isDine: Boolean, token: String, userId: String?,
    currSymbol: String, currencyCode: String
  ): Single<CartDetails?>? {
    val map = HashMap<String, Any>()
    map[STORE_CATEGORY_ID] = ECOM_CAT_ID
    return restApi.getCartProducts(
      token, Locale.getDefault().language,
      PLATFORM, currSymbol, currencyCode, map
    )
  }

  override fun getWalletAmt(
    header: String,
    userId: String,
    userType: String
  ): Single<WalletDataDetails?>? {
    val map = HashMap<String, Any>()
    map[USER_ID] = userId
    map[USER_TYPE] = userType
    return restApi.getWalletDetailsApi(header, Locale.getDefault().language, map)
  }

  override fun placeOrder(
    token: String, request: PlaceOrderRequest?,
    currSymbol: String, currencyCode: String
  ): Single<CommonModel?>? {
    return restApi.placeOrder(
      token, Locale.getDefault().language,
      PLATFORM, currSymbol, currencyCode, request
    )
  }

  override fun ipAddressToLocation(ipAddress: String, currencySymbol: String, currencyCode: String):
      Single<IpAddressToLocationDetails?>? {
    var ipAddressMapper: ArrayMap<String?, String?>? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      ipAddressMapper = ArrayMap()
      ipAddressMapper[IP_ADDRESS] = ipAddress
    }
    return restApi.ipAddressToLocation(
      Locale.getDefault().language, PLATFORM,
      currencySymbol, currencyCode, ipAddressMapper
    )
  }

  override fun applyPromoCode(applyPromoCodeRequest: ApplyPromoCodeRequest?): Single<ApplyPromoCodeListData?>? {
    return restApi.applyPromoCodeApi(applyPromoCodeRequest)
  }

  override fun reviewLikeDisLike(
    header: String,
    request: ReviewLikeDisLikeRequest?
  ): Single<CommonModel?>? {
    return restApi.reviewLikeDisLike(header, Locale.getDefault().language, request)
  }

  override fun addProductToWishList(
    header: String,
    request: AddToWishListRequest?
  ): Single<CommonModel?>? {
    return restApi.addProductToWishList(
      header, Locale.getDefault().language,
      request
    )
  }

  override fun deleteWishListProduct(
    header: String,
    request: AddToWishListRequest?
  ): Single<CommonModel?>? {
    return restApi.removeProductFromWishList(
      header, ECOM_CAT_ID,
      Locale.getDefault().language, request
    )
  }

  override fun rateAndReviewProduct(
    token: String,
    reviewRequest: ProductRateAndReviewRequest?, ipAddress: String,
    latitude: Double, longitude: Double
  ): Single<CommonModel?>? {
    return restApi.reviewProduct(
      token, Locale.getDefault().language,
      ipAddress, latitude, longitude, reviewRequest
    )
  }

  override fun getRatableAttributes(
    token: String,
    storeCatId: String?,
    driverId: String?,
    orderId: String?,
    productId: String?
  ): Single<RatableDetails?>? {
    var hashReq: ArrayMap<String?, String?>? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      hashReq = ArrayMap()
    }
    assert(hashReq != null)
    if (productId != null && !productId.isEmpty()) hashReq!![PRODUCT_ID] = productId
    if (driverId != null && !driverId.isEmpty()) hashReq!![DRIVER_ID] = driverId
    if (orderId != null && !orderId.isEmpty()) hashReq!![ORDER_ID] = orderId
    return restApi.getRatableProducts(token, Locale.getDefault().language, storeCatId, hashReq)
  }

  override fun getSellerList(
    header: String,
    request: GetSellerListRequest
  ): Single<SellerDetails?>? {
    var hashReq: ArrayMap<String?, String?>? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      hashReq = ArrayMap()
    }
    assert(hashReq != null)
    hashReq!![PRODUCT_ID] = request.getProductId()
    hashReq[PARENT_PRODUCT_ID] = request.getParentProductId()
    return restApi.getSellerList(
      header, Locale.getDefault().language,
      RETAILER_LOGIN_TYPE, hashReq
    )
  }

  override fun getSimilarProducts(
    header: String, itemId: String?, limit: Int?,
    loginType: Int, zoneId: String?, userId: String?
  ): Single<GetSimilarProducts?>? {
    var hashReq: ArrayMap<String?, Any?>? = null
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      hashReq = ArrayMap()
    }
    assert(hashReq != null)
    hashReq!![ITEM_ID] = itemId
    hashReq[STORE_CAT] = ECOM_CAT_ID
    hashReq[NUM] = limit
    hashReq[LOGIN_TYPE] = loginType
    hashReq[ZONE_ID_KEY] = zoneId
    hashReq[USER_ID_KEY] = userId
    return restApi.getSimilarProducts(hashReq)
  }

  override fun clearAllWishListItems(header: String): Single<CommonModel?>? {
    return restApi.clearAllWishListItems(header)
  }

  override fun getOrderHistory(
    platform: Int,
    token: String?,
    lang: String?,
    currency: String?,
    currencyCode: String,
    request: GetOrderHistoryRequest?
  ): Single<OrderHistoryDetails?>? {
    return restApi.getOrderHistory(
      token, lang,
      platform, currency, currencyCode,
      request!!.query
    )
  }

  override fun cancelOrder(
    platform: Int,
    token: String?,
    lang: String?,
    currency: String?,
    currencyCode: String,
    request: CancelOrderRequest?
  ): Single<CommonModel?>? {
    return restApi.cancelOrder(
      token, lang,
      platform, currency, currencyCode, request
    )
  }

     override fun getOrderDetails(
         platform:Int,
         token: String?,
         lang: String?,
         type: String?,
         currency: String?,
         currencyCode: String,
         orderId: String?
     ): Single<MasterOrderDetails?>? {
         val map =
             HashMap<String, Any>()
         map[TYPE] = type!!
         map[ORDER_ID] = orderId!!
         return restApi.getOrderDetails(
             token, lang,
             platform, currency, currencyCode, map
         )
     }

  override fun getMasterOrderDetail(
    platform: Int,
    token: String?,
    lang: String?,
    currency: String?,
    currencyCode: String,
    type: String?,
    orderId: String?
  ): Single<MasterOrdersDetail?>? {
    val map =
      HashMap<String?, Any?>()
    map[TYPE] = type
    map[ORDER_ID] = orderId
    return restApi.getMasterOrderDetail(
      token, lang,
      platform, currency, currencyCode, map
    )
  }

  override fun getStoreOrderDetail(
    platform: Int,
    token: String?,
    lang: String?,
    currency: String?,
    currencyCode: String,
    type: String?,
    orderId: String?
  ): Single<StoreOrdersItem?>? {
    val map =
      HashMap<String, Any>()
    map[TYPE] = type!!
    map[ORDER_ID] = orderId!!
    return restApi.getStoreOrderDetail(
      token, lang,
      platform, currency, currencyCode, map
    )
  }

  override fun getDeliveryStatus(
    platform: Int,
    token: String?,
    lang: String?,
    currency: String?,
    currencyCode: String,
    productOrderId: String?
  ): Single<TrackingListOrderStatusData?>? {
    val map =
      HashMap<String, Any>()
    map[ORDER_ID] = productOrderId!!
    return restApi.getTrackingDetails(
      token, lang,
      platform, currency, currencyCode, map
    )
  }

  override fun getPackageDetails(
    platform: Int,
    token: String?,
    lang: String?,
    currency: String?,
    currencyCode: String,
    packId: String?,
    productOrderId: String?
  ): Single<OrderDetails?>? {
    val map =
      HashMap<String, Any>()
    map[PRODUCT_ORDER_ID] = productOrderId!!
    map[PACK_ID] = packId!!
    return restApi.getPackageDetails(
      token, lang,
      platform, currency, currencyCode, map
    )
  }

  override fun getWishList(
    header: String,
    sortType: Int,
    searchQuery: String?
  ): Single<WishListDetails?>? {
    return if (TextUtils.isEmpty(searchQuery)) {
      restApi.getWishList(
        header, ECOM_CAT_ID, Locale.getDefault().language,
        sortType
      )
    } else {
      restApi.getWishList(
        header, ECOM_CAT_ID, Locale.getDefault().language,
        sortType, searchQuery
      )
    }
  }
}