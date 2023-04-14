package chat.hola.com.app.Networking;

import chat.hola.com.app.dubly.DubResponse;
import chat.hola.com.app.dublycategory.modules.DubCategoryResponse;
import chat.hola.com.app.home.home.ProductsResponse;
import io.reactivex.Observable;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * <h1>EcomPythonService</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 18/6/18.
 */


public interface EcomPythonService {
    @POST("python/product/list")
    Observable<Response<ProductsResponse>> getProducts(@Header("authorization") String things,
        @Header("language") String language,
        @Body Map<String, Object> map);
}
