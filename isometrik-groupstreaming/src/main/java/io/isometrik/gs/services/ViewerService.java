package io.isometrik.gs.services;

import io.isometrik.gs.response.viewer.AddViewerResult;
import io.isometrik.gs.response.viewer.FetchViewersCountResult;
import io.isometrik.gs.response.viewer.FetchViewersResult;
import io.isometrik.gs.response.viewer.LeaveViewerResult;
import io.isometrik.gs.response.viewer.RemoveViewerResult;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * The interface viewer service to join,leave a stream group as viewer,remove a viewer, fetch
 * viewers and viewers count in a stream group.
 */
public interface ViewerService {

  /**
   * Join stream group as viewer call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<AddViewerResult>
   * @see io.isometrik.gs.response.viewer.AddViewerResult
   */
  @POST("/streaming/viewer")
  Call<AddViewerResult> addViewer(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Leave as viewer from a stream group call.
   *
   * @param headers the headers
   * @param accountId the account id
   * @param projectId the project id
   * @param keysetId the keyset id
   * @param streamId the stream id
   * @param viewerId the viewer id
   * @return the Call<LeaveViewerResult>
   * @see io.isometrik.gs.response.viewer.LeaveViewerResult
   */
  @DELETE("/streaming/viewer/leave/{accountId}/{projectId}/{keysetId}/{streamId}/{viewerId}")
  Call<LeaveViewerResult> leaveViewer(@HeaderMap Map<String, String> headers,
      @Path("accountId") String accountId, @Path("projectId") String projectId,
      @Path("keysetId") String keysetId, @Path("streamId") String streamId,
      @Path("viewerId") String viewerId);

  /**
   * Remove a viewer from a stream group call.
   *
   * @param headers the headers
   * @param accountId the account id
   * @param projectId the project id
   * @param keysetId the keyset id
   * @param streamId the stream id
   * @param initiatorId the initiator id
   * @param viewerId the viewer id
   * @return the Call<RemoveViewerResult>
   * @see io.isometrik.gs.response.viewer.RemoveViewerResult
   */
  @DELETE("/streaming/viewer/{accountId}/{projectId}/{keysetId}/{streamId}/{initiatorId}/{viewerId}")
  Call<RemoveViewerResult> removeViewer(@HeaderMap Map<String, String> headers,
      @Path("accountId") String accountId, @Path("projectId") String projectId,
      @Path("keysetId") String keysetId, @Path("streamId") String streamId,
      @Path("initiatorId") String initiatorId, @Path("viewerId") String viewerId);

  /**
   * Fetch viewers in a stream group call.
   *
   * @param headers the headers
   * @param accountId the account id
   * @param projectId the project id
   * @param keysetId the keyset id
   * @param streamId the stream id
   * @return the Call<FetchViewersResult>
   * @see io.isometrik.gs.response.viewer.FetchViewersResult
   */
  @GET("/streaming/viewer/{accountId}/{projectId}/{keysetId}/{streamId}")
  Call<FetchViewersResult> fetchViewers(@HeaderMap Map<String, String> headers,
      @Path("accountId") String accountId, @Path("projectId") String projectId,
      @Path("keysetId") String keysetId, @Path("streamId") String streamId);

  /**
   * Fetch viewers count in a stream group call.
   *
   * @param headers the headers
   * @param accountId the account id
   * @param projectId the project id
   * @param keysetId the keyset id
   * @param streamId the stream id
   * @return the Call<FetchViewersCountResult>
   * @see io.isometrik.gs.response.viewer.FetchViewersCountResult
   */
  @GET("/streaming/viewer/count/{accountId}/{projectId}/{keysetId}/{streamId}")
  Call<FetchViewersCountResult> fetchViewersCount(@HeaderMap Map<String, String> headers,
      @Path("accountId") String accountId, @Path("projectId") String projectId,
      @Path("keysetId") String keysetId, @Path("streamId") String streamId);
}
