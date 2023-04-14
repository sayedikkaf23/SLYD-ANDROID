package io.isometrik.gs.services;

import io.isometrik.gs.response.subscription.AddSubscriptionResult;
import io.isometrik.gs.response.subscription.RemoveSubscriptionResult;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.HeaderMap;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * The interface subscription service to add or remove subscriptions for reciving stream presence
 * events on stream start or stop.
 */
public interface SubscriptionService {

  /**
   * Add subscription for stream start or stop event call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<AddSubscriptionResult>
   * @see io.isometrik.gs.response.subscription.AddSubscriptionResult
   */
  @PUT("/gs/subscription")
  Call<AddSubscriptionResult> addSubscription(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Remove subscription for stream start or stop event call.
   *
   * @param headers the headers
   * @param accountId the account id
   * @param projectId the project id
   * @param keysetId the keyset id
   * @param clientId the client id
   * @param streamStartChannel the stream start channel
   * @return the Call<RemoveSubscriptionResult>
   * @see io.isometrik.gs.response.subscription.RemoveSubscriptionResult
   */
  @DELETE("/gs/subscription/{accountId}/{projectId}/{keysetId}/{clientId}/{streamStartChannel}")
  Call<RemoveSubscriptionResult> removeSubscription(@HeaderMap Map<String, String> headers,
      @Path("accountId") String accountId, @Path("projectId") String projectId,
      @Path("keysetId") String keysetId, @Path("clientId") String clientId,
      @Path("streamStartChannel") boolean streamStartChannel);
}
