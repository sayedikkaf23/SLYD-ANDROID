package io.isometrik.gs.services;

import io.isometrik.gs.response.stream.FetchStreamsResult;
import io.isometrik.gs.response.stream.StartStreamResult;
import io.isometrik.gs.response.stream.StopStreamResult;
import io.isometrik.gs.response.stream.UpdateStreamPublishingStatusResult;
import io.isometrik.gs.response.stream.UpdateUserPublishingStatusResult;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * The interface stream service to start or stop streaming, fetch stream groups and update user's
 * publishing status in a stream group or in all streams of which he is a viewer or was publining
 * in.
 */
public interface StreamService {

  /**
   * Start streaming call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<StartStreamResult>
   * @see io.isometrik.gs.response.stream.StartStreamResult
   */
  @POST("/streaming/stream")
  Call<StartStreamResult> startStream(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Stop streaming call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<StopStreamResult>
   * @see io.isometrik.gs.response.stream.StopStreamResult
   */
  @PUT("/streaming/stream")
  Call<StopStreamResult> stopStream(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Update publishing status of a member in a stream group call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<UpdateStreamPublishingStatusResult>
   * @see io.isometrik.gs.response.stream.UpdateStreamPublishingStatusResult
   */
  @PUT("/streaming/publish")
  Call<UpdateStreamPublishingStatusResult> updatePublishingStatus(
      @HeaderMap Map<String, String> headers, @Body Map<String, Object> bodyParams);

  /**
   * Update user's publishing status in a stream group call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<UpdateUserPublishingStatusResult>
   * @see io.isometrik.gs.response.stream.UpdateUserPublishingStatusResult
   */
  @PUT("/streaming/publish/user")
  Call<UpdateUserPublishingStatusResult> updateUserPublishingStatus(
      @HeaderMap Map<String, String> headers, @Body Map<String, Object> bodyParams);

  /**
   * Fetch streams call.
   *
   * @param headers the headers
   * @param accountId the account id
   * @param projectId the project id
   * @param keysetId the keyset id
   * @param pagingOptions the paging options
   * @return the Call<FetchStreamsResult>
   * @see io.isometrik.gs.response.stream.FetchStreamsResult
   */
  @GET("/streaming/stream/{accountId}/{projectId}/{keysetId}")
  Call<FetchStreamsResult> fetchStreams(@HeaderMap Map<String, String> headers,
      @Path("accountId") String accountId, @Path("projectId") String projectId,
      @Path("keysetId") String keysetId, @QueryMap Map<String, Object> pagingOptions);
}
