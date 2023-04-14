package chat.hola.com.app.DownloadFile;
/*
 * Created by moda on 28/06/16.
 */


import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.UnsafeOkHttpClient;
import chat.hola.com.app.Utilities.ApiOnServer;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/*
 * Service containing the list of the multipart port and link used to upload image/audio or video shared on a chat
 *
 * */
public class ServiceGenerator {

    private static final String ChatMulterUpload =

            ApiOnServer.CHAT_MULTER_UPLOAD_URL;


    private static OkHttpClient httpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient(AppController.getInstance().getApplicationContext());
//            new OkHttpClient.Builder().readTimeout(300, TimeUnit.SECONDS).writeTimeout(300, TimeUnit.SECONDS)
//            .connectTimeout(300, TimeUnit.SECONDS);


    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(ChatMulterUpload)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }
}