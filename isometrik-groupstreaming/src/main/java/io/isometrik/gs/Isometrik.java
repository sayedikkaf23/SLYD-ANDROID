package io.isometrik.gs;

import ai.deepar.ar.DeepAR;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;
import io.isometrik.gs.builder.copublish.AcceptCopublishRequestQuery;
import io.isometrik.gs.builder.copublish.AddCopublishRequestQuery;
import io.isometrik.gs.builder.copublish.DeleteCopublishRequestQuery;
import io.isometrik.gs.builder.copublish.DenyCopublishRequestQuery;
import io.isometrik.gs.builder.copublish.FetchCopublishRequestStatusQuery;
import io.isometrik.gs.builder.copublish.FetchCopublishRequestsQuery;
import io.isometrik.gs.builder.copublish.SwitchProfileQuery;
import io.isometrik.gs.builder.member.AddMemberQuery;
import io.isometrik.gs.builder.member.FetchMembersQuery;
import io.isometrik.gs.builder.member.LeaveMemberQuery;
import io.isometrik.gs.builder.member.RemoveMemberQuery;
import io.isometrik.gs.builder.message.DeleteMessageQuery;
import io.isometrik.gs.builder.message.FetchMessagesQuery;
import io.isometrik.gs.builder.message.SendMessageQuery;
import io.isometrik.gs.builder.stream.FetchStreamsQuery;
import io.isometrik.gs.builder.stream.StartStreamQuery;
import io.isometrik.gs.builder.stream.StopStreamQuery;
import io.isometrik.gs.builder.stream.UpdateStreamPublishingStatusQuery;
import io.isometrik.gs.builder.stream.UpdateUserPublishingStatusQuery;
import io.isometrik.gs.builder.subscription.AddSubscriptionQuery;
import io.isometrik.gs.builder.subscription.RemoveSubscriptionQuery;
import io.isometrik.gs.builder.user.AddUserQuery;
import io.isometrik.gs.builder.user.DeleteUserQuery;
import io.isometrik.gs.builder.user.FetchUserDetailsQuery;
import io.isometrik.gs.builder.user.FetchUsersQuery;
import io.isometrik.gs.builder.user.UpdateUserQuery;
import io.isometrik.gs.builder.viewer.AddViewerQuery;
import io.isometrik.gs.builder.viewer.FetchViewersCountQuery;
import io.isometrik.gs.builder.viewer.FetchViewersQuery;
import io.isometrik.gs.builder.viewer.LeaveViewerQuery;
import io.isometrik.gs.builder.viewer.RemoveViewerQuery;
import io.isometrik.gs.callbacks.ConnectionEventCallback;
import io.isometrik.gs.callbacks.CopublishEventCallback;
import io.isometrik.gs.callbacks.MemberEventCallback;
import io.isometrik.gs.callbacks.MessageEventCallback;
import io.isometrik.gs.callbacks.PresenceEventCallback;
import io.isometrik.gs.callbacks.StreamEventCallback;
import io.isometrik.gs.callbacks.ViewerEventCallback;
import io.isometrik.gs.listeners.ConnectionListenerManager;
import io.isometrik.gs.listeners.CopublishListenerManager;
import io.isometrik.gs.listeners.MemberListenerManager;
import io.isometrik.gs.listeners.MessageListenerManager;
import io.isometrik.gs.listeners.PresenceListenerManager;
import io.isometrik.gs.listeners.StreamListenerManager;
import io.isometrik.gs.listeners.ViewerListenerManager;
import io.isometrik.gs.managers.BasePathManager;
import io.isometrik.gs.managers.RetrofitManager;
import io.isometrik.gs.models.copublish.AcceptCopublishRequest;
import io.isometrik.gs.models.copublish.AddCopublishRequest;
import io.isometrik.gs.models.copublish.DeleteCopublishRequest;
import io.isometrik.gs.models.copublish.DenyCopublishRequest;
import io.isometrik.gs.models.copublish.FetchCopublishRequestStatus;
import io.isometrik.gs.models.copublish.FetchCopublishRequests;
import io.isometrik.gs.models.copublish.SwitchProfile;
import io.isometrik.gs.models.events.MessageEvents;
import io.isometrik.gs.models.events.PresenceEvents;
import io.isometrik.gs.models.member.AddMember;
import io.isometrik.gs.models.member.FetchMembers;
import io.isometrik.gs.models.member.LeaveMember;
import io.isometrik.gs.models.member.RemoveMember;
import io.isometrik.gs.models.message.AddMessage;
import io.isometrik.gs.models.message.DeleteMessage;
import io.isometrik.gs.models.message.FetchMessages;
import io.isometrik.gs.models.stream.FetchStreams;
import io.isometrik.gs.models.stream.StartStream;
import io.isometrik.gs.models.stream.StopStream;
import io.isometrik.gs.models.stream.UpdateStreamPublishStatus;
import io.isometrik.gs.models.stream.UpdateUserPublishStatus;
import io.isometrik.gs.models.subscription.AddSubscription;
import io.isometrik.gs.models.subscription.RemoveSubscription;
import io.isometrik.gs.models.user.AddUser;
import io.isometrik.gs.models.user.DeleteUser;
import io.isometrik.gs.models.user.FetchUserDetails;
import io.isometrik.gs.models.user.FetchUsers;
import io.isometrik.gs.models.user.UpdateUser;
import io.isometrik.gs.models.viewer.AddViewer;
import io.isometrik.gs.models.viewer.FetchViewers;
import io.isometrik.gs.models.viewer.FetchViewersCount;
import io.isometrik.gs.models.viewer.LeaveViewer;
import io.isometrik.gs.models.viewer.RemoveViewer;
import io.isometrik.gs.network.ConnectivityReceiver;
import io.isometrik.gs.network.IsometrikConnection;
import io.isometrik.gs.response.CompletionHandler;
import io.isometrik.gs.response.copublish.AcceptCopublishRequestResult;
import io.isometrik.gs.response.copublish.AddCopublishRequestResult;
import io.isometrik.gs.response.copublish.DeleteCopublishRequestResult;
import io.isometrik.gs.response.copublish.DenyCopublishRequestResult;
import io.isometrik.gs.response.copublish.FetchCopublishRequestStatusResult;
import io.isometrik.gs.response.copublish.FetchCopublishRequestsResult;
import io.isometrik.gs.response.copublish.SwitchProfileResult;
import io.isometrik.gs.response.error.BaseResponse;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.member.AddMemberResult;
import io.isometrik.gs.response.member.FetchMembersResult;
import io.isometrik.gs.response.member.LeaveMemberResult;
import io.isometrik.gs.response.member.RemoveMemberResult;
import io.isometrik.gs.response.message.DeleteMessageResult;
import io.isometrik.gs.response.message.FetchMessagesResult;
import io.isometrik.gs.response.message.SendMessageResult;
import io.isometrik.gs.response.stream.FetchStreamsResult;
import io.isometrik.gs.response.stream.StartStreamResult;
import io.isometrik.gs.response.stream.StopStreamResult;
import io.isometrik.gs.response.stream.UpdateStreamPublishingStatusResult;
import io.isometrik.gs.response.stream.UpdateUserPublishingStatusResult;
import io.isometrik.gs.response.subscription.AddSubscriptionResult;
import io.isometrik.gs.response.subscription.RemoveSubscriptionResult;
import io.isometrik.gs.response.user.AddUserResult;
import io.isometrik.gs.response.user.DeleteUserResult;
import io.isometrik.gs.response.user.FetchUserDetailsResult;
import io.isometrik.gs.response.user.FetchUsersResult;
import io.isometrik.gs.response.user.UpdateUserResult;
import io.isometrik.gs.response.viewer.AddViewerResult;
import io.isometrik.gs.response.viewer.FetchViewersCountResult;
import io.isometrik.gs.response.viewer.FetchViewersResult;
import io.isometrik.gs.response.viewer.LeaveViewerResult;
import io.isometrik.gs.response.viewer.RemoveViewerResult;
import io.isometrik.gs.rtcengine.ar.ARInitializeListener;
import io.isometrik.gs.rtcengine.ar.AROperations;
import io.isometrik.gs.rtcengine.ar.capture.ImageCaptureCallbacks;
import io.isometrik.gs.rtcengine.rtc.BroadcastOperations;
import io.isometrik.gs.rtcengine.rtc.IsometrikRtcEngineEventHandler;
import io.isometrik.gs.rtcengine.rtc.IsometrikRtcEventHandler;
import io.isometrik.gs.rtcengine.rtc.RemoteMediaStateEventsHandler;
import io.isometrik.gs.rtcengine.stats.IsometrikRtcStatsManager;
import io.isometrik.gs.rtcengine.utils.FileUtil;
import io.isometrik.gs.rtcengine.voice.VoiceOperations;
import org.jetbrains.annotations.NotNull;

/**
 * The type Isometrik.
 */
public class Isometrik {

  private IMConfiguration configuration;

  private MemberListenerManager memberListenerManager;
  private MessageListenerManager messageListenerManager;
  private StreamListenerManager streamListenerManager;
  private PresenceListenerManager presenceListenerManager;
  private ViewerListenerManager viewerListenerManager;
  private CopublishListenerManager copublishListenerManager;
  private ConnectionListenerManager connectionListenerManager;
  /**
   * Model classes for members
   */
  private AddMember addMember;
  private FetchMembers fetchMembers;
  private LeaveMember leaveMember;
  private RemoveMember removeMember;

  /**
   * Model classes for messages
   */
  private AddMessage addMessage;
  private DeleteMessage deleteMessage;
  private FetchMessages fetchMessages;

  /**
   * Model classes for streams
   */
  private FetchStreams fetchStreams;
  private StartStream startStream;
  private StopStream stopStream;
  private UpdateStreamPublishStatus updateStreamPublishStatus;
  private UpdateUserPublishStatus updateUserPublishStatus;

  /**
   * Model classes for user
   */
  private AddUser addUser;
  private DeleteUser deleteUser;
  private FetchUserDetails fetchUserDetails;
  private FetchUsers fetchUsers;
  private UpdateUser updateUser;
  /**
   * Model classes for viewer
   */
  private AddViewer addViewer;
  private FetchViewers fetchViewers;
  private FetchViewersCount fetchViewersCount;
  private LeaveViewer leaveViewer;
  private RemoveViewer removeViewer;

  /**
   * Model classes for subscription
   */
  private AddSubscription addSubscription;
  private RemoveSubscription removeSubscription;

  /**
   * Model classes for copublish
   */
  private AcceptCopublishRequest acceptCopublishRequest;
  private AddCopublishRequest addCopublishRequest;
  private DenyCopublishRequest denyCopublishRequest;
  private SwitchProfile switchProfile;
  private DeleteCopublishRequest deleteCopublishRequest;
  private FetchCopublishRequests fetchCopublishRequests;
  private FetchCopublishRequestStatus fetchCopublishRequestStatus;

  /**
   * Model class for error handling
   */
  private BaseResponse baseResponse;

  private BasePathManager basePathManager;
  private RetrofitManager retrofitManager;

  private IsometrikConnection isometrikConnection;
  /**
   * Model classes for event handling
   */
  private MessageEvents messageEvents;
  private PresenceEvents presenceEvents;

  /**
   * For paring json response
   */
  private Gson gson;

  /**
   * Initialize IsometrikRTC Engine
   */
  private RtcEngine mRtcEngine;
  private IsometrikRtcEngineEventHandler isometrikRtcEngineEventHandler;
  private IsometrikRtcStatsManager isometrikRtcStatsManager;
  private BroadcastOperations broadcastOperations;
  private VoiceOperations voiceOperations;

  /**
   * Initialize Ar Engine
   */
  private DeepAR deepAR;
  private boolean arFiltersEnabled;
  private AROperations arOperations;
  private ImageCaptureCallbacks imageCaptureCallbacks;
  private static final String ISOMETRIK_SUCCESS_TAG = "isometrik-success-logs";
  private static final String ISOMETRIK_ERROR_TAG = "isometrik-error-logs";

  /**
   * Instantiates a new Isometrik.
   *
   * @param initialConfig the initial config
   */
  public Isometrik(@NotNull IMConfiguration initialConfig) {
    configuration = initialConfig;

    basePathManager = new BasePathManager(initialConfig);
    retrofitManager = new RetrofitManager(this);

    memberListenerManager = new MemberListenerManager(this);
    messageListenerManager = new MessageListenerManager(this);
    streamListenerManager = new StreamListenerManager(this);
    viewerListenerManager = new ViewerListenerManager(this);
    presenceListenerManager = new PresenceListenerManager(this);
    copublishListenerManager = new CopublishListenerManager(this);
    connectionListenerManager = new ConnectionListenerManager(this);

    messageEvents = new MessageEvents();
    presenceEvents = new PresenceEvents();

    isometrikConnection = new IsometrikConnection(this);

    addMember = new AddMember();
    fetchMembers = new FetchMembers();
    leaveMember = new LeaveMember();
    removeMember = new RemoveMember();

    fetchStreams = new FetchStreams();
    startStream = new StartStream();
    stopStream = new StopStream();
    updateStreamPublishStatus = new UpdateStreamPublishStatus();
    updateUserPublishStatus = new UpdateUserPublishStatus();

    addMessage = new AddMessage();
    deleteMessage = new DeleteMessage();
    fetchMessages = new FetchMessages();

    addUser = new AddUser();
    deleteUser = new DeleteUser();
    fetchUserDetails = new FetchUserDetails();
    fetchUsers = new FetchUsers();
    updateUser = new UpdateUser();

    addViewer = new AddViewer();
    fetchViewers = new FetchViewers();
    leaveViewer = new LeaveViewer();
    removeViewer = new RemoveViewer();
    fetchViewersCount = new FetchViewersCount();
    baseResponse = new BaseResponse();

    addSubscription = new AddSubscription();
    removeSubscription = new RemoveSubscription();

    acceptCopublishRequest = new AcceptCopublishRequest();
    addCopublishRequest = new AddCopublishRequest();
    denyCopublishRequest = new DenyCopublishRequest();
    switchProfile = new SwitchProfile();
    deleteCopublishRequest = new DeleteCopublishRequest();
    fetchCopublishRequests = new FetchCopublishRequests();
    fetchCopublishRequestStatus = new FetchCopublishRequestStatus();

    gson = new GsonBuilder().create();

    isometrikRtcEngineEventHandler = new IsometrikRtcEngineEventHandler(this);

    isometrikRtcStatsManager = new IsometrikRtcStatsManager();

    initializeRTCEngine(initialConfig.getContext());

    if (initialConfig.getAREnabled()) {

      initializeArEngine(initialConfig.getContext(), initialConfig.getARFiltersKey());
      arOperations = new AROperations(this);
    }
    registerConnectivityReceiver();
  }

  private void registerConnectivityReceiver() {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      try {

        ConnectivityManager connectivityManager = (ConnectivityManager) configuration.getContext()
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
          connectivityManager.registerNetworkCallback(new NetworkRequest.Builder().build(),
              new ConnectivityManager.NetworkCallback() {

                @Override
                public void onAvailable(@NotNull Network network) {

                  reConnect();
                }

                @Override
                public void onLost(@NotNull Network network) {

                }
              });
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      IntentFilter filter = new IntentFilter();
      filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
      configuration.getContext().registerReceiver(new ConnectivityReceiver(this), filter);
    }
  }

  /**
   * Sets configuration.
   *
   * @param configuration the configuration
   */
  public void setConfiguration(@NotNull IMConfiguration configuration) {
    this.configuration = configuration;
  }

  /**
   * Gets gson.
   *
   * @return the gson
   */
  public Gson getGson() {
    return gson;
  }

  /**
   * Gets isometrik success tag.
   *
   * @return the isometrik success tag
   */
  public static String getIsometrikSuccessTag() {
    return ISOMETRIK_SUCCESS_TAG;
  }

  /**
   * Gets isometrik error tag.
   *
   * @return the isometrik error tag
   */
  public static String getIsometrikErrorTag() {
    return ISOMETRIK_ERROR_TAG;
  }

  /**
   * Gets base url.
   *
   * @return the base url
   */
  @NotNull
  public String getBaseUrl() {
    return this.basePathManager.getBasePath();
  }

  /**
   * Gets connection base url.
   *
   * @return the connection base url
   */
  @NotNull
  public String getConnectionBaseUrl() {
    return this.basePathManager.getConnectionsBasePath();
  }

  /**
   * Gets configuration.
   *
   * @return the configuration
   */
  public IMConfiguration getConfiguration() {
    return configuration;
  }

  /**
   * Gets member listener manager.
   *
   * @return the member listener manager
   */
  public MemberListenerManager getMemberListenerManager() {
    return memberListenerManager;
  }

  /**
   * Gets message listener manager.
   *
   * @return the message listener manager
   */
  public MessageListenerManager getMessageListenerManager() {
    return messageListenerManager;
  }

  /**
   * Gets stream listener manager.
   *
   * @return the stream listener manager
   */
  public StreamListenerManager getStreamListenerManager() {
    return streamListenerManager;
  }

  /**
   * Gets presence listener manager.
   *
   * @return the presence listener manager
   */
  public PresenceListenerManager getPresenceListenerManager() {
    return presenceListenerManager;
  }

  /**
   * Gets viewer listener manager.
   *
   * @return the viewer listener manager
   */
  public ViewerListenerManager getViewerListenerManager() {
    return viewerListenerManager;
  }

  /**
   * Gets copublish listener manager.
   *
   * @return the copublish listener manager
   */
  public CopublishListenerManager getCopublishListenerManager() {
    return copublishListenerManager;
  }

  /**
   * Gets connection listener manager.
   *
   * @return the connection listener manager
   */
  public ConnectionListenerManager getConnectionListenerManager() {
    return connectionListenerManager;
  }

  /**
   * Gets message events.
   *
   * @return the message events
   */
  public MessageEvents getMessageEvents() {
    return messageEvents;
  }

  /**
   * Gets presence events.
   *
   * @return the presence events
   */
  public PresenceEvents getPresenceEvents() {
    return presenceEvents;
  }

  /**
   * Add member event listener.
   *
   * @param listener the listener
   */
  public void addMemberEventListener(@NotNull MemberEventCallback listener) {
    memberListenerManager.addListener(listener);
  }

  /**
   * Remove member event listener.
   *
   * @param listener the listener
   */
  public void removeMemberEventListener(@NotNull MemberEventCallback listener) {
    memberListenerManager.removeListener(listener);
  }

  /**
   * Add stream event listener.
   *
   * @param listener the listener
   */
  public void addStreamEventListener(@NotNull StreamEventCallback listener) {
    streamListenerManager.addListener(listener);
  }

  /**
   * Remove stream event listener.
   *
   * @param listener the listener
   */
  public void removeStreamEventListener(@NotNull StreamEventCallback listener) {
    streamListenerManager.removeListener(listener);
  }

  /**
   * Add message event listener.
   *
   * @param listener the listener
   */
  public void addMessageEventListener(@NotNull MessageEventCallback listener) {
    messageListenerManager.addListener(listener);
  }

  /**
   * Remove message event listener.
   *
   * @param listener the listener
   */
  public void removeMessageEventListener(@NotNull MessageEventCallback listener) {
    messageListenerManager.removeListener(listener);
  }

  /**
   * Add viewer event listener.
   *
   * @param listener the listener
   */
  public void addViewerEventListener(@NotNull ViewerEventCallback listener) {
    viewerListenerManager.addListener(listener);
  }

  /**
   * Remove viewer event listener.
   *
   * @param listener the listener
   */
  public void removeViewerEventListener(@NotNull ViewerEventCallback listener) {
    viewerListenerManager.removeListener(listener);
  }

  /**
   * Add presence event listener.
   *
   * @param listener the listener
   */
  public void addPresenceEventListener(@NotNull PresenceEventCallback listener) {
    presenceListenerManager.addListener(listener);
  }

  /**
   * Remove presence event listener.
   *
   * @param listener the listener
   */
  public void removePresenceEventListener(@NotNull PresenceEventCallback listener) {
    presenceListenerManager.removeListener(listener);
  }

  /**
   * Add copublish event listener.
   *
   * @param listener the listener
   */
  public void addCopublishEventListener(@NotNull CopublishEventCallback listener) {
    copublishListenerManager.addListener(listener);
  }

  /**
   * Remove copublish event listener.
   *
   * @param listener the listener
   */
  public void removeCopublishEventListener(@NotNull CopublishEventCallback listener) {
    copublishListenerManager.removeListener(listener);
  }

  /**
   * Add connection event listener.
   *
   * @param listener the listener
   */
  public void addConnectionEventListener(@NotNull ConnectionEventCallback listener) {
    connectionListenerManager.addListener(listener);
  }

  /**
   * Remove connection event listener.
   *
   * @param listener the listener
   */
  public void removeConnectionEventListener(@NotNull ConnectionEventCallback listener) {
    connectionListenerManager.removeListener(listener);
  }

  /**
   * Add member.
   *
   * @param addMemberQuery the add member query
   * @param completionHandler the completion handler
   */
  public void addMember(@NotNull AddMemberQuery addMemberQuery,
      @NotNull CompletionHandler<AddMemberResult> completionHandler) {

    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      addMember.validateParams(addMemberQuery, completionHandler, retrofitManager, configuration,
          baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch members.
   *
   * @param fetchMembersQuery the fetch members query
   * @param completionHandler the completion handler
   */
  public void fetchMembers(@NotNull FetchMembersQuery fetchMembersQuery,
      @NotNull CompletionHandler<FetchMembersResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      fetchMembers.validateParams(fetchMembersQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Leave member.
   *
   * @param leaveMemberQuery the leave member query
   * @param completionHandler the completion handler
   */
  public void leaveMember(@NotNull LeaveMemberQuery leaveMemberQuery,
      @NotNull CompletionHandler<LeaveMemberResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      leaveMember.validateParams(leaveMemberQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Remove member.
   *
   * @param removeMemberQuery the remove member query
   * @param completionHandler the completion handler
   */
  public void removeMember(@NotNull RemoveMemberQuery removeMemberQuery,
      @NotNull CompletionHandler<RemoveMemberResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      removeMember.validateParams(removeMemberQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Delete message.
   *
   * @param deleteMessageQuery the delete message query
   * @param completionHandler the completion handler
   */
  public void deleteMessage(@NotNull DeleteMessageQuery deleteMessageQuery,
      @NotNull CompletionHandler<DeleteMessageResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      deleteMessage.validateParams(deleteMessageQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch messages.
   *
   * @param fetchMessagesQuery the fetch messages query
   * @param completionHandler the completion handler
   */
  public void fetchMessages(@NotNull FetchMessagesQuery fetchMessagesQuery,
      @NotNull CompletionHandler<FetchMessagesResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      fetchMessages.validateParams(fetchMessagesQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Send message.
   *
   * @param sendMessageQuery the send message query
   * @param completionHandler the completion handler
   */
  public void sendMessage(@NotNull SendMessageQuery sendMessageQuery,
      @NotNull CompletionHandler<SendMessageResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      addMessage.validateParams(sendMessageQuery, completionHandler, retrofitManager, configuration,
          baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Start stream.
   *
   * @param startStreamQuery the start stream query
   * @param completionHandler the completion handler
   */
  public void startStream(@NotNull StartStreamQuery startStreamQuery,
      @NotNull CompletionHandler<StartStreamResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      startStream.validateParams(startStreamQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Stop stream.
   *
   * @param stopStreamQuery the stop stream query
   * @param completionHandler the completion handler
   */
  public void stopStream(@NotNull StopStreamQuery stopStreamQuery,
      @NotNull CompletionHandler<StopStreamResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      stopStream.validateParams(stopStreamQuery, completionHandler, retrofitManager, configuration,
          baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Update stream publishing status.
   *
   * @param updatePublishingStatusQuery the update publishing status query
   * @param completionHandler the completion handler
   */
  public void updateStreamPublishingStatus(
      @NotNull UpdateStreamPublishingStatusQuery updatePublishingStatusQuery,
      @NotNull CompletionHandler<UpdateStreamPublishingStatusResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      updateStreamPublishStatus.validateParams(updatePublishingStatusQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Update user publishing status.
   *
   * @param updateUserPublishingStatusQuery the update user publishing status query
   * @param completionHandler the completion handler
   */
  public void updateUserPublishingStatus(
      @NotNull UpdateUserPublishingStatusQuery updateUserPublishingStatusQuery,
      @NotNull CompletionHandler<UpdateUserPublishingStatusResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      updateUserPublishStatus.validateParams(updateUserPublishingStatusQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch streams.
   *
   * @param fetchStreamsQuery the fetch streams query
   * @param completionHandler the completion handler
   */
  public void fetchStreams(@NotNull FetchStreamsQuery fetchStreamsQuery,
      @NotNull CompletionHandler<FetchStreamsResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      fetchStreams.validateParams(fetchStreamsQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Add user.
   *
   * @param addUserQuery the add user query
   * @param completionHandler the completion handler
   */
  public void addUser(@NotNull AddUserQuery addUserQuery,
      @NotNull CompletionHandler<AddUserResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      addUser.validateParams(addUserQuery, completionHandler, retrofitManager, configuration,
          baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Delete user.
   *
   * @param deleteUserQuery the delete user query
   * @param completionHandler the completion handler
   */
  public void deleteUser(@NotNull DeleteUserQuery deleteUserQuery,
      @NotNull CompletionHandler<DeleteUserResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      deleteUser.validateParams(deleteUserQuery, completionHandler, retrofitManager, configuration,
          baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch user details.
   *
   * @param fetchUserDetailsQuery the fetch user details query
   * @param completionHandler the completion handler
   */
  public void fetchUserDetails(@NotNull FetchUserDetailsQuery fetchUserDetailsQuery,
      @NotNull CompletionHandler<FetchUserDetailsResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      fetchUserDetails.validateParams(fetchUserDetailsQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch users.
   *
   * @param fetchUsersQuery the fetch users query
   * @param completionHandler the completion handler
   */
  public void fetchUsers(@NotNull FetchUsersQuery fetchUsersQuery,
      @NotNull CompletionHandler<FetchUsersResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      fetchUsers.validateParams(fetchUsersQuery, completionHandler, retrofitManager, configuration,
          baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Update user.
   *
   * @param updateUserQuery the update user query
   * @param completionHandler the completion handler
   */
  public void updateUser(@NotNull UpdateUserQuery updateUserQuery,
      @NotNull CompletionHandler<UpdateUserResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      updateUser.validateParams(updateUserQuery, completionHandler, retrofitManager, configuration,
          baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Add viewer.
   *
   * @param addViewerQuery the add viewer query
   * @param completionHandler the completion handler
   */
  public void addViewer(@NotNull AddViewerQuery addViewerQuery,
      @NotNull CompletionHandler<AddViewerResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      addViewer.validateParams(addViewerQuery, completionHandler, retrofitManager, configuration,
          baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch viewers.
   *
   * @param fetchViewersQuery the fetch viewers query
   * @param completionHandler the completion handler
   */
  public void fetchViewers(@NotNull FetchViewersQuery fetchViewersQuery,
      @NotNull CompletionHandler<FetchViewersResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      fetchViewers.validateParams(fetchViewersQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch viewers count.
   *
   * @param fetchViewersCountQuery the fetch viewers count query
   * @param completionHandler the completion handler
   */
  public void fetchViewersCount(@NotNull FetchViewersCountQuery fetchViewersCountQuery,
      @NotNull CompletionHandler<FetchViewersCountResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      fetchViewersCount.validateParams(fetchViewersCountQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Leave viewer.
   *
   * @param leaveViewerQuery the leave viewer query
   * @param completionHandler the completion handler
   */
  public void leaveViewer(@NotNull LeaveViewerQuery leaveViewerQuery,
      @NotNull CompletionHandler<LeaveViewerResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      leaveViewer.validateParams(leaveViewerQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Remove viewer.
   *
   * @param removeViewerQuery the remove viewer query
   * @param completionHandler the completion handler
   */
  public void removeViewer(@NotNull RemoveViewerQuery removeViewerQuery,
      @NotNull CompletionHandler<RemoveViewerResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      removeViewer.validateParams(removeViewerQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Add subscription.
   *
   * @param addSubscriptionQuery the add subscription query
   * @param completionHandler the completion handler
   */
  public void addSubscription(@NotNull AddSubscriptionQuery addSubscriptionQuery,
      @NotNull CompletionHandler<AddSubscriptionResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      addSubscription.validateParams(addSubscriptionQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Remove subscription.
   *
   * @param removeSubscriptionQuery the remove subscription query
   * @param completionHandler the completion handler
   */
  public void removeSubscription(@NotNull RemoveSubscriptionQuery removeSubscriptionQuery,
      @NotNull CompletionHandler<RemoveSubscriptionResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      removeSubscription.validateParams(removeSubscriptionQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Accept copublish request.
   *
   * @param acceptCopublishRequestQuery the accept copublish request query
   * @param completionHandler the completion handler
   */
  public void acceptCopublishRequest(
      @NotNull AcceptCopublishRequestQuery acceptCopublishRequestQuery,
      @NotNull CompletionHandler<AcceptCopublishRequestResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      acceptCopublishRequest.validateParams(acceptCopublishRequestQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Add copublish request.
   *
   * @param addCopublishRequestQuery the add copublish request query
   * @param completionHandler the completion handler
   */
  public void addCopublishRequest(@NotNull AddCopublishRequestQuery addCopublishRequestQuery,
      @NotNull CompletionHandler<AddCopublishRequestResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      addCopublishRequest.validateParams(addCopublishRequestQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Deny copublish request.
   *
   * @param denyCopublishRequestQuery the deny copublish request query
   * @param completionHandler the completion handler
   */
  public void denyCopublishRequest(@NotNull DenyCopublishRequestQuery denyCopublishRequestQuery,
      @NotNull CompletionHandler<DenyCopublishRequestResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      denyCopublishRequest.validateParams(denyCopublishRequestQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Switch profile from viewer to a broadcaster.
   *
   * @param switchProfileQuery the switch profile query
   * @param completionHandler the completion handler
   */
  public void switchProfile(@NotNull SwitchProfileQuery switchProfileQuery,
      @NotNull CompletionHandler<SwitchProfileResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      switchProfile.validateParams(switchProfileQuery, completionHandler, retrofitManager,
          configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Delete copublish request from a stream group.
   *
   * @param deleteCopublishRequestQuery the delete copublish request query
   * @param completionHandler the completion handler
   */
  public void deleteCopublishRequest(
      @NotNull DeleteCopublishRequestQuery deleteCopublishRequestQuery,
      @NotNull CompletionHandler<DeleteCopublishRequestResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      deleteCopublishRequest.validateParams(deleteCopublishRequestQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch copublish requests in a stream group.
   *
   * @param fetchCopublishRequestsQuery the fetch copublish requests query
   * @param completionHandler the completion handler
   */
  public void fetchCopublishRequests(
      @NotNull FetchCopublishRequestsQuery fetchCopublishRequestsQuery,
      @NotNull CompletionHandler<FetchCopublishRequestsResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      fetchCopublishRequests.validateParams(fetchCopublishRequestsQuery, completionHandler,
          retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Fetch copublish request status in a stream group.
   *
   * @param fetchCopublishRequestStatusQuery the fetch copublish request status query
   * @param completionHandler the completion handler
   */
  public void fetchCopublishRequestStatus(
      @NotNull FetchCopublishRequestStatusQuery fetchCopublishRequestStatusQuery,
      @NotNull CompletionHandler<FetchCopublishRequestStatusResult> completionHandler) {
    IsometrikError isometrikError = configuration.validateConfiguration();
    if (isometrikError == null) {
      fetchCopublishRequestStatus.validateParams(fetchCopublishRequestStatusQuery,
          completionHandler, retrofitManager, configuration, baseResponse, gson);
    } else {
      completionHandler.onComplete(null, isometrikError);
    }
  }

  /**
   * Create connection.
   *
   * @param clientId the client id
   */
  public void createConnection(String clientId) {
    configuration.setClientId(clientId);
    IsometrikError isometrikError = configuration.validateConnectionConfiguration();
    if (isometrikError == null) {
      isometrikConnection.createConnection(this);
    } else {
      connectionListenerManager.announce(isometrikError);
    }
  }

  /**
   * Drop connection.
   */
  public void dropConnection() {
    isometrikConnection.dropConnection();
  }

  /**
   * Re connect.
   */
  public void reConnect() {

    isometrikConnection.reConnect();
  }

  /**
   * Destroy the SDK to cancel all ongoing requests and stop heartbeat timer.
   */
  public void destroy() {
    try {

      isometrikConnection.dropConnection(false);
      retrofitManager.destroy(false);
    } catch (Exception error) {
      error.printStackTrace();
    }
    onTerminate();
  }

  /**
   * Force destroy the SDK to evict the connection pools and close executors.
   */
  public void forceDestroy() {
    try {

      retrofitManager.destroy(true);
      isometrikConnection.dropConnection(true);
    } catch (Exception error) {
      error.printStackTrace();
    }
    onTerminate();
  }

  private void initializeRTCEngine(Context context) {
    try {
      mRtcEngine =
          RtcEngine.create(context, configuration.getRtcAppId(), isometrikRtcEngineEventHandler);
      mRtcEngine.setChannelProfile(io.agora.rtc.Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
      mRtcEngine.enableVideo();
      mRtcEngine.setAudioProfile(Constants.AUDIO_PROFILE_MUSIC_HIGH_QUALITY_STEREO,
          Constants.AUDIO_SCENARIO_DEFAULT);
      mRtcEngine.setLogFile(FileUtil.initializeLogFile(context));
    } catch (Exception e) {
      e.printStackTrace();
    }

    //isometrikRtcStatsManager.enableStats(configuration.isEnableDetailedRtcStats());

    broadcastOperations = new BroadcastOperations(mRtcEngine, context, configuration, this);

    voiceOperations = new VoiceOperations(mRtcEngine);
  }

  private void initializeArEngine(Context context, String arLicenseKey) {

    try {
      deepAR = new DeepAR(context);
      deepAR.setLicenseKey(arLicenseKey);
      deepAR.initialize(context, new ARInitializeListener(this));
      setARFiltersEnabled(true);
    } catch (Exception e) {

      e.printStackTrace();
    }
  }

  /**
   * Sets ar filters enabled.
   *
   * @param arFiltersEnabled the ar filters enabled
   */
  public void setARFiltersEnabled(boolean arFiltersEnabled) {
    this.arFiltersEnabled = arFiltersEnabled;

    //this.arFiltersEnabled = false;
  }

  /**
   * Is ar filters enabled boolean.
   *
   * @return the boolean
   */
  public boolean isARFiltersEnabled() {
    return arFiltersEnabled;
  }

  private void releaseARFilters() {

    try {
      deepAR.release();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets ar operations.
   *
   * @return the ar operations
   */
  public AROperations getAROperations() {
    return arOperations;
  }

  /**
   * Gets ar engine.
   *
   * @return the ar engine
   */
  public DeepAR getAREngine() {
    return deepAR;
  }

  /**
   * Gets rtc engine.
   *
   * @return the rtc engine
   */
  protected RtcEngine getRtcEngine() {
    return mRtcEngine;
  }

  /**
   * Gets broadcast operations.
   *
   * @return the broadcast operations
   */
  public BroadcastOperations getBroadcastOperations() {
    return broadcastOperations;
  }

  /**
   * Gets voice operations.
   *
   * @return the voice operations
   */
  public VoiceOperations getVoiceOperations() {
    return voiceOperations;
  }

  /**
   * Register broadcast rtc event handler.
   *
   * @param handler the handler
   */
  public void registerBroadcastRtcEventHandler(IsometrikRtcEventHandler handler) {
    isometrikRtcEngineEventHandler.addHandler(handler);
  }

  /**
   * Register broadcast rtc event handler.
   *
   * @param handler the handler
   */
  public void registerRemoteMediaEventHandler(RemoteMediaStateEventsHandler handler) {
    isometrikRtcEngineEventHandler.addRemoteMediaStateHandler(handler);
  }

  /**
   * Remove broadcast rtc event handler.
   *
   * @param handler the handler
   */
  public void removeBroadcastRtcEventHandler(IsometrikRtcEventHandler handler) {
    isometrikRtcEngineEventHandler.removeHandler(handler);
  }

  /**
   * Returns isometrik stats manager
   *
   * @return IsometrikRtcStatsManager instance
   * @see io.isometrik.gs.rtcengine.stats.IsometrikRtcStatsManager
   */
  public IsometrikRtcStatsManager getIsometrikRtcStatsManager() {
    return isometrikRtcStatsManager;
  }

  public IsometrikRtcEngineEventHandler getIsometrikRtcEngineEventHandler() {
    return isometrikRtcEngineEventHandler;
  }

  /**
   * On terminate.
   */
  public void onTerminate() {

    RtcEngine.destroy();
    releaseARFilters();
  }

  /**
   * To return the bitmap captured using AR engine
   *
   * @param bitmap captured
   */
  public void imageCaptured(Bitmap bitmap) {
    if (imageCaptureCallbacks != null) {
      imageCaptureCallbacks.imageCaptured(bitmap);
    }
  }

  /**
   * Sets image capture callbacks.
   *
   * @param imageCaptureCallbacks the image capture callbacks
   */
  public void setImageCaptureCallbacks(ImageCaptureCallbacks imageCaptureCallbacks) {
    this.imageCaptureCallbacks = imageCaptureCallbacks;
  }
}
