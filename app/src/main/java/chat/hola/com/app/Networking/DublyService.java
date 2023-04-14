package chat.hola.com.app.Networking;

import chat.hola.com.app.dubly.DubResponse;
import chat.hola.com.app.dublycategory.modules.DubCategoryResponse;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * <h1>HowdooServiceTrending</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 18/6/18.
 */


public interface DublyService {
    @GET("musicList")
    Observable<Response<DubResponse>> getDubs(@Header("authorization") String auth,
                                              @Header("lang") String lang,
                                              @Query("categoryId") String categoryId,
                                              @Query("offset") Integer offset,
                                              @Query("limit") Integer limit);

    @GET("musicList/byCategory")
    Observable<Response<DubResponse>> getMusicListByCategory(@Header("authorization") String auth,
                                                             @Header("lang") String lang,
                                                             @Query("categoryId") String categoryId,
                                                             @Query("offset") Integer offset,
                                                             @Query("limit") Integer limit);


    @GET("musicCategory")
    Observable<Response<DubCategoryResponse>> getDubCategories(@Header("authorization") String auth,
                                                               @Header("lang") String lang);

    @GET("favouriteMusic")
    Observable<Response<DubResponse>> getFavouriteDubs(@Header("authorization") String auth,
                                                       @Header("lang") String lang);
}
