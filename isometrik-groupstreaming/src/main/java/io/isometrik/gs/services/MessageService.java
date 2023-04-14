package io.isometrik.gs.services;

import io.isometrik.gs.response.message.DeleteMessageResult;
import io.isometrik.gs.response.message.FetchMessagesResult;
import io.isometrik.gs.response.message.SendMessageResult;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * The interface message service to send or remove message in a stream group,fetch messages sent in
 * a stream group.
 */
public interface MessageService {

  /**
   * Send message in a stream group call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<SendMessageResult>
   * @see io.isometrik.gs.response.message.SendMessageResult
   */
  @POST("/streaming/message")
  Call<SendMessageResult> sendMessage(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Remove message from a stream group call.
   *
   * @param headers the headers
   * @param accountId the account id
   * @param projectId the project id
   * @param keysetId the keyset id
   * @param streamId the stream id
   * @param memberId the member id
   * @param memberName the member name
   * @param messageId the message id
   * @param sentAt the sent at
   * @return the Call<DeleteMessageResult>
   * @see io.isometrik.gs.response.message.DeleteMessageResult
   */
  @DELETE("/streaming/message/{accountId}/{projectId}/{keysetId}/{streamId}/{memberId}/{memberName}/{messageId}/{sentAt}")
  Call<DeleteMessageResult> removeMessage(@HeaderMap Map<String, String> headers,
      @Path("accountId") String accountId, @Path("projectId") String projectId,
      @Path("keysetId") String keysetId, @Path("streamId") String streamId,
      @Path("memberId") String memberId, @Path("memberName") String memberName,
      @Path("messageId") String messageId, @Path("sentAt") Long sentAt);

  /**
   * Fetch messages in a stream group call.
   *
   * @param headers the headers
   * @param accountId the account id
   * @param projectId the project id
   * @param keysetId the keyset id
   * @param streamId the stream id
   * @param userId the user id
   * @param pagingOptions the paging options
   * @return the Call<FetchMessagesResult>
   * @see io.isometrik.gs.response.message.FetchMessagesResult
   */
  @GET("/streaming/message/{accountId}/{projectId}/{keysetId}/{streamId}/{userId}")
  Call<FetchMessagesResult> fetchMessages(@HeaderMap Map<String, String> headers,
      @Path("accountId") String accountId, @Path("projectId") String projectId,
      @Path("keysetId") String keysetId, @Path("streamId") String streamId,
      @Path("userId") String userId, @QueryMap Map<String, Object> pagingOptions);
}
