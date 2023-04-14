package chat.hola.com.app.Networking;

import chat.hola.com.app.home.model.Posts;
import chat.hola.com.app.home.model.SuggestedUserResponse;
import chat.hola.com.app.home.trending.model.HeaderResponse;
import chat.hola.com.app.home.trending.model.Trending;
import chat.hola.com.app.models.ViewPostRequest;
import chat.hola.com.app.profileScreen.channel.Model.ChannelData;
import chat.hola.com.app.profileScreen.discover.contact.pojo.ContactRequest;
import chat.hola.com.app.profileScreen.discover.contact.pojo.Contacts;
import chat.hola.com.app.search.model.SearchResponse;
import io.reactivex.Completable;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * <h1>HowdooServiceTrending</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 18/6/18.
 */


public interface HowdooServiceTrending {
    @GET("categories/")
    Observable<Response<HeaderResponse>> getCategories(@Header("authorization") String auth,
                                                       @Header("lang") String lang);

    @GET("trendingHashTags/")
    Observable<Response<Trending>> getTrending(@Header("authorization") String auth,
                                               @Header("lang") String lang,
                                               @Query("set") int skip,
                                               @Query("limit") int limit);

    @GET("postsByHashtags/")
    Observable<Response<chat.hola.com.app.music.Response>> getPostByHashTag(@Header("authorization") String auth,
                                                                            @Header("lang") String lang,
                                                                            @Header("hashtag") String hashtag,
                                                                            @Query("set") int skip,
                                                                            @Query("limit") int limit);

    @GET("trendingPosts/")
    Observable<Response<chat.hola.com.app.music.Response>> getPostByCategory(@Header("authorization") String auth,
                                                                             @Header("lang") String lang,
                                                                             @Query("categoryId") String categoryId,
                                                                             @Query("set") int skip,
                                                                             @Query("limit") int limit);

    @GET("trendingPosts/")
    Observable<Response<ChannelData>> getPostByCategory1(@Header("authorization") String auth,
                                                         @Header("lang") String lang,
                                                         @Query("categoryId") String categoryId,
                                                         @Query("set") int skip,
                                                         @Query("limit") int limit);

    @GET("getTrendingUsers/")
    Observable<Response<SearchResponse>> getTopStars(@Header("authorization") String auth,
                                                     @Header("lang") String lang,
                                                     @Query("userType") String type,
                                                     @Query("set") int skip,
                                                     @Query("limit") int limit);

    @POST("contactSync/")
    Observable<Response<Contacts>> contactSync(@Header("authorization") String auth,
                                               @Header("lang") String lang,
                                               @Header("skip") int skip,
                                               @Header("limit") int limit,
                                               @Body ContactRequest params);

    @GET("trendingPosts/")
    Observable<Response<Posts>> getPopularPosts(@Header("authorization") String auth,
                                                @Header("lang") String lang,
                                                @Query("set") int skip,
                                                @Query("limit") int limit);

    @GET("guestPosts/")
    Observable<Response<Posts>> getGuestPosts(@Header("authorization") String auth,
                                              @Header("lang") String lang,
                                              @Query("set") int skip,
                                              @Query("limit") int limit);

    @POST("postViews/")
    Observable<Response<ResponseBody>> viewPost(@Header("authorization") String auth,
                                                @Header("lang") String lang,
                                                @Body ViewPostRequest request);

    @GET("popularUser")
    Observable<Response<SuggestedUserResponse>> suggestedUser(@Header("authorization") String auth,
                                                              @Header("lang") String lang,
                                                              @Query("userId") String userId);

    @GET("xclusivePosts/")
    Observable<Response<Posts>> getXclusivePosts(@Header("authorization") String auth,
        @Header("lang") String lang,
        @Query("set") int skip,
        @Query("limit") int limit);
}
