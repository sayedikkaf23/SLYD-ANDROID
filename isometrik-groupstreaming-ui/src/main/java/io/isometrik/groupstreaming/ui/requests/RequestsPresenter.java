package io.isometrik.groupstreaming.ui.requests;

import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.utils.StreamDialogEnum;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.copublish.AcceptCopublishRequestQuery;
import io.isometrik.gs.builder.copublish.DenyCopublishRequestQuery;
import io.isometrik.gs.builder.copublish.FetchCopublishRequestsQuery;
import io.isometrik.gs.callbacks.CopublishEventCallback;
import io.isometrik.gs.events.copublish.CopublishRequestAcceptEvent;
import io.isometrik.gs.events.copublish.CopublishRequestAddEvent;
import io.isometrik.gs.events.copublish.CopublishRequestDenyEvent;
import io.isometrik.gs.events.copublish.CopublishRequestRemoveEvent;
import io.isometrik.gs.events.copublish.CopublishRequestSwitchProfileEvent;
import io.isometrik.gs.response.copublish.FetchCopublishRequestsResult;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;

/**
 * The viewers presenter listens for the requests, members events.Fetches list
 * of requests in a stream group.Accepts or declines a request in a stream group.
 * It implements RequestsContract.Presenter{@link RequestsContract.Presenter}
 *
 * @see RequestsContract.Presenter
 */
public class RequestsPresenter implements RequestsContract.Presenter {

  /**
   * Instantiates a new request presenter.
   */
  RequestsPresenter() {
  }

  private RequestsContract.View requestsView;

  private String streamId = "";
  private boolean initiator;
  private boolean isLoading;
  private Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();

  @Override
  public void attachView(RequestsContract.View requestsView) {
    this.requestsView = requestsView;
  }

  @Override
  public void detachView() {
    this.requestsView = null;
  }

  /**
   * {@link RequestsContract.Presenter#initialize(String, boolean)}
   */
  @Override
  public void initialize(String streamId, boolean initiator) {
    this.initiator = initiator;
    this.streamId = streamId;
  }

  /**
   * @see CopublishEventCallback
   */
  private CopublishEventCallback copublishEventCallback = new CopublishEventCallback() {
    @Override
    public void copublishRequestAccepted(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestAcceptEvent copublishRequestAcceptEvent) {
      if (copublishRequestAcceptEvent.getStreamId().equals(streamId)) {
        if (requestsView != null) {
          requestsView.onCopublishRequestAccepted(copublishRequestAcceptEvent.getUserId());
        }
      }
    }

    @Override
    public void copublishRequestDenied(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestDenyEvent copublishRequestDenyEvent) {
      if (copublishRequestDenyEvent.getStreamId().equals(streamId)) {
        if (requestsView != null) {
          requestsView.onCopublishRequestDeclined(copublishRequestDenyEvent.getUserId());
        }
      }
    }

    @Override
    public void copublishRequestAdded(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestAddEvent copublishRequestAddEvent) {
      if (copublishRequestAddEvent.getStreamId().equals(streamId)) {
        if (requestsView != null) {
          requestsView.addRequestEvent(new RequestsModel(copublishRequestAddEvent, initiator));
        }
      }
    }

    @Override
    public void copublishRequestRemoved(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestRemoveEvent copublishRequestRemoveEvent) {
      if (copublishRequestRemoveEvent.getStreamId().equals(streamId)) {
        if (requestsView != null) {
          requestsView.removeRequestEvent(copublishRequestRemoveEvent.getUserId());
        }
      }
    }

    @Override
    public void switchProfile(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestSwitchProfileEvent copublishRequestSwitchProfileEvent) {
      //TODO Nothing
    }
  };

  /**
   * {@link RequestsContract.Presenter#registerStreamRequestsEventListener()}
   */
  @Override
  public void registerStreamRequestsEventListener() {
    isometrik.addCopublishEventListener(copublishEventCallback);
  }

  /**
   * {@link RequestsContract.Presenter#unregisterStreamRequestsEventListener()}
   */
  @Override
  public void unregisterStreamRequestsEventListener() {
    isometrik.removeCopublishEventListener(copublishEventCallback);
  }

  /**
   * {@link RequestsContract.Presenter#requestCopublishRequestsData(String)}
   */
  @Override
  public void requestCopublishRequestsData(String streamId) {

    if (!isLoading) {
      isLoading = true;

      isometrik.fetchCopublishRequests(
          new FetchCopublishRequestsQuery.Builder().setStreamId(streamId).build(), (var1, var2) -> {

            isLoading = false;

            if (var1 != null) {

              ArrayList<RequestsModel> requestsModels = new ArrayList<>();

              ArrayList<FetchCopublishRequestsResult.CopublishRequest> requests =
                  var1.getCopublishRequests();
              int size = requests.size();

              for (int i = 0; i < size; i++) {

                requestsModels.add(new RequestsModel(requests.get(i), initiator));
              }
              if (requestsView != null) {
                requestsView.onCopublishRequestsDataReceived(requestsModels);
              }
            } else {
              if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {
                if (requestsView != null) {   //No requests found
                  requestsView.onCopublishRequestsDataReceived(new ArrayList<>());
                }
              } else if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 0) {
                if (requestsView != null) {  //Stream not live anymore
                  requestsView.onStreamOffline(IsometrikUiSdk.getInstance()
                          .getContext()
                          .getString(R.string.ism_stream_offline),
                      StreamDialogEnum.StreamOffline.getValue());
                }
              } else {
                if (requestsView != null) {
                  requestsView.onError(var2.getErrorMessage());
                }
              }
            }
          });
    }
  }

  /**
   * {@link RequestsContract.Presenter#acceptCopublishRequest(String)}
   */
  @Override
  public void acceptCopublishRequest(String userId) {

    isometrik.acceptCopublishRequest(new AcceptCopublishRequestQuery.Builder().setStreamId(streamId)
        .setUserId(userId)
        .setInitiatorId(IsometrikUiSdk.getInstance().getUserSession().getUserId())
        .build(), (var1, var2) -> {

      if (var1 != null) {
        if (requestsView != null) {
          requestsView.onCopublishRequestAccepted(userId);
        }
      } else {
        //Stream not live anymore
        if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 0) {
          if (requestsView != null) {
            requestsView.onStreamOffline(
                IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_stream_offline),
                StreamDialogEnum.StreamOffline.getValue());
          }
        } else {
          if (requestsView != null) {
            requestsView.onError(var2.getErrorMessage());
          }
        }
      }
    });
  }

  /**
   * {@link RequestsContract.Presenter#declineCopublishRequest(String)}
   */
  @Override
  public void declineCopublishRequest(String userId) {

    isometrik.denyCopublishRequest(new DenyCopublishRequestQuery.Builder().setStreamId(streamId)
        .setUserId(userId)
        .setInitiatorId(IsometrikUiSdk.getInstance().getUserSession().getUserId())
        .build(), (var1, var2) -> {

      if (var1 != null) {
        if (requestsView != null) {
          requestsView.onCopublishRequestDeclined(userId);
        }
      } else {
        //Stream not live anymore
        if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 0) {
          if (requestsView != null) {
            requestsView.onStreamOffline(
                IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_stream_offline),
                StreamDialogEnum.StreamOffline.getValue());
          }
        } else {
          if (requestsView != null) {
            requestsView.onError(var2.getErrorMessage());
          }
        }
      }
    });
  }
}