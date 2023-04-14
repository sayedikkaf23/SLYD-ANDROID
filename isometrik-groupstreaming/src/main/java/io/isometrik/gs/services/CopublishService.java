package io.isometrik.gs.services;

import io.isometrik.gs.response.copublish.AcceptCopublishRequestResult;
import io.isometrik.gs.response.copublish.AddCopublishRequestResult;
import io.isometrik.gs.response.copublish.DeleteCopublishRequestResult;
import io.isometrik.gs.response.copublish.DenyCopublishRequestResult;
import io.isometrik.gs.response.copublish.FetchCopublishRequestStatusResult;
import io.isometrik.gs.response.copublish.FetchCopublishRequestsResult;
import io.isometrik.gs.response.copublish.SwitchProfileResult;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * The interface copublish service to request or delete copublish, accept or deny a user's
 * copublish request or fetch all copublish requests or fetch status of a copublish request in a
 * stream group.
 */
public interface CopublishService {

  /**
   * Add copublish request call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call<AddCopublishRequestResult>
   * @see io.isometrik.gs.response.copublish.AddCopublishRequestResult
   */
  @POST("/streaming/copublish/request")
  Call<AddCopublishRequestResult> addCopublishRequest(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Accept copublish request call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call<AcceptCopublishRequestResult>
   * @see io.isometrik.gs.response.copublish.AcceptCopublishRequestResult
   */
  @POST("/streaming/copublish/accept")
  Call<AcceptCopublishRequestResult> acceptCopublishRequest(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Deny copublish request call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call<DenyCopublishRequestResult>
   * @see io.isometrik.gs.response.copublish.DenyCopublishRequestResult
   */
  @POST("/streaming/copublish/deny")
  Call<DenyCopublishRequestResult> denyCopublishRequest(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Switch profile call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the call<SwitchProfileResult>
   * @see io.isometrik.gs.response.copublish.SwitchProfileResult
   */
  @POST("/streaming/switchprofile")
  Call<SwitchProfileResult> switchProfile(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Delete copublish request call.
   *
   * @param headers the headers
   * @param accountId the account id
   * @param projectId the project id
   * @param keysetId the keyset id
   * @param streamId the stream id
   * @param userId the user id
   * @return Call<DeleteCopublishRequestResult>
   * @see io.isometrik.gs.response.copublish.DeleteCopublishRequestResult
   */
  @DELETE("/streaming/copublish/request/{accountId}/{projectId}/{keysetId}/{streamId}/{userId}")
  Call<DeleteCopublishRequestResult> deleteCopublishRequest(@HeaderMap Map<String, String> headers,
      @Path("accountId") String accountId, @Path("projectId") String projectId,
      @Path("keysetId") String keysetId, @Path("streamId") String streamId,
      @Path("userId") String userId);

  /**
   * Fetch copublish request status call.
   *
   * @param headers the headers
   * @param accountId the account id
   * @param projectId the project id
   * @param keysetId the keyset id
   * @param streamId the stream id
   * @param userId the user id
   * @return the Call<FetchCopublishRequestStatusResult>
   * @see io.isometrik.gs.response.copublish.FetchCopublishRequestStatusResult
   */
  @GET("/streaming/copublish/status/{accountId}/{projectId}/{keysetId}/{streamId}/{userId}")
  Call<FetchCopublishRequestStatusResult> fetchCopublishRequestStatus(
      @HeaderMap Map<String, String> headers, @Path("accountId") String accountId,
      @Path("projectId") String projectId, @Path("keysetId") String keysetId,
      @Path("streamId") String streamId, @Path("userId") String userId);

  /**
   * Fetch copublish requests call.
   *
   * @param headers the headers
   * @param accountId the account id
   * @param projectId the project id
   * @param keysetId the keyset id
   * @param streamId the stream id
   * @return the Call<FetchCopublishRequestsResult>
   * @see io.isometrik.gs.response.copublish.FetchCopublishRequestsResult
   */
  @GET("/streaming/copublish/request/{accountId}/{projectId}/{keysetId}/{streamId}")
  Call<FetchCopublishRequestsResult> fetchCopublishRequests(@HeaderMap Map<String, String> headers,
      @Path("accountId") String accountId, @Path("projectId") String projectId,
      @Path("keysetId") String keysetId, @Path("streamId") String streamId);
}
