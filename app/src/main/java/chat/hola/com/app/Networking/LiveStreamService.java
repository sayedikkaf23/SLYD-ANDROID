package chat.hola.com.app.Networking;


import java.util.Map;

import chat.hola.com.app.live_stream.ResponcePojo.AllReceivedMessage;
import chat.hola.com.app.live_stream.gift.GiftDataResponse;
import chat.hola.com.app.models.GiftCategories;
import chat.hola.com.app.models.GiftResponse;
import chat.hola.com.app.models.StreamStats;
import chat.hola.com.app.models.StreamViewersResponse;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * <h1>LiveStreamService</h1>
 * <p>contains live stream apis<p/>
 *
 * @author Shaktisisnh jadeja
 * @version 1.0
 * @since 18 July 2019
 */
public interface LiveStreamService {

    @GET("stream/live")
    Observable<Response<ResponseBody>> getLiveStreamers(@Header("authorization") String auth,
                                                        @Header("lan") String selLang,
                                                        @Query("offset") Integer offset,
                                                        @Query("limit") Integer limit);

    @POST("stream")
    Observable<Response<ResponseBody>> postApiCallStream(@Header("authorization") String auth,
                                                         @Header("lan") String selLang,
                                                         @Body Map<String, Object> map);

    @POST("stream/chat")
    Observable<Response<ResponseBody>> postApiChatStream(@Header("authorization") String auth,
                                                         @Header("lan") String selLang,
                                                         @Body Map<String, Object> map);

    @GET("stream/chat")
    Observable<Response<AllReceivedMessage>> getApiChat(@Header("authorization") String auth, @Header("lan") String selLang,
                                                        @Query("streamId") String streamId,
                                                        @Query("timestamp") String timestamp,
                                                        @Query("offset") Integer offset,
                                                        @Query("limit") Integer limit);

    @PUT("stream/subscribe")
    Observable<Response<ResponseBody>> postApiSubscribe(@Header("authorization") String auth,
                                                        @Header("lan") String selLang,
                                                        @Body Map<String, Object> map);


    //For the gifts functionality
    @GET("gift/all")
    Observable<Response<ResponseBody>> getGifts(@Header("authorization") String auth, @Header("lan") String selLang);


    //For sending the gift functionality
    @POST("stream/likeOrGift")
    Observable<Response<GiftResponse>> sendLikeOrGift(@Header("authorization") String auth, @Header("lan") String lang,
                                                      @Body Map<String, Object> map);


    @GET("stream")
    Observable<Response<ResponseBody>> getActiveViewers(@Header("authorization") String auth, @Header("lan") String selLang,
                                                        @Query("id") String id);

    @GET("stream/subscribers")
    Observable<Response<StreamViewersResponse>> viewers(@Header("authorization") String auth, @Header("lan") String lang,
                                                        @Query("streamId") String streamId,
                                                        @Query("userId") String userId,
                                                        @Query("offset") Integer offset,
                                                        @Query("limit") Integer limit);

    @DELETE("endStream")
    Observable<Response<ResponseBody>> endStream(@Header("authorization") String auth, @Header("lan") String selLang);

    @GET("streams/stats")
    Observable<Response<StreamStats>> getLiveStreamStats(@Header("authorization") String auth,
                                                         @Header("lan") String selLang,
                                                         @Query("streamId") String streamId);


    @GET("giftCategories")
    Observable<Response<GiftCategories>> giftCategories(@Header("authorization") String auth,
                                                        @Header("lan") String selLang,
                                                        @Query("status") String status);

    @GET("gifts")
    Observable<Response<GiftDataResponse>> giftsByCategory(@Header("authorization") String auth,
                                                           @Header("lan") String selLang,
                                                           @Query("status") String status,
                                                           @Query("categoryId") String categoryId);
}
