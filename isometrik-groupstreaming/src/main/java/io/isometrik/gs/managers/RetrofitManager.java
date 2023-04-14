package io.isometrik.gs.managers;

import android.util.Log;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.enums.IMLogVerbosity;
import io.isometrik.gs.services.CopublishService;
import io.isometrik.gs.services.MemberService;
import io.isometrik.gs.services.MessageService;
import io.isometrik.gs.services.StreamService;
import io.isometrik.gs.services.SubscriptionService;
import io.isometrik.gs.services.UserService;
import io.isometrik.gs.services.ViewerService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The manager class for Retrofit and OkHttpClient client.
 */
public class RetrofitManager {

  private Isometrik isometrik;

  private OkHttpClient transactionClientInstance;

  private MemberService memberService;
  private MessageService messageService;
  private StreamService streamService;
  private UserService userService;
  private ViewerService viewerService;
  private SubscriptionService subscriptionService;
  private CopublishService copublishService;

  /**
   * Instantiates a new Retrofit manager.
   *
   * @param isometrikInstance the isometrik instance
   * @see io.isometrik.gs.Isometrik
   */
  public RetrofitManager(Isometrik isometrikInstance) {
    this.isometrik = isometrikInstance;
    this.transactionClientInstance =
        createOkHttpClient(this.isometrik.getConfiguration().getRequestTimeout(),
            this.isometrik.getConfiguration().getConnectTimeout());
    Retrofit transactionInstance = createRetrofit(this.transactionClientInstance);

    this.memberService = transactionInstance.create(MemberService.class);
    this.messageService = transactionInstance.create(MessageService.class);
    this.streamService = transactionInstance.create(StreamService.class);
    this.userService = transactionInstance.create(UserService.class);
    this.viewerService = transactionInstance.create(ViewerService.class);
    this.subscriptionService = transactionInstance.create(SubscriptionService.class);
    this.copublishService = transactionInstance.create(CopublishService.class);
  }

  /**
   * Create OkHttpClient instance.
   *
   * @return OkHttpClient instance
   * @see okhttp3.OkHttpClient
   */
  private OkHttpClient createOkHttpClient(int requestTimeout, int connectTimeOut) {

    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    httpClient.retryOnConnectionFailure(false);
    httpClient.readTimeout(requestTimeout, TimeUnit.SECONDS);
    httpClient.connectTimeout(connectTimeOut, TimeUnit.SECONDS);

    if (isometrik.getConfiguration().getLogVerbosity() == IMLogVerbosity.BODY) {
      HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
      logging.setLevel(HttpLoggingInterceptor.Level.BODY);
      httpClient.addInterceptor(logging);
    }

    return httpClient.build();
  }

  /**
   * Create retrofit instance.
   *
   * @return retrofit instance
   * @see retrofit2.Retrofit
   */
  private Retrofit createRetrofit(OkHttpClient client) {
    return new Retrofit.Builder().baseUrl(isometrik.getBaseUrl())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build();
  }

  /**
   * Gets member service.
   *
   * @return the member service
   * @see io.isometrik.gs.services.MemberService
   */
  public MemberService getMemberService() {
    return memberService;
  }

  /**
   * Gets copublish service.
   *
   * @return the copublish service
   * @see io.isometrik.gs.services.CopublishService
   */
  public CopublishService getCopublishService() {
    return copublishService;
  }

  /**
   * Gets message service.
   *
   * @return the message service
   * @see io.isometrik.gs.services.MessageService
   */
  public MessageService getMessageService() {
    return messageService;
  }

  /**
   * Gets stream service.
   *
   * @return the stream service
   * @see io.isometrik.gs.services.StreamService
   */
  public StreamService getStreamService() {
    return streamService;
  }

  /**
   * Gets user service.
   *
   * @return the user service
   * @see io.isometrik.gs.services.UserService
   */
  public UserService getUserService() {
    return userService;
  }

  /**
   * Gets viewer service.
   *
   * @return the viewer service
   * io.isometrik.gs.services.ViewerService
   */
  public ViewerService getViewerService() {
    return viewerService;
  }

  /**
   * Gets subscription service.
   *
   * @return the subscription service
   * @see io.isometrik.gs.services.SubscriptionService
   */
  public SubscriptionService getSubscriptionService() {
    return subscriptionService;
  }

  /**
   * Destroy.
   *
   * @param force whether to destroy forcibly
   */
  public void destroy(boolean force) {
    if (this.transactionClientInstance != null) {
      closeExecutor(this.transactionClientInstance, force);
    }
  }

  /**
   * Closes the OkHttpClient execute
   *
   * @param client OkHttpClient whose requests are to be canceled
   * @param force whether to forcibly shutdown the OkHttpClient and evict all connection pool
   */
  private void closeExecutor(OkHttpClient client, boolean force) {
    client.dispatcher().cancelAll();
    if (force) {
      client.connectionPool().evictAll();
      ExecutorService executorService = client.dispatcher().executorService();
      executorService.shutdown();
    }
  }
}

