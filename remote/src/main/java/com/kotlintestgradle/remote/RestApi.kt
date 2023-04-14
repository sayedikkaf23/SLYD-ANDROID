package com.kotlintestgradle.remote

import android.util.ArrayMap
import com.kotlintestgradle.remote.model.request.LoginRequest
import com.kotlintestgradle.remote.model.request.PostCallRequest
import com.kotlintestgradle.remote.model.request.ecom.AddAddressRequest
import com.kotlintestgradle.remote.model.request.ecom.AddProductToCartRequest
import com.kotlintestgradle.remote.model.request.ecom.AddToWishListRequest
import com.kotlintestgradle.remote.model.request.ecom.ApplyPromoCodeRequest
import com.kotlintestgradle.remote.model.request.ecom.CancelOrderRequest
import com.kotlintestgradle.remote.model.request.ecom.MakeAddressDefaultRequest
import com.kotlintestgradle.remote.model.request.ecom.NotifyProductAvailabilityRequest
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
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import retrofit2.http.QueryMap
import java.util.*

/**
 * /**
 * @author 3Embed
 *used for retrofit api
 * @since 1.0 (23-Aug-2019)
*/
 */
interface RestApi {
    @POST("/signIn")
    @Headers("Content-Type: application/json;charset=UTF-8")
    fun loginAPI(
        @retrofit2.http.Header("lan") token: String?,
        @Body request: LoginRequest
    ): Single<LoginResponse>

    @GET("/users")
    @Headers("Content-Type: application/json;charset=UTF-8")
    fun getUsersList(
        @retrofit2.http.Header("lan") token: String?,
        @retrofit2.http.Header("authorization") authorization: String?
    ): Single<UsersListResponse>

    @POST("/call")
    @Headers("Content-Type: application/json;charset=UTF-8")
    fun postCall(
        @retrofit2.http.Header("lan") token: String?,
        @retrofit2.http.Header("authorization") authorization: String?,
        @retrofit2.http.Header("serverKey") serverKey: String?,
        @Body request: PostCallRequest
    ): Single<CallingResponse>

    @PUT("/call")
    @Headers("Content-Type: application/json;charset=UTF-8")
    fun answerCall(
        @retrofit2.http.Header("lan") token: String?,
        @retrofit2.http.Header("authorization") authorization: String?,
        @Query("callId") callId: String,
        @Query("sessionId") sessionId: String
    ): Single<CallingResponse>

    @PUT("/call/invite")
    fun inviteMembers(
        @retrofit2.http.Header("lan") token: String?,
        @retrofit2.http.Header("authorization") authorization: String?,
        @retrofit2.http.Header("serverKey") serverKey: String?,
        @Query("callId") callId: String,
        @Query("to") to: String
    ): Single<CallingResponse>

    @HTTP(method = "DELETE", path = "/call", hasBody = true)
    fun disconnectCall(
        @retrofit2.http.Header("lan") token: String?,
        @retrofit2.http.Header("authorization") authorization: String?,
        @Query("callId") callId: String,
        @Query("callFrom") callFrom: String
    ): Single<CallingResponse>

    @GET("/call")
    fun getParticipantsList(
        @retrofit2.http.Header("lan") token: String?,
        @retrofit2.http.Header("authorization") authorization: String?,
        @Query("callId") callId: String
    ): Single<ParticipantsListResponse>

    @GET("v1/ecomm/customer/support")
    fun getHelp(
        @Header("language") language: String?,
        @Header("platform") platform: Int,
        @Header("currencysymbol") currencySymbol: String?,
        @Header("currencycode") currencyCode: String?): Single<HelpListDetails>

    @POST("v1/ecomm/cart")
    fun addProductToCart(@Header("authorization") authorization: String?,
                         @Header("language") language: String?,
                         @Header("platform") platform: Int,
                         @Header("currencysymbol") currencySymbol: String?,
                         @Header("currencycode") currencyCode: String?,
                         @Body request: AddProductToCartRequest): Single<CommonModel>

    @DELETE("v1/ecomm/address")
    fun deleteAddress(@Header("authorization") authorization: String?,
                      @Header("language") language: String?,
                      @Header("platform") platform: Int,
                      @Header("currencysymbol") currencySymbol: String?,
                      @Header("currencycode") currencyCode: String?,
                      @QueryMap(encoded = true) userId: ArrayMap<String, String>): Single<CommonModel>

    @GET("v1/ecomm/address")
    fun getAddress(@Header("authorization") authorization: String?,
                   @Header("language") language: String?,
                   @Header("platform") platform: Int,
                   @Header("currencysymbol") currencySymbol: String?,
                   @Header("currencycode") currencyCode: String?,
                   @QueryMap(encoded = true) userId: ArrayMap<String?, String?>?): Single<AddressDetails>

    @PATCH("v1/ecomm/address")
    fun editAddress(@Header("authorization") authorization: String?,
                    @Header("language") language: String?,
                    @Header("platform") platform: Int,
                    @Header("currencysymbol") currencySymbol: String?,
                    @Header("currencycode") currencyCode: String?,
                    @Body request: AddAddressRequest): Single<CommonModel>

    @POST("v1/ecomm/address")
    fun addAddress(@Header("authorization") authorization: String?,
                   @Header("language") language: String?,
                   @Header("platform") platform: Int,
                   @Header("currencysymbol") currencySymbol: String?,
                   @Header("currencycode") currencyCode: String?,
                   @Body request: AddAddressRequest): Single<CommonModel>

    @PATCH("v1/ecomm/defaultAddress")
    fun makeDefaultAddress(@Header("authorization") authorization: String?,
                           @Header("language") language: String?,
                           @Header("platform") platform: Int,
                           @Header("currencysymbol") currencySymbol: String?,
                           @Header("currencycode") currencyCode: String?,
                           @Body request: MakeAddressDefaultRequest): Single<CommonModel>


    @GET("v1/ecomm/delivery/calculate")
    fun calculateDeliveryFee(@Header("authorization") authorization: String?,
                             @Header("language") language: String?,
                             @Header("platform") platform: Int,
                             @Header("currencysymbol") currencySymbol: String?,
                             @Header("currencycode") currencyCode: String?,
                             @QueryMap(encoded = true) cartAddressInfo: ArrayMap<String, String>?): Single<DeliveryData>

    @GET("python/product/details")
    fun getProductDetails(@Header("authorization") authorization: String?,
                          @Header("language") language: String?,
                          @Header("ipAddress") ipAddress: String?,
                          @Header("latitude") latitude: Double,
                          @Header("longitude") longitude: Double,
                          @Header("platform") platform: Int,
                          @Header("city") city: String?,
                          @Header("country") country: String?,
                          @QueryMap(encoded = true) options: ArrayMap<String?, String?>?): Single<ProductDetails?>?

    @POST("python/product/notify")
    fun notifyProductAvailability(@Header("authorization") authorization: String?,
                                  @Body request: NotifyProductAvailabilityRequest?): Single<CommonModel?>?

    @POST("v1/ecomm/cart")
    fun updateCart(@Header("authorization") authorization: String?,
                   @Header("language") language: String?,
                   @Header("platform") platform: Int,
                   @Header("currencysymbol") currencySymbol: String?,
                   @Header("currencycode") currencyCode: String?,
                   @Body request: UpdateCartRequest?): Single<CommonModel?>?

    @GET("v1/ecomm/cart")
    fun getCartProducts(@Header("authorization") authorization: String?,
                        @Header("language") language: String?,
                        @Header("platform") platform: Int,
                        @Header("currencysymbol") currencySymbol: String?,
                        @Header("currencycode") currencyCode: String?,
                        @QueryMap(encoded = true) query: HashMap<String, Any>): Single<CartDetails?>?

    @GET("v1/ecomm/Wallet")
    fun getWalletDetailsApi(@Header("authorization") authorization: String?,
                            @Header("lan") language: String?,
                            @QueryMap(encoded = true) query: HashMap<String, Any>): Single<WalletDataDetails?>?

    @POST("v1/ecomm/order")
    fun placeOrder(@Header("authorization") authorization: String?,
                   @Header("language") language: String?,
                   @Header("platform") platform: Int,
                   @Header("currencysymbol") currencySymbol: String?,
                   @Header("currencycode") currencyCode: String?,
                   @Body request: PlaceOrderRequest?): Single<CommonModel?>?

    @GET("v1/ecomm/ipLocation")
    fun ipAddressToLocation(@Header("language") language: String?,
                            @Header("platform") platform: Int,
                            @Header("currencysymbol") currencySymbol: String?,
                            @Header("currencycode") currencyCode: String?,
                            @QueryMap(encoded = true) ipAddress: ArrayMap<String?, String?>?): Single<IpAddressToLocationDetails?>?

    @POST("v3/promotion/")
    fun applyPromoCodeApi(@Body req: ApplyPromoCodeRequest?): Single<ApplyPromoCodeListData?>?


    @POST("python/likeDislikeReview/")
    fun reviewLikeDisLike(@Header("authorization") authorization: String?,
                          @Header("language") language: String?,
                          @Body request: ReviewLikeDisLikeRequest?): Single<CommonModel?>?

    @POST("python/favourite/product/")
    fun addProductToWishList(@Header("authorization") authorization: String?,
                             @Header("language") language: String?,
                             @Body request: AddToWishListRequest?): Single<CommonModel?>?

    @PATCH("python/favourite/product/")
    fun removeProductFromWishList(
        @Header("authorization") authorization: String?,
        @Header("STORECATEGORYID") storeCatId: String?,
        @Header("language") language: String?,
        @Body request: AddToWishListRequest?): Single<CommonModel?>?

    @GET("python/ratableAttribute/")
    fun getRatableProducts(@Header("authorization") authorization: String?,
                           @Header("language") language: String?,
                           @Header("storeCategoryId") storeCategoryId: String?,
                           @QueryMap(encoded = true) productId: ArrayMap<String?, String?>?): Single<RatableDetails?>?

    @POST("python/productReviewRating/")
    fun reviewProduct(@Header("authorization") authorization: String?,
                      @Header("language") language: String?,
                      @Header("ipAddress") ipAddress: String?,
                      @Header("latitude") latitude: Double,
                      @Header("longitude") longitude: Double,
                      @Body reviewRequest: ProductRateAndReviewRequest?): Single<CommonModel?>?

    @GET("python/seller/list")
    fun getSellerList(@Header("authorization") authorization: String?,
                      @Header("language") language: String?,
                      @Header("loginType") loginType: String?,
                      @QueryMap(encoded = true) queryParam: ArrayMap<String?, String?>?): Single<SellerDetails?>?

    @GET("python/predict")
    fun getSimilarProducts(@QueryMap(encoded = true) hashReq: ArrayMap<String?, Any?>?): Single<GetSimilarProducts?>?

    @PATCH("python/wishList/")
    fun clearAllWishListItems(@Header("authorization") authorization: String?): Single<CommonModel?>?

    @GET("python/wishList/")
    fun getWishList(@Header("authorization") authorization: String?,
                    @Header("STORECATEGORYID") storeCatId: String?,
                    @Header("language") language: String?,
                    @Header("sortType") sortType: Int): Single<WishListDetails?>?

    @GET("python/wishList/")
    fun getWishList(@Header("authorization") authorization: String?,
                    @Header("STORECATEGORYID") storeCatId: String?,
                    @Header("language") language: String?,
                    @Header("sortType") sortType: Int,
                    @Header("text") searchQuery: String?): Single<WishListDetails?>?

  @HTTP(method = "DELETE", path = "order", hasBody = true)
  fun cancelOrder(
    @Header("authorization") authorization: String?,
    @Header("language") language: String?,
    @Header("platform") platform: Int,
    @Header("currencysymbol") currencySymbol: String?,
    @Header("currencycode") currencyCode: String?,
    @Body request: CancelOrderRequest?
  ): Single<CommonModel?>?

  @GET("v1/ecomm/orders")
  fun getOrderHistory(
    @Header("authorization") authorization: String?,
    @Header("language") language: String?,
    @Header("platform") platform: Int,
    @Header("currencysymbol") currencySymbol: String?,
    @Header("currencycode") currencyCode: String?,
    @QueryMap(encoded = true) query: HashMap<String, Any>
  ): Single<OrderHistoryDetails?>?

  @GET("v1/ecomm/orders/details")
  fun getStoreOrderDetail(
    @Header("authorization") authorization: String?,
    @Header("language") language: String?,
    @Header("platform") platform: Int,
    @Header("currencysymbol") currencySymbol: String?,
    @Header("currencycode") currencyCode: String?,
    @QueryMap(encoded = true) query: HashMap<String, Any>
  ): Single<StoreOrdersItem?>?

  @GET("v1/ecomm/orders/details")
  fun getOrderDetails(
    @Header("authorization") authorization: String?,
    @Header("language") language: String?,
    @Header("platform") platform: Int,
    @Header("currencysymbol") currencySymbol: String?,
    @Header("currencycode") currencyCode: String?,
    @QueryMap(encoded = true) query: HashMap<String, Any>
  ): Single<MasterOrderDetails?>?

  @GET("v1/ecomm/orders/details")
  fun getMasterOrderDetail(
    @Header("authorization") authorization: String?,
    @Header("language") language: String?,
    @Header("platform") platform: Int,
    @Header("currencysymbol") currencySymbol: String?,
    @Header("currencycode") currencyCode: String?,
    @QueryMap(encoded = true) query: HashMap<String?, Any?>?
  ): Single<MasterOrdersDetail?>?

  @GET("v1/ecomm/order/status")
  fun getTrackingDetails(
    @Header("authorization") authorization: String?,
    @Header("language") language: String?,
    @Header("platform") platform: Int,
    @Header("currencysymbol") currencySymbol: String?,
    @Header("currencycode") currencyCode: String?,
    @QueryMap(encoded = true) query: HashMap<String, Any>
  ): Single<TrackingListOrderStatusData?>?

  @GET("v1/ecomm/order/package/details")
  fun getPackageDetails(
    @Header("authorization") authorization: String?,
    @Header("language") language: String?,
    @Header("platform") platform: Int,
    @Header("currencysymbol") currencySymbol: String?,
    @Header("currencycode") currencyCode: String?,
    @QueryMap(encoded = true) query: HashMap<String, Any>
  ): Single<OrderDetails?>?
}