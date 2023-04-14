package io.isometrik.gs.services;

import io.isometrik.gs.response.user.AddUserResult;
import io.isometrik.gs.response.user.DeleteUserResult;
import io.isometrik.gs.response.user.FetchUserDetailsResult;
import io.isometrik.gs.response.user.FetchUsersResult;
import io.isometrik.gs.response.user.UpdateUserResult;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * The interface user service to create, update or delete a user and fetch list of users.
 */
public interface UserService {

  /**
   * Create user call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<AddUserResult>
   * @see io.isometrik.gs.response.user.AddUserResult
   */
  @POST("/streaming/user")
  Call<AddUserResult> addUser(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Update user call.
   *
   * @param headers the headers
   * @param bodyParams the body params
   * @return the Call<UpdateUserResult>
   * @see io.isometrik.gs.response.user.UpdateUserResult
   */
  @PATCH("/streaming/user")
  Call<UpdateUserResult> updateUser(@HeaderMap Map<String, String> headers,
      @Body Map<String, Object> bodyParams);

  /**
   * Delete user call.
   *
   * @param headers the headers
   * @param accountId the account id
   * @param projectId the project id
   * @param keysetId the keyset id
   * @param userId the user id
   * @return the Call<DeleteUserResult>
   * @see io.isometrik.gs.response.user.DeleteUserResult
   */
  @DELETE("/streaming/user/details/{accountId}/{projectId}/{keysetId}/{userId}")
  Call<DeleteUserResult> deleteUser(@HeaderMap Map<String, String> headers,
      @Path("accountId") String accountId, @Path("projectId") String projectId,
      @Path("keysetId") String keysetId, @Path("userId") String userId);

  /**
   * Fetch users list call.
   *
   * @param headers the headers
   * @param accountId the account id
   * @param projectId the project id
   * @param keysetId the keyset id
   * @param pagingOptions the paging options
   * @return the Call<FetchUsersResult>
   * @see io.isometrik.gs.response.user.FetchUsersResult
   */
  @GET("/streaming/user/{accountId}/{projectId}/{keysetId}")
  Call<FetchUsersResult> fetchUsers(@HeaderMap Map<String, String> headers,
      @Path("accountId") String accountId, @Path("projectId") String projectId,
      @Path("keysetId") String keysetId, @QueryMap Map<String, Object> pagingOptions);

  /**
   * Fetch user details call.
   *
   * @param headers the headers
   * @param accountId the account id
   * @param projectId the project id
   * @param keysetId the keyset id
   * @param userId the user id
   * @return the Call<FetchUserDetailsResult>
   * @see io.isometrik.gs.response.user.FetchUserDetailsResult
   */
  @GET("/streaming/user/details/{accountId}/{projectId}/{keysetId}/{userId}")
  Call<FetchUserDetailsResult> fetchUserDetails(@HeaderMap Map<String, String> headers,
      @Path("accountId") String accountId, @Path("projectId") String projectId,
      @Path("keysetId") String keysetId, @Path("userId") String userId);
}
