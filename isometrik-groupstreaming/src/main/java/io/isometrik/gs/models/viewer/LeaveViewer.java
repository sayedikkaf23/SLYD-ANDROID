package io.isometrik.gs.models.viewer;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.viewer.LeaveViewerQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.ErrorResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.error.IsometrikErrorBuilder;
import io.isometrik.gs.response.viewer.LeaveViewerResult;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The model class to validate the leave as viewer from a stream group LeaveViewerQuery{@link
 * io.isometrik.gs.builder.viewer.LeaveViewerQuery} request and parse the response(both success or
 * error) to return leave viewer result LeaveViewerResult{@link io.isometrik.gs.response.viewer.LeaveViewerResult}
 * or error received.
 *
 * @see io.isometrik.gs.builder.viewer.LeaveViewerQuery
 * @see io.isometrik.gs.response.viewer.LeaveViewerResult
 */
public class LeaveViewer {
  /**
   * Validate params.
   *
   * @param leaveViewerQuery the leave viewer request query
   * @param completionHandler the leave as viewer request completion handler
   * @param retrofitManager the retrofit manager to make remote api calls
   * @param imConfiguration the isometrik configuration instance
   * @param baseResponse the base response instance to handle non 200 http response code messages
   * @param gson the gson instance
   * @see io.isometrik.gs.builder.viewer.LeaveViewerQuery
   * @see io.isometrik.gs.response.CompletionHandler
   * @see io.isometrik.gs.managers.RetrofitManager
   * @see io.isometrik.gs.IMConfiguration
   * @see io.isometrik.gs.response.error.BaseResponse
   * @see com.google.gson.Gson
   */
  public void validateParams(@NotNull LeaveViewerQuery leaveViewerQuery,
      @NotNull final CompletionHandler<LeaveViewerResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

    String streamId = leaveViewerQuery.getStreamId();
    String viewerId = leaveViewerQuery.getViewerId();

    if (streamId == null || streamId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_STREAMID_MISSING);
    } else if (viewerId == null || viewerId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_VIEWERID_MISSING);
    } else {
      Map<String, String> headers = new HashMap<>();
      headers.put("licenseKey", imConfiguration.getLicenseKey());

      Call<LeaveViewerResult> call = retrofitManager.getViewerService()
          .leaveViewer(headers, imConfiguration.getAccountId(), imConfiguration.getProjectId(),
              imConfiguration.getKeysetId(), streamId, viewerId);
      call.enqueue(new Callback<LeaveViewerResult>() {
        @Override
        public void onResponse(@NotNull Call<LeaveViewerResult> call,
            @NotNull Response<LeaveViewerResult> response) {

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
        public void onFailure(@NotNull Call<LeaveViewerResult> call, @NotNull Throwable t) {
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