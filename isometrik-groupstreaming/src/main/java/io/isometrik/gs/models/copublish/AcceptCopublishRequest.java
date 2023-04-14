package io.isometrik.gs.models.copublish;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.copublish.AcceptCopublishRequestQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.copublish.AcceptCopublishRequestResult;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.ErrorResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.error.IsometrikErrorBuilder;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The class to parse accept copublish request result of accepting a copublish request in a stream
 * group query
 * AcceptCopublishRequestQuery{@link io.isometrik.gs.builder.copublish.AcceptCopublishRequestQuery}.
 *
 * @see io.isometrik.gs.builder.copublish.AcceptCopublishRequestQuery
 */
public class AcceptCopublishRequest {

  /**
   * Validate params.
   *
   * @param acceptCopublishRequestQuery the accept copublish request query
   * @param completionHandler the accept copublish request completion handler
   * @param retrofitManager the retrofit manager to make remote api calls
   * @param imConfiguration the isometrik configuration instance
   * @param baseResponse the base response instance to handle non 200 http response code messages
   * @param gson the gson instance
   * @see io.isometrik.gs.builder.copublish.AcceptCopublishRequestQuery
   * @see io.isometrik.gs.response.CompletionHandler
   * @see io.isometrik.gs.managers.RetrofitManager
   * @see io.isometrik.gs.IMConfiguration
   * @see io.isometrik.gs.response.error.BaseResponse
   * @see com.google.gson.Gson
   */
  public void validateParams(@NotNull AcceptCopublishRequestQuery acceptCopublishRequestQuery,
      @NotNull final CompletionHandler<AcceptCopublishRequestResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

    String streamId = acceptCopublishRequestQuery.getStreamId();
    String userId = acceptCopublishRequestQuery.getUserId();
    String initiatorId = acceptCopublishRequestQuery.getInitiatorId();
    if (streamId == null || streamId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_STREAMID_MISSING);
    } else if (userId == null || userId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_USERID_MISSING);
    } else if (initiatorId == null || initiatorId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_INITIATORID_MISSING);
    } else {

      Map<String, String> headers = new HashMap<>();
      headers.put("licenseKey", imConfiguration.getLicenseKey());

      Map<String, Object> body = new HashMap<>();
      body.put("accountId", imConfiguration.getAccountId());
      body.put("projectId", imConfiguration.getProjectId());
      body.put("keysetId", imConfiguration.getKeysetId());
      body.put("streamId", streamId);
      body.put("userId", userId);
      body.put("initiatorId", initiatorId);

      Call<AcceptCopublishRequestResult> call =
          retrofitManager.getCopublishService().acceptCopublishRequest(headers, body);

      call.enqueue(new Callback<AcceptCopublishRequestResult>() {
        @Override
        public void onResponse(@NotNull Call<AcceptCopublishRequestResult> call,
            @NotNull Response<AcceptCopublishRequestResult> response) {

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

              case 403:

                isometrikErrorBuilder =
                    baseResponse.handle403responseCode(isometrikErrorBuilder, errorResponse, true);
                break;

              case 404:

                isometrikErrorBuilder =
                    baseResponse.handle404responseCode(isometrikErrorBuilder, errorResponse);
                break;

              case 409:

                isometrikErrorBuilder =
                    baseResponse.handle409responseCode(isometrikErrorBuilder, errorResponse, true);
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
        public void onFailure(@NotNull Call<AcceptCopublishRequestResult> call,
            @NotNull Throwable t) {
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