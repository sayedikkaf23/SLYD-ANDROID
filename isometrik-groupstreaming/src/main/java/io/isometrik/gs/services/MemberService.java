package io.isometrik.gs.services;

import io.isometrik.gs.response.member.AddMemberResult;
import io.isometrik.gs.response.member.FetchMembersResult;
import io.isometrik.gs.response.member.LeaveMemberResult;
import io.isometrik.gs.response.member.RemoveMemberResult;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * The interface member service to add,remove member,fetch members in stream group and leave a
 * stream group by a member.
 */
public interface MemberService {

  /**
   * Add member to a stream group call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<AddMemberResult>
   * @see io.isometrik.gs.response.member.AddMemberResult
   */
  @POST("/streaming/member")
  Call<AddMemberResult> addMember(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Remove member from a stream group call.
   *
   * @param headers the headers
   * @param accountId the account id
   * @param projectId the project id
   * @param keysetId the keyset id
   * @param streamId the stream id
   * @param initiatorId the initiator id
   * @param memberId the member id
   * @return the Call<RemoveMemberResult>
   * @see io.isometrik.gs.response.member.RemoveMemberResult
   */
  @DELETE("/streaming/member/{accountId}/{projectId}/{keysetId}/{streamId}/{initiatorId}/{memberId}")
  Call<RemoveMemberResult> removeMember(@HeaderMap Map<String, String> headers,
      @Path("accountId") String accountId, @Path("projectId") String projectId,
      @Path("keysetId") String keysetId, @Path("streamId") String streamId,
      @Path("initiatorId") String initiatorId, @Path("memberId") String memberId);

  /**
   * Fetch members in a stream group call.
   *
   * @param headers the headers
   * @param accountId the account id
   * @param projectId the project id
   * @param keysetId the keyset id
   * @param streamId the stream id
   * @return the Call<FetchMembersResult>
   * @see io.isometrik.gs.response.member.FetchMembersResult
   */
  @GET("/streaming/member/{accountId}/{projectId}/{keysetId}/{streamId}")
  Call<FetchMembersResult> fetchMembers(@HeaderMap Map<String, String> headers,
      @Path("accountId") String accountId, @Path("projectId") String projectId,
      @Path("keysetId") String keysetId, @Path("streamId") String streamId);

  /**
   * Leave stream group by a member call.
   *
   * @param headers the headers
   * @param accountId the account id
   * @param projectId the project id
   * @param keysetId the keyset id
   * @param streamId the stream id
   * @param memberId the member id
   * @return Call<LeaveMemberResult>
   * @see io.isometrik.gs.response.member.LeaveMemberResult
   */
  @DELETE("/streaming/member/leave/{accountId}/{projectId}/{keysetId}/{streamId}/{memberId}")
  Call<LeaveMemberResult> leaveMember(@HeaderMap Map<String, String> headers,
      @Path("accountId") String accountId, @Path("projectId") String projectId,
      @Path("keysetId") String keysetId, @Path("streamId") String streamId,
      @Path("memberId") String memberId);
}
