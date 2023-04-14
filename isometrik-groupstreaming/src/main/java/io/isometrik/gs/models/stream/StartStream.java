package io.isometrik.gs.models.stream;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.stream.StartStreamQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.ErrorResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.error.IsometrikErrorBuilder;
import io.isometrik.gs.response.stream.StartStreamResult;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The model class to validate the create stream group StartStreamQuery{@link
 * io.isometrik.gs.builder.stream.StartStreamQuery} request and parse the response(both success or
 * error) to return start stream result StartStreamResult{@link io.isometrik.gs.response.stream.StartStreamResult}
 * or error received.
 *
 * @see io.isometrik.gs.builder.stream.StartStreamQuery
 * @see io.isometrik.gs.response.stream.StartStreamResult
 */
public class StartStream {

  /**
   * Validate params.
   *
   * @param startStreamQuery the start stream request query
   * @param completionHandler the create stream group request completion handler
   * @param retrofitManager the retrofit manager to make remote api calls
   * @param imConfiguration the isometrik configuration instance
   * @param baseResponse the base response instance to handle non 200 http response code messages
   * @param gson the gson instance
   * @see io.isometrik.gs.builder.stream.StartStreamQuery
   * @see io.isometrik.gs.response.CompletionHandler
   * @see io.isometrik.gs.managers.RetrofitManager
   * @see io.isometrik.gs.IMConfiguration
   * @see io.isometrik.gs.response.error.BaseResponse
   * @see com.google.gson.Gson
   */
  public void validateParams(@NotNull StartStreamQuery startStreamQuery,
      @NotNull final CompletionHandler<StartStreamResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

    String createdBy = startStreamQuery.getCreatedBy();
    String streamImage = startStreamQuery.getStreamImage();
    String streamDescription = startStreamQuery.getStreamDescription();
    Boolean isPublic = startStreamQuery.isPublic();
    List<String> members = startStreamQuery.getMembers();

    if (createdBy == null || createdBy.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_CREATED_BY_MISSING);
    } else if (streamImage == null || streamImage.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_STREAM_IMAGE_MISSING);
    } else if (streamDescription == null || streamDescription.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_STREAM_DESCRIPTION_MISSING);
    } else if (members == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_STREAM_MEMBERS_MISSING);
    } else if (isPublic == null) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_IS_PUBLIC_MISSING);
    } else {
      Map<String, String> headers = new HashMap<>();
      headers.put("licenseKey", imConfiguration.getLicenseKey());

      Map<String, Object> body = new HashMap<>();
      body.put("accountId", imConfiguration.getAccountId());
      body.put("projectId", imConfiguration.getProjectId());
      body.put("keysetId", imConfiguration.getKeysetId());
      body.put("createdBy", createdBy);
      body.put("streamImage", streamImage);
      body.put("streamDescription", streamDescription);
      body.put("members", members);
      body.put("isPublic", isPublic);

      Call<StartStreamResult> call = retrofitManager.getStreamService().startStream(headers, body);
      call.enqueue(new Callback<StartStreamResult>() {
        @Override
        public void onResponse(@NotNull Call<StartStreamResult> call,
            @NotNull Response<StartStreamResult> response) {

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
        public void onFailure(@NotNull Call<StartStreamResult> call, @NotNull Throwable t) {
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
