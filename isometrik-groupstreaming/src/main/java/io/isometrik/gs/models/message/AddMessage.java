package io.isometrik.gs.models.message;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.isometrik.gs.IMConfiguration;
import io.isometrik.gs.builder.message.SendMessageQuery;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.ErrorResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.error.IsometrikErrorBuilder;
import io.isometrik.gs.response.message.SendMessageResult;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * The model class to validate the send a message in a stream group SendMessageQuery{@link
 * io.isometrik.gs.builder.message.SendMessageQuery} request and parse the response(both success or
 * error) to return send message result SendMessageResult{@link io.isometrik.gs.response.message.SendMessageResult}
 * or error received.
 *
 * @see io.isometrik.gs.builder.message.SendMessageQuery
 * @see io.isometrik.gs.response.message.SendMessageResult
 */
public class AddMessage {

  /**
   * Validate params.
   *
   * @param sendMessageQuery the send message request query
   * @param completionHandler the send message request completion handler
   * @param retrofitManager the retrofit manager to make remote api calls
   * @param imConfiguration the isometrik configuration instance
   * @param baseResponse the base response instance to handle non 200 http response code messages
   * @param gson the gson instance
   * @see io.isometrik.gs.builder.message.SendMessageQuery
   * @see io.isometrik.gs.response.CompletionHandler
   * @see io.isometrik.gs.managers.RetrofitManager
   * @see io.isometrik.gs.IMConfiguration
   * @see io.isometrik.gs.response.error.BaseResponse
   * @see com.google.gson.Gson
   */
  public void validateParams(@NotNull SendMessageQuery sendMessageQuery,
      @NotNull final CompletionHandler<SendMessageResult> completionHandler,
      @NotNull RetrofitManager retrofitManager, @NotNull IMConfiguration imConfiguration,
      @NotNull final BaseResponse baseResponse, final @NotNull Gson gson) {

    String streamId = sendMessageQuery.getStreamId();
    String senderId = sendMessageQuery.getSenderId();
    String senderName = sendMessageQuery.getSenderName();
    String senderIdentifier = sendMessageQuery.getSenderIdentifier();
    String senderImage = sendMessageQuery.getSenderImage();

    String message = sendMessageQuery.getMessage();
    int messageTpe = sendMessageQuery.getMessageType();

    if (streamId == null || streamId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_STREAMID_MISSING);
    } else if (senderId == null || senderId.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_SENDERID_MISSING);
    } else if (senderName == null || senderName.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_SENDER_NAME_MISSING);
    } else if (senderIdentifier == null || senderIdentifier.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_SENDER_IDENTIFIER_MISSING);
    } else if (senderImage == null || senderImage.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_SENDER_IMAGE_MISSING);
    } else if (message == null || message.isEmpty()) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MESSAGE_MISSING);
    } else if (messageTpe < 0) {
      completionHandler.onComplete(null, IsometrikErrorBuilder.IMERROBJ_MESSAGE_TYPE_INVALID_VALUE);
    } else {
      Map<String, String> headers = new HashMap<>();
      headers.put("licenseKey", imConfiguration.getLicenseKey());

      Map<String, Object> body = new HashMap<>();
      body.put("accountId", imConfiguration.getAccountId());
      body.put("projectId", imConfiguration.getProjectId());
      body.put("keysetId", imConfiguration.getKeysetId());
      body.put("streamId", streamId);
      body.put("senderId", senderId);
      body.put("senderName", senderName);
      body.put("senderIdentifier", senderIdentifier);
      body.put("senderImage", senderImage);
      body.put("message", message);
      body.put("messageType", messageTpe);

      Call<SendMessageResult> call = retrofitManager.getMessageService().sendMessage(headers, body);
      call.enqueue(new Callback<SendMessageResult>() {
        @Override
        public void onResponse(@NotNull Call<SendMessageResult> call,
            @NotNull Response<SendMessageResult> response) {

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
        public void onFailure(@NotNull Call<SendMessageResult> call, @NotNull Throwable t) {
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
