package io.isometrik.gs.models.viewer;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.viewer.RemoveViewerQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.ErrorResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.error.IsometrikErrorBuilder;
import io.isometrik.gs.response.viewer.RemoveViewerResult;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The model class to validate the remove a viewer from a stream group RemoveViewerQuery{@link
 * io.isometrik.gs.builder.viewer.RemoveViewerQuery} request and parse the response(both success or
 * error) to return remove viewer result RemoveViewerResult{@link io.isometrik.gs.response.viewer.RemoveViewerResult}
 * or error received.
 *
 * @see io.isometrik.gs.builder.viewer.RemoveViewerQuery
 * @see io.isometrik.gs.response.viewer.RemoveViewerResult
 */
public class RemoveViewer {

  /**
   * Validate params.
   *
   * @param removeViewerQuery the remove viewer request query
   * @param completionHandler the remove a viewer request completion handler
   * @param retrofitManager the retrofit manager to make remote api calls
   * @param imConfiguration the isometrik configuration instance
   * @param baseResponse the base response instance to handle non 200 http response code messages
   * @param gson the gson instance
   * @see io.isometrik.gs.builder.viewer.RemoveViewerQuery
   * @see io.isometrik.gs.response.CompletionHandler
   * @see io.isometrik.gs.managers.RetrofitManager
   * @see io.isometrik.gs.IMConfiguration
   * @see io.isometrik.gs.response.error.BaseResponse
   * @see com.google.gson.Gson
   */
  public void validateParams(@NotNull RemoveViewerQuery removeViewerQuery,
      @NotNull final CompletionHandler<RemoveViewerResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

    String streamId = removeViewerQuery.getStreamId();
    String viewerId = removeViewerQuery.getViewerId();
    String initiatorId = removeViewerQuery.getInitiatorId();

    if (streamId == null || streamId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_STREAMID_MISSING);
    } else if (viewerId == null || viewerId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_VIEWERID_MISSING);
    } else if (initiatorId == null || initiatorId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_INITIATORID_MISSING);
    } else {
      Map<String, String> headers = new HashMap<>();
      headers.put("licenseKey", imConfiguration.getLicenseKey());

      Call<RemoveViewerResult> call = retrofitManager.getViewerService()
          .removeViewer(headers, imConfiguration.getAccountId(), imConfiguration.getProjectId(),
              imConfiguration.getKeysetId(), streamId, initiatorId, viewerId);
      call.enqueue(new Callback<RemoveViewerResult>() {
        @Override
        public void onResponse(@NotNull Call<RemoveViewerResult> call,
            @NotNull Response<RemoveViewerResult> response) {

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
        public void onFailure(@NotNull Call<RemoveViewerResult> call, @NotNull Throwable t) {
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
