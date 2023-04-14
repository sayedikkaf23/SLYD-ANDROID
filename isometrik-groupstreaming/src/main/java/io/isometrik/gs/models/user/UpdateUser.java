package io.isometrik.gs.models.user;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.user.UpdateUserQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.ErrorResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.error.IsometrikErrorBuilder;
import io.isometrik.gs.response.user.UpdateUserResult;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The model class to validate the update a user UpdateUserQuery{@link
 * io.isometrik.gs.builder.user.UpdateUserQuery} request and parse the response(both success or
 * error) to return update user result UpdateUserResult{@link io.isometrik.gs.response.user.UpdateUserResult}
 * or error received.
 *
 * @see io.isometrik.gs.builder.user.UpdateUserQuery
 * @see io.isometrik.gs.response.user.UpdateUserResult
 */
public class UpdateUser {
  /**
   * Validate params.
   *
   * @param updateUserQuery the update user request query
   * @param completionHandler the update user details request completion handler
   * @param retrofitManager the retrofit manager to make remote api calls
   * @param imConfiguration the isometrik configuration instance
   * @param baseResponse the base response instance to handle non 200 http response code messages
   * @param gson the gson instance
   * @see io.isometrik.gs.builder.user.UpdateUserQuery
   * @see io.isometrik.gs.response.CompletionHandler
   * @see io.isometrik.gs.managers.RetrofitManager
   * @see io.isometrik.gs.IMConfiguration
   * @see io.isometrik.gs.response.error.BaseResponse
   * @see com.google.gson.Gson
   */
  public void validateParams(@NotNull UpdateUserQuery updateUserQuery,
      @NotNull final CompletionHandler<UpdateUserResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

    String userIdentifier = updateUserQuery.getUserIdentifier();
    String userName = updateUserQuery.getUserName();
    String userProfilePic = updateUserQuery.getUserProfilePic();
    String userId = updateUserQuery.getUserId();

    if (userId == null || userId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USERID_MISSING);
    } else if (userName != null && userName.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USERNAME_INVALID_VALUE);
    } else if (userIdentifier != null && userIdentifier.isEmpty()) {
      completionHandler.onComplete(null,
          IsometrikErrorBuilder.IMERROBJ_USER_IDENTIFIER_INVALID_VALUE);
    } else if (userProfilePic != null && userProfilePic.isEmpty()) {
      completionHandler.onComplete(null,
          IsometrikErrorBuilder.IMERROBJ_USER_PROFILEPIC_INVALID_VALUE);
    } else {
      Map<String, String> headers = new HashMap<>();
      headers.put("licenseKey", imConfiguration.getLicenseKey());

      Map<String, Object> body = new HashMap<>();
      body.put("accountId", imConfiguration.getAccountId());
      body.put("projectId", imConfiguration.getProjectId());
      body.put("keysetId", imConfiguration.getKeysetId());
      body.put("userId", userId);

      if (userName != null) body.put("userName", userName);
      if (userProfilePic != null) body.put("userProfilePic", userProfilePic);
      if (userIdentifier != null) body.put("userIdentifier", userIdentifier);

      Call<UpdateUserResult> call = retrofitManager.getUserService().updateUser(headers, body);
      call.enqueue(new Callback<UpdateUserResult>() {
        @Override
        public void onResponse(@NotNull Call<UpdateUserResult> call,
            @NotNull Response<UpdateUserResult> response) {

          if (response.isSuccessful()) {

            if (response.code() == 200) {
              completionHandler.onComplete(response.body(), null);
            }
          } else {

            ErrorResponse errorResponse;
            IsometrikError.Builder isometrikErrorBuilder;
            try {

              if (response.errorBody() != null) {
                errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
              } else {
                errorResponse = new ErrorResponse();
              }
            } catch (IOException | IllegalStateException | JsonSyntaxException e) {
              // handle failure to read error
              errorResponse = new ErrorResponse();
            }

            isometrikErrorBuilder =
                new IsometrikError.Builder().setHttpResponseCode(response.code())
                    .setRemoteError(true);

            switch (response.code()) {

              case 400:

                isometrikErrorBuilder =
                    baseResponse.handle400responseCode(isometrikErrorBuilder, errorResponse);
                break;

              case 404:

                isometrikErrorBuilder =
                    baseResponse.handle404responseCode(isometrikErrorBuilder, errorResponse);
                break;

              case 409:

                isometrikErrorBuilder =
                    baseResponse.handle409responseCode(isometrikErrorBuilder, errorResponse, false);
                break;

              case 422:

                isometrikErrorBuilder =
                    baseResponse.handle422responseCode(isometrikErrorBuilder, errorResponse);
                break;

              case 502:

                isometrikErrorBuilder = baseResponse.handle502responseCode(isometrikErrorBuilder);
                break;

              case 503:
                isometrikErrorBuilder =
                    baseResponse.handle503responseCode(isometrikErrorBuilder, errorResponse);
                break;

              default:
                //500 response code
                isometrikErrorBuilder = baseResponse.handle500responseCode(isometrikErrorBuilder);
            }

            completionHandler.onComplete(null, isometrikErrorBuilder.build());
          }
        }

        @Override
        public void onFailure(@NotNull Call<UpdateUserResult> call, @NotNull Throwable t) {
          if (t instanceof IOException) {
            // Network failure
            completionHandler.onComplete(null, baseResponse.handleNetworkError(t));
          } else {
            // Parsing error
            completionHandler.onComplete(null, baseResponse.handleParsingError(t));
          }
        }
      });
    }
  }
}
