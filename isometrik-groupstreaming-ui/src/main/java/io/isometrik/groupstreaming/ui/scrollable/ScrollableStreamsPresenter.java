package io.isometrik.groupstreaming.ui.scrollable;

import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.gifts.GiftsModel;
import io.isometrik.groupstreaming.ui.messages.MessagesModel;
import io.isometrik.groupstreaming.ui.streams.grid.StreamsModel;
import io.isometrik.groupstreaming.ui.utils.Constants;
import io.isometrik.groupstreaming.ui.utils.DateUtil;
import io.isometrik.groupstreaming.ui.utils.MessageTypeEnum;
import io.isometrik.groupstreaming.ui.utils.StreamDialogEnum;
import io.isometrik.groupstreaming.ui.utils.UserSession;
import io.isometrik.groupstreaming.ui.viewers.ViewersListModel;
import io.isometrik.gs.Isometrik;
import io.isometrik.gs.builder.copublish.AcceptCopublishRequestQuery;
import io.isometrik.gs.builder.copublish.AddCopublishRequestQuery;
import io.isometrik.gs.builder.copublish.DeleteCopublishRequestQuery;
import io.isometrik.gs.builder.copublish.DenyCopublishRequestQuery;
import io.isometrik.gs.builder.copublish.FetchCopublishRequestStatusQuery;
import io.isometrik.gs.builder.copublish.SwitchProfileQuery;
import io.isometrik.gs.builder.member.FetchMembersQuery;
import io.isometrik.gs.builder.member.LeaveMemberQuery;
import io.isometrik.gs.builder.member.RemoveMemberQuery;
import io.isometrik.gs.builder.message.DeleteMessageQuery;
import io.isometrik.gs.builder.message.FetchMessagesQuery;
import io.isometrik.gs.builder.message.SendMessageQuery;
import io.isometrik.gs.builder.stream.FetchStreamsQuery;
import io.isometrik.gs.builder.stream.StopStreamQuery;
import io.isometrik.gs.builder.stream.UpdateStreamPublishingStatusQuery;
import io.isometrik.gs.builder.viewer.AddViewerQuery;
import io.isometrik.gs.builder.viewer.FetchViewersCountQuery;
import io.isometrik.gs.builder.viewer.FetchViewersQuery;
import io.isometrik.gs.builder.viewer.LeaveViewerQuery;
import io.isometrik.gs.callbacks.ConnectionEventCallback;
import io.isometrik.gs.callbacks.CopublishEventCallback;
import io.isometrik.gs.callbacks.MemberEventCallback;
import io.isometrik.gs.callbacks.MessageEventCallback;
import io.isometrik.gs.callbacks.PresenceEventCallback;
import io.isometrik.gs.callbacks.StreamEventCallback;
import io.isometrik.gs.callbacks.ViewerEventCallback;
import io.isometrik.gs.enums.IMStreamType;
import io.isometrik.gs.events.connection.ConnectEvent;
import io.isometrik.gs.events.connection.ConnectionFailedEvent;
import io.isometrik.gs.events.connection.DisconnectEvent;
import io.isometrik.gs.events.copublish.CopublishRequestAcceptEvent;
import io.isometrik.gs.events.copublish.CopublishRequestAddEvent;
import io.isometrik.gs.events.copublish.CopublishRequestDenyEvent;
import io.isometrik.gs.events.copublish.CopublishRequestRemoveEvent;
import io.isometrik.gs.events.copublish.CopublishRequestSwitchProfileEvent;
import io.isometrik.gs.events.member.MemberAddEvent;
import io.isometrik.gs.events.member.MemberLeaveEvent;
import io.isometrik.gs.events.member.MemberRemoveEvent;
import io.isometrik.gs.events.member.MemberTimeoutEvent;
import io.isometrik.gs.events.member.NoPublisherLiveEvent;
import io.isometrik.gs.events.member.PublishStartEvent;
import io.isometrik.gs.events.member.PublishStopEvent;
import io.isometrik.gs.events.message.MessageAddEvent;
import io.isometrik.gs.events.message.MessageRemoveEvent;
import io.isometrik.gs.events.presence.PresenceStreamStartEvent;
import io.isometrik.gs.events.presence.PresenceStreamStopEvent;
import io.isometrik.gs.events.stream.StreamStartEvent;
import io.isometrik.gs.events.stream.StreamStopEvent;
import io.isometrik.gs.events.viewer.ViewerJoinEvent;
import io.isometrik.gs.events.viewer.ViewerLeaveEvent;
import io.isometrik.gs.events.viewer.ViewerRemoveEvent;
import io.isometrik.gs.events.viewer.ViewerTimeoutEvent;
import io.isometrik.gs.response.error.IsometrikError;
import io.isometrik.gs.response.member.FetchMembersResult;
import io.isometrik.gs.response.message.FetchMessagesResult;
import io.isometrik.gs.response.stream.FetchStreamsResult;
import io.isometrik.gs.response.viewer.FetchViewersResult;
import io.isometrik.gs.rtcengine.utils.UserIdGenerator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The scrollable streams presenter to listen for connections, viewers, members, streams, messages
 * event for
 * the stream group as well as to send/remove message in/from a stream group, fetch messages sent
 * or viewers count in a stream group, to start/stop publishing a stream group as member and to
 * fetch status of a copublish request, make a copublish request, accept/decline a copublish
 * request.
 * It implements ScrollableStreamsContract.Presenter{@link ScrollableStreamsContract.Presenter}
 *
 * @see ScrollableStreamsContract.Presenter
 */
public class ScrollableStreamsPresenter implements ScrollableStreamsContract.Presenter {

  /**
   * Instantiates a new scrollable streams presenter.
   */
  ScrollableStreamsPresenter(ScrollableStreamsContract.View scrollableStreamsView) {
    this.scrollableStreamsView = scrollableStreamsView;
    this.scrollableStreamsView.initializeHeartsViews();
  }

  private ScrollableStreamsContract.View scrollableStreamsView;

  private String pageToken;
  private boolean isLastStreamsPage;
  private boolean isLoadingStreams;
  private boolean isLastMessagesPage;
  private boolean isLoadingMessages;
  private boolean isAdmin;
  private boolean isBroadcaster;
  private String streamId;
  private ArrayList<String> memberIds = new ArrayList<>();
  private Map<String, String> members = new HashMap<>();

  private Map<Integer, Object> memberMutedState = new HashMap<>();

  private Isometrik isometrik = IsometrikUiSdk.getInstance().getIsometrik();

  /**
   * {@link ScrollableStreamsContract.Presenter#initialize(String, ArrayList, boolean,
   * boolean)}
   */
  @Override
  public void initialize(String streamId, ArrayList<String> memberIds, boolean isAdmin,
      boolean isBroadcaster) {
    this.streamId = streamId;
    this.memberIds = memberIds;
    this.isAdmin = isAdmin;
    this.isBroadcaster = isBroadcaster;
    members = new HashMap<>();
    for (int i = 0; i < memberIds.size(); i++) {
      members.put(memberIds.get(i), "");
    }
    memberMutedState = new HashMap<>();
    isLastMessagesPage = false;
    isLoadingMessages = false;
  }

  /**
   * @see CopublishEventCallback
   */
  private CopublishEventCallback copublishEventCallback = new CopublishEventCallback() {
    @Override
    public void copublishRequestAccepted(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestAcceptEvent copublishRequestAcceptEvent) {
      if (copublishRequestAcceptEvent.getStreamId().equals(streamId)) {

        if (!memberIds.contains(copublishRequestAcceptEvent.getUserId())) {
          memberIds.add(copublishRequestAcceptEvent.getUserId());
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("videoMuted", false);
        map.put("audioMuted", false);
        map.put("userName", copublishRequestAcceptEvent.getUserName());
        map.put("userId", copublishRequestAcceptEvent.getUserId());
        memberMutedState.put(UserIdGenerator.getUid(copublishRequestAcceptEvent.getUserId()), map);

        members.put(copublishRequestAcceptEvent.getUserId(),
            copublishRequestAcceptEvent.getUserName());
        boolean canJoin = IsometrikUiSdk.getInstance()
            .getUserSession()
            .getUserId()
            .equals(copublishRequestAcceptEvent.getUserId());

        scrollableStreamsView.onPresenceMessageEvent(new MessagesModel(IsometrikUiSdk.getInstance()
            .getContext()
            .getString(R.string.ism_copublish_accepted_message,
                copublishRequestAcceptEvent.getInitiatorName(),
                copublishRequestAcceptEvent.getUserName()),
            MessageTypeEnum.CopublishRequestAcceptedMessage.getValue(),
            copublishRequestAcceptEvent.getUserId(),
            copublishRequestAcceptEvent.getUserIdentifier(),
            copublishRequestAcceptEvent.getUserProfilePic(),
            copublishRequestAcceptEvent.getUserName(), copublishRequestAcceptEvent.getTimestamp(),
            isAdmin, canJoin));

        if (canJoin) {
          scrollableStreamsView.onCopublishRequestAcceptedEvent();
        }

        scrollableStreamsView.updateMembersAndViewersCount(
            copublishRequestAcceptEvent.getMembersCount(),
            copublishRequestAcceptEvent.getViewersCount());
      } else {

        if (!isBroadcaster) {
          scrollableStreamsView.updateMembersAndViewersCountInList(
              copublishRequestAcceptEvent.getStreamId(),
              copublishRequestAcceptEvent.getMembersCount(),
              copublishRequestAcceptEvent.getViewersCount());
        }
      }
    }

    @Override
    public void copublishRequestDenied(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestDenyEvent copublishRequestDenyEvent) {
      if (copublishRequestDenyEvent.getStreamId().equals(streamId)) {

        scrollableStreamsView.onPresenceMessageEvent(new MessagesModel(IsometrikUiSdk.getInstance()
            .getContext()
            .getString(R.string.ism_copublish_rejected_message,
                copublishRequestDenyEvent.getInitiatorName(),
                copublishRequestDenyEvent.getUserName()),
            MessageTypeEnum.CopublishRequestActionMessage.getValue(),
            copublishRequestDenyEvent.getUserId(), copublishRequestDenyEvent.getUserIdentifier(),
            copublishRequestDenyEvent.getUserProfilePic(), copublishRequestDenyEvent.getUserName(),
            copublishRequestDenyEvent.getTimestamp(), isAdmin, false));

        if (IsometrikUiSdk.getInstance()
            .getUserSession()
            .getUserId()
            .equals(copublishRequestDenyEvent.getUserId())) {

          scrollableStreamsView.onCopublishRequestDeclinedEvent();
        }

        scrollableStreamsView.updateMembersAndViewersCount(
            copublishRequestDenyEvent.getMembersCount(),
            copublishRequestDenyEvent.getViewersCount());
      } else {

        if (!isBroadcaster) {
          scrollableStreamsView.updateMembersAndViewersCountInList(
              copublishRequestDenyEvent.getStreamId(), copublishRequestDenyEvent.getMembersCount(),
              copublishRequestDenyEvent.getViewersCount());
        }
      }
    }

    @Override
    public void copublishRequestAdded(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestAddEvent copublishRequestAddEvent) {
      if (copublishRequestAddEvent.getStreamId().equals(streamId)) {

        scrollableStreamsView.onPresenceMessageEvent(new MessagesModel(IsometrikUiSdk.getInstance()
            .getContext()
            .getString(R.string.ism_request_message, copublishRequestAddEvent.getUserName()),
            MessageTypeEnum.CopublishRequestMessage.getValue(),
            copublishRequestAddEvent.getUserId(), copublishRequestAddEvent.getUserIdentifier(),
            copublishRequestAddEvent.getUserProfilePic(), copublishRequestAddEvent.getUserName(),
            copublishRequestAddEvent.getTimestamp(), isAdmin, false));

        scrollableStreamsView.updateMembersAndViewersCount(
            copublishRequestAddEvent.getMembersCount(), copublishRequestAddEvent.getViewersCount());
      } else {

        if (!isBroadcaster) {
          scrollableStreamsView.updateMembersAndViewersCountInList(
              copublishRequestAddEvent.getStreamId(), copublishRequestAddEvent.getMembersCount(),
              copublishRequestAddEvent.getViewersCount());
        }
      }
    }

    @Override
    public void copublishRequestRemoved(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestRemoveEvent copublishRequestRemoveEvent) {
      if (copublishRequestRemoveEvent.getStreamId().equals(streamId)) {

        scrollableStreamsView.onPresenceMessageEvent(new MessagesModel(IsometrikUiSdk.getInstance()
            .getContext()
            .getString(R.string.ism_copublish_deleted_message,
                copublishRequestRemoveEvent.getUserName()),
            MessageTypeEnum.CopublishRequestActionMessage.getValue(),
            copublishRequestRemoveEvent.getUserId(),
            copublishRequestRemoveEvent.getUserIdentifier(),
            copublishRequestRemoveEvent.getUserProfilePic(),
            copublishRequestRemoveEvent.getUserName(), copublishRequestRemoveEvent.getTimestamp(),
            isAdmin, false));

        scrollableStreamsView.updateMembersAndViewersCount(
            copublishRequestRemoveEvent.getMembersCount(),
            copublishRequestRemoveEvent.getViewersCount());
      } else {

        if (!isBroadcaster) {
          scrollableStreamsView.updateMembersAndViewersCountInList(
              copublishRequestRemoveEvent.getStreamId(),
              copublishRequestRemoveEvent.getMembersCount(),
              copublishRequestRemoveEvent.getViewersCount());
        }
      }
    }

    @Override
    public void switchProfile(@NotNull Isometrik isometrik,
        @NotNull CopublishRequestSwitchProfileEvent copublishRequestSwitchProfileEvent) {
      if (copublishRequestSwitchProfileEvent.getStreamId().equals(streamId)) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("videoMuted", false);
        map.put("audioMuted", false);
        map.put("userName", copublishRequestSwitchProfileEvent.getUserName());
        map.put("userId", copublishRequestSwitchProfileEvent.getUserId());
        memberMutedState.put(UserIdGenerator.getUid(copublishRequestSwitchProfileEvent.getUserId()),
            map);

        members.put(copublishRequestSwitchProfileEvent.getUserId(),
            copublishRequestSwitchProfileEvent.getUserName());

        scrollableStreamsView.onPresenceMessageEvent(new MessagesModel(IsometrikUiSdk.getInstance()
            .getContext()
            .getString(R.string.ism_switch_profile_message,
                copublishRequestSwitchProfileEvent.getUserName()),
            MessageTypeEnum.CopublishRequestActionMessage.getValue(),
            copublishRequestSwitchProfileEvent.getUserId(),
            copublishRequestSwitchProfileEvent.getUserIdentifier(),
            copublishRequestSwitchProfileEvent.getUserProfilePic(),
            copublishRequestSwitchProfileEvent.getUserName(),
            copublishRequestSwitchProfileEvent.getTimestamp(), isAdmin, false));

        scrollableStreamsView.updateMembersAndViewersCount(
            copublishRequestSwitchProfileEvent.getMembersCount(),
            copublishRequestSwitchProfileEvent.getViewersCount());

        scrollableStreamsView.removeViewerEvent(copublishRequestSwitchProfileEvent.getUserId(),
            copublishRequestSwitchProfileEvent.getViewersCount());
      } else {

        if (!isBroadcaster) {
          scrollableStreamsView.updateMembersAndViewersCountInList(
              copublishRequestSwitchProfileEvent.getStreamId(),
              copublishRequestSwitchProfileEvent.getMembersCount(),
              copublishRequestSwitchProfileEvent.getViewersCount());
        }
      }
    }
  };

  /**
   * @see ConnectionEventCallback
   */
  private ConnectionEventCallback connectionEventCallback = new ConnectionEventCallback() {
    @Override
    public void disconnected(@NotNull Isometrik isometrik,
        @NotNull DisconnectEvent disconnectEvent) {

      scrollableStreamsView.connectionStateChanged(false);
    }

    @Override
    public void connected(@NotNull Isometrik isometrik, @NotNull ConnectEvent connectEvent) {

      scrollableStreamsView.connectionStateChanged(true);

      if (isBroadcaster) {
        updateBroadcastingStatus(true, streamId, true);
      } else {
        joinAsViewer(streamId, true);
      }
    }

    @Override
    public void connectionFailed(@NotNull Isometrik isometrik,
        @NotNull IsometrikError isometrikError) {

      scrollableStreamsView.connectionStateChanged(false);
    }

    @Override
    public void failedToConnect(@NotNull Isometrik isometrik,
        @NotNull ConnectionFailedEvent connectionFailedEvent) {
      scrollableStreamsView.connectionStateChanged(false);
    }
  };

  /**
   * @see StreamEventCallback
   */
  private StreamEventCallback streamEventCallback = new StreamEventCallback() {
    @Override
    public void streamStarted(@NotNull Isometrik isometrik,
        @NotNull StreamStartEvent streamStartEvent) {
      String userId = IsometrikUiSdk.getInstance().getUserSession().getUserId();

      if (!userId.equals(streamStartEvent.getInitiatorId())) {

        boolean givenUserIsMember = false;
        List<StreamStartEvent.StreamMember> members = streamStartEvent.getMembers();

        ArrayList<String> memberIds = new ArrayList<>();
        String memberId;
        int size = members.size();
        for (int i = 0; i < size; i++) {
          memberId = members.get(i).getMemberId();
          memberIds.add(memberId);
          if (memberId.equals(userId)) {
            givenUserIsMember = true;
          }
        }

        scrollableStreamsView.onStreamStarted(
            new StreamsModel(streamStartEvent.getStreamId(), streamStartEvent.getStreamImage(),
                streamStartEvent.getStreamDescription(), streamStartEvent.getMembersCount(), 0, 1,
                streamStartEvent.getInitiatorId(), streamStartEvent.getInitiatorName(),
                streamStartEvent.getInitiatorIdentifier(), streamStartEvent.getInitiatorImage(),
                streamStartEvent.getTimestamp(), memberIds, givenUserIsMember,
                streamStartEvent.isPublic()));
      }
    }

    @Override
    public void streamStopped(@NotNull Isometrik isometrik,
        @NotNull StreamStopEvent streamStopEvent) {
      if (streamStopEvent.getStreamId().equals(streamId)) {

        scrollableStreamsView.onStreamOffline(IsometrikUiSdk.getInstance()
                .getContext()
                .getString(R.string.ism_stream_offline_initiator_stop),
            StreamDialogEnum.StreamOffline.getValue(), streamId, false);
      }

      if (!streamStopEvent.getInitiatorId()
          .equals(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {

        scrollableStreamsView.onStreamEnded(streamStopEvent.getStreamId(),
            streamStopEvent.getStreamId().equals(streamId));
      }
    }
  };

  /**
   * @see PresenceEventCallback
   */
  private PresenceEventCallback presenceEventCallback = new PresenceEventCallback() {
    @Override
    public void streamStarted(@NotNull Isometrik isometrik,
        @NotNull PresenceStreamStartEvent presenceStreamStartEvent) {

      String userId = IsometrikUiSdk.getInstance().getUserSession().getUserId();

      if (!userId.equals(presenceStreamStartEvent.getInitiatorId())) {

        boolean givenUserIsMember = false;
        List<PresenceStreamStartEvent.StreamMember> members = presenceStreamStartEvent.getMembers();

        ArrayList<String> memberIds = new ArrayList<>();
        String memberId;
        int size = members.size();
        for (int i = 0; i < size; i++) {
          memberId = members.get(i).getMemberId();
          memberIds.add(memberId);
          if (memberId.equals(userId)) {
            givenUserIsMember = true;
          }
        }

        scrollableStreamsView.onStreamStarted(
            new StreamsModel(presenceStreamStartEvent.getStreamId(),
                presenceStreamStartEvent.getStreamImage(),
                presenceStreamStartEvent.getStreamDescription(),
                presenceStreamStartEvent.getMembersCount(), 0, 1,
                presenceStreamStartEvent.getInitiatorId(),
                presenceStreamStartEvent.getInitiatorName(),
                presenceStreamStartEvent.getInitiatorIdentifier(),
                presenceStreamStartEvent.getInitiatorImage(),
                presenceStreamStartEvent.getTimestamp(), memberIds, givenUserIsMember,
                presenceStreamStartEvent.isPublic()));
      }
    }

    @Override
    public void streamStopped(@NotNull Isometrik isometrik,
        @NotNull PresenceStreamStopEvent presenceStreamStopEvent) {
      if (presenceStreamStopEvent.getStreamId().equals(streamId)) {

        scrollableStreamsView.onStreamOffline(IsometrikUiSdk.getInstance()
                .getContext()
                .getString(R.string.ism_stream_offline_initiator_stop),
            StreamDialogEnum.StreamOffline.getValue(), streamId, false);
      }
      if (!presenceStreamStopEvent.getInitiatorId()
          .equals(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {

        scrollableStreamsView.onStreamEnded(presenceStreamStopEvent.getStreamId(),
            presenceStreamStopEvent.getStreamId().equals(streamId));
      }
    }
  };

  /**
   * @see MessageEventCallback
   */
  private MessageEventCallback messageEventCallback = new MessageEventCallback() {
    @Override
    public void messageAdded(@NotNull Isometrik isometrik,
        @NotNull MessageAddEvent messageAddEvent) {

      if (messageAddEvent.getStreamId().equals(streamId)) {

        if (messageAddEvent.getMessageType() == MessageTypeEnum.NormalMessage.getValue()) {

          if (memberIds.contains(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {

            scrollableStreamsView.onTextMessageReceived(
                new MessagesModel(messageAddEvent, isBroadcaster));
          } else {
            scrollableStreamsView.onTextMessageReceived(new MessagesModel(messageAddEvent, false));
          }
        } else if (messageAddEvent.getMessageType() == MessageTypeEnum.HeartMessage.getValue()) {
          scrollableStreamsView.onLikeEvent();
        } else if (messageAddEvent.getMessageType() == MessageTypeEnum.GiftMessage.getValue()) {
          try {
            JSONObject jsonObject = new JSONObject(messageAddEvent.getMessage());
            scrollableStreamsView.onGiftEvent(
                new GiftsModel(messageAddEvent.getSenderId(), messageAddEvent.getSenderName(),
                    messageAddEvent.getSenderImage(), messageAddEvent.getSenderIdentifier(),
                    jsonObject.getString("message"), jsonObject.getInt("coinsValue"),
                    jsonObject.getString("giftName")));
          } catch (JSONException e) {
            e.printStackTrace();
          }
        }
      }
    }

    @Override
    public void messageRemoved(@NotNull Isometrik isometrik,
        @NotNull MessageRemoveEvent messageRemoveEvent) {
      if (messageRemoveEvent.getStreamId().equals(streamId)) {
        scrollableStreamsView.onMessageRemovedEvent(messageRemoveEvent.getMessageId(),
            IsometrikUiSdk.getInstance()
                .getContext()
                .getString(R.string.ism_message_removed, messageRemoveEvent.getInitiatorName(),
                    DateUtil.getDate(messageRemoveEvent.getTimestamp())));
      }
    }
  };

  /**
   * @see MemberEventCallback
   */
  private MemberEventCallback memberEventCallback = new MemberEventCallback() {
    @Override
    public void memberAdded(@NotNull Isometrik isometrik, @NotNull MemberAddEvent memberAddEvent) {
      if (memberAddEvent.getStreamId().equals(streamId)) {

        if (!memberIds.contains(memberAddEvent.getMemberId())) {
          memberIds.add(memberAddEvent.getMemberId());
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("videoMuted", false);
        map.put("audioMuted", false);
        map.put("userName", memberAddEvent.getMemberName());
        map.put("userId", memberAddEvent.getMemberId());
        memberMutedState.put(UserIdGenerator.getUid(memberAddEvent.getMemberId()), map);

        members.put(memberAddEvent.getMemberId(), memberAddEvent.getMemberName());

        if (memberAddEvent.getMemberId()
            .equals(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {
          scrollableStreamsView.onMemberAdded(true, memberAddEvent.getInitiatorName());
        }

        scrollableStreamsView.onPresenceMessageEvent(new MessagesModel(IsometrikUiSdk.getInstance()
            .getContext()
            .getString(R.string.ism_member_added, memberAddEvent.getMemberName(),
                memberAddEvent.getInitiatorName()), MessageTypeEnum.PresenceMessage.getValue(),
            memberAddEvent.getTimestamp()));

        scrollableStreamsView.updateMembersAndViewersCount(memberAddEvent.getMembersCount(),
            memberAddEvent.getViewersCount());
      } else {

        if (!isBroadcaster) {
          scrollableStreamsView.updateMembersAndViewersCountInList(memberAddEvent.getStreamId(),
              memberAddEvent.getMembersCount(), memberAddEvent.getViewersCount());
        }
      }
    }

    @Override
    public void memberLeft(@NotNull Isometrik isometrik,
        @NotNull MemberLeaveEvent memberLeaveEvent) {
      if (memberLeaveEvent.getStreamId().equals(streamId)) {

        memberIds.remove(memberLeaveEvent.getMemberId());
        members.remove(memberLeaveEvent.getMemberId());
        memberMutedState.remove(UserIdGenerator.getUid(memberLeaveEvent.getMemberId()));

        scrollableStreamsView.onPresenceMessageEvent(new MessagesModel(IsometrikUiSdk.getInstance()
            .getContext()
            .getString(R.string.ism_member_left, memberLeaveEvent.getMemberName()),
            MessageTypeEnum.PresenceMessage.getValue(), memberLeaveEvent.getTimestamp()));

        scrollableStreamsView.updateMembersAndViewersCount(memberLeaveEvent.getMembersCount(),
            memberLeaveEvent.getViewersCount());
      } else {

        if (!isBroadcaster) {
          scrollableStreamsView.updateMembersAndViewersCountInList(memberLeaveEvent.getStreamId(),
              memberLeaveEvent.getMembersCount(), memberLeaveEvent.getViewersCount());
        }
      }
    }

    @Override
    public void memberRemoved(@NotNull Isometrik isometrik,
        @NotNull MemberRemoveEvent memberRemoveEvent) {
      if (memberRemoveEvent.getStreamId().equals(streamId)) {

        memberIds.remove(memberRemoveEvent.getMemberId());
        members.remove(memberRemoveEvent.getMemberId());
        memberMutedState.remove(UserIdGenerator.getUid(memberRemoveEvent.getMemberId()));

        if (memberRemoveEvent.getMemberId()
            .equals(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {

          scrollableStreamsView.onMemberAdded(false, memberRemoveEvent.getInitiatorName());

          String senderName = IsometrikUiSdk.getInstance()
              .getContext()
              .getString(R.string.ism_you, memberRemoveEvent.getMemberName());

          scrollableStreamsView.onPresenceMessageEvent(new MessagesModel(
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_member_removed, senderName,
                      memberRemoveEvent.getInitiatorName()),
              MessageTypeEnum.PresenceMessage.getValue(), memberRemoveEvent.getTimestamp()));
        } else {

          scrollableStreamsView.onPresenceMessageEvent(new MessagesModel(
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_member_removed, memberRemoveEvent.getMemberName(),
                      memberRemoveEvent.getInitiatorName()),
              MessageTypeEnum.PresenceMessage.getValue(), memberRemoveEvent.getTimestamp()));
        }
        scrollableStreamsView.updateMembersAndViewersCount(memberRemoveEvent.getMembersCount(),
            memberRemoveEvent.getViewersCount());
      } else {

        if (!isBroadcaster) {
          scrollableStreamsView.updateMembersAndViewersCountInList(memberRemoveEvent.getStreamId(),
              memberRemoveEvent.getMembersCount(), memberRemoveEvent.getViewersCount());
        }
      }
    }

    @Override
    public void memberTimedOut(@NotNull Isometrik isometrik,
        @NotNull MemberTimeoutEvent memberTimeoutEvent) {
      if (memberTimeoutEvent.getStreamId().equals(streamId)) {

        members.put(memberTimeoutEvent.getMemberId(), memberTimeoutEvent.getMemberName());

        scrollableStreamsView.onPresenceMessageEvent(new MessagesModel(IsometrikUiSdk.getInstance()
            .getContext()
            .getString(R.string.ism_member_timeout, memberTimeoutEvent.getMemberName()),
            MessageTypeEnum.PresenceMessage.getValue(), memberTimeoutEvent.getTimestamp()));

        scrollableStreamsView.updateMembersAndViewersCount(memberTimeoutEvent.getMembersCount(),
            memberTimeoutEvent.getViewersCount());
      } else {

        if (!isBroadcaster) {
          scrollableStreamsView.updateMembersAndViewersCountInList(memberTimeoutEvent.getStreamId(),
              memberTimeoutEvent.getMembersCount(), memberTimeoutEvent.getViewersCount());
        }
      }
    }

    @Override
    public void memberPublishStarted(@NotNull Isometrik isometrik,
        @NotNull PublishStartEvent publishStartEvent) {
      if (publishStartEvent.getStreamId().equals(streamId)) {

        members.put(publishStartEvent.getMemberId(), publishStartEvent.getMemberName());

        scrollableStreamsView.onPresenceMessageEvent(new MessagesModel(IsometrikUiSdk.getInstance()
            .getContext()
            .getString(R.string.ism_member_publish_start, publishStartEvent.getMemberName()),
            MessageTypeEnum.PresenceMessage.getValue(), publishStartEvent.getTimestamp()));

        scrollableStreamsView.updateMembersAndViewersCount(publishStartEvent.getMembersCount(),
            publishStartEvent.getViewersCount());
      } else {

        if (!isBroadcaster) {
          scrollableStreamsView.updateMembersAndViewersCountInList(publishStartEvent.getStreamId(),
              publishStartEvent.getMembersCount(), publishStartEvent.getViewersCount());
        }
      }
    }

    @Override
    public void memberPublishStopped(@NotNull Isometrik isometrik,
        @NotNull PublishStopEvent publishStopEvent) {
      if (publishStopEvent.getStreamId().equals(streamId)) {

        members.put(publishStopEvent.getMemberId(), publishStopEvent.getMemberName());

        scrollableStreamsView.onPresenceMessageEvent(new MessagesModel(IsometrikUiSdk.getInstance()
            .getContext()
            .getString(R.string.ism_member_publish_stop, publishStopEvent.getMemberName()),
            MessageTypeEnum.PresenceMessage.getValue(), publishStopEvent.getTimestamp()));

        scrollableStreamsView.updateMembersAndViewersCount(publishStopEvent.getMembersCount(),
            publishStopEvent.getViewersCount());
      } else {

        if (!isBroadcaster) {
          scrollableStreamsView.updateMembersAndViewersCountInList(publishStopEvent.getStreamId(),
              publishStopEvent.getMembersCount(), publishStopEvent.getViewersCount());
        }
      }
    }

    @Override
    public void noMemberPublishing(@NotNull Isometrik isometrik,
        @NotNull NoPublisherLiveEvent noPublisherLiveEvent) {
      if (noPublisherLiveEvent.getStreamId().equals(streamId)) {
        scrollableStreamsView.onStreamOffline(IsometrikUiSdk.getInstance()
                .getContext()
                .getString(R.string.ism_stream_offline_no_publisher),
            StreamDialogEnum.StreamOffline.getValue(), streamId, false);
      }
    }
  };

  /**
   * @see ViewerEventCallback
   */
  private ViewerEventCallback viewerEventCallback = new ViewerEventCallback() {
    @Override
    public void viewerJoined(@NotNull Isometrik isometrik,
        @NotNull ViewerJoinEvent viewerJoinEvent) {

      if (viewerJoinEvent.getStreamId().equals(streamId)) {

        scrollableStreamsView.onPresenceMessageEvent(new MessagesModel(IsometrikUiSdk.getInstance()
            .getContext()
            .getString(R.string.ism_viewer_joined, viewerJoinEvent.getViewerName()),
            MessageTypeEnum.PresenceMessage.getValue(), viewerJoinEvent.getTimestamp()));

        scrollableStreamsView.updateMembersAndViewersCount(viewerJoinEvent.getMembersCount(),
            viewerJoinEvent.getViewersCount());

        scrollableStreamsView.addViewerEvent(new ViewersListModel(viewerJoinEvent.getViewerId(),
            viewerJoinEvent.getViewerProfilePic()), viewerJoinEvent.getViewersCount());
      } else {

        if (!isBroadcaster) {
          scrollableStreamsView.updateMembersAndViewersCountInList(viewerJoinEvent.getStreamId(),
              viewerJoinEvent.getMembersCount(), viewerJoinEvent.getViewersCount());
        }
      }
    }

    @Override
    public void viewerLeft(@NotNull Isometrik isometrik,
        @NotNull ViewerLeaveEvent viewerLeaveEvent) {
      if (viewerLeaveEvent.getStreamId().equals(streamId)) {

        scrollableStreamsView.onPresenceMessageEvent(new MessagesModel(IsometrikUiSdk.getInstance()
            .getContext()
            .getString(R.string.ism_viewer_left, viewerLeaveEvent.getViewerName()),
            MessageTypeEnum.PresenceMessage.getValue(), viewerLeaveEvent.getTimestamp()));

        scrollableStreamsView.updateMembersAndViewersCount(viewerLeaveEvent.getMembersCount(),
            viewerLeaveEvent.getViewersCount());

        scrollableStreamsView.removeViewerEvent(viewerLeaveEvent.getViewerId(),
            viewerLeaveEvent.getViewersCount());
      } else {

        if (!isBroadcaster) {
          scrollableStreamsView.updateMembersAndViewersCountInList(viewerLeaveEvent.getStreamId(),
              viewerLeaveEvent.getMembersCount(), viewerLeaveEvent.getViewersCount());
        }
      }
    }

    @Override
    public void viewerRemoved(@NotNull Isometrik isometrik,
        @NotNull ViewerRemoveEvent viewerRemoveEvent) {

      if (viewerRemoveEvent.getStreamId().equals(streamId)) {

        members.put(viewerRemoveEvent.getViewerId(), viewerRemoveEvent.getInitiatorName());
        if (viewerRemoveEvent.getViewerId()
            .equals(IsometrikUiSdk.getInstance().getUserSession().getUserId()) && !isBroadcaster) {

          scrollableStreamsView.onStreamOffline(IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_viewer_kicked_out, viewerRemoveEvent.getInitiatorName()),
              StreamDialogEnum.KickedOut.getValue(), streamId, true);
        } else {

          scrollableStreamsView.onPresenceMessageEvent(new MessagesModel(
              IsometrikUiSdk.getInstance()
                  .getContext()
                  .getString(R.string.ism_viewer_removed, viewerRemoveEvent.getViewerName(),
                      viewerRemoveEvent.getInitiatorName()),
              MessageTypeEnum.PresenceMessage.getValue(), viewerRemoveEvent.getTimestamp()));

          scrollableStreamsView.updateMembersAndViewersCount(viewerRemoveEvent.getMembersCount(),
              viewerRemoveEvent.getViewersCount());

          scrollableStreamsView.removeViewerEvent(viewerRemoveEvent.getViewerId(),
              viewerRemoveEvent.getViewersCount());
        }
      } else {

        if (!isBroadcaster) {
          scrollableStreamsView.updateMembersAndViewersCountInList(viewerRemoveEvent.getStreamId(),
              viewerRemoveEvent.getMembersCount(), viewerRemoveEvent.getViewersCount());
        }
      }
    }

    @Override
    public void viewerTimedOut(@NotNull Isometrik isometrik,
        @NotNull ViewerTimeoutEvent viewerTimeoutEvent) {
      if (viewerTimeoutEvent.getStreamId().equals(streamId)) {

        scrollableStreamsView.onPresenceMessageEvent(new MessagesModel(IsometrikUiSdk.getInstance()
            .getContext()
            .getString(R.string.ism_viewer_timeout, viewerTimeoutEvent.getViewerName()),
            MessageTypeEnum.PresenceMessage.getValue(), viewerTimeoutEvent.getTimestamp()));

        scrollableStreamsView.updateMembersAndViewersCount(viewerTimeoutEvent.getMembersCount(),
            viewerTimeoutEvent.getViewersCount());

        scrollableStreamsView.removeViewerEvent(viewerTimeoutEvent.getViewerId(),
            viewerTimeoutEvent.getViewersCount());
      } else {

        if (!isBroadcaster) {
          scrollableStreamsView.updateMembersAndViewersCountInList(viewerTimeoutEvent.getStreamId(),
              viewerTimeoutEvent.getMembersCount(), viewerTimeoutEvent.getViewersCount());
        }
      }
    }
  };

  /**
   * {@link ScrollableStreamsContract.Presenter#registerConnectionEventListener()}
   */
  @Override
  public void registerConnectionEventListener() {
    isometrik.addConnectionEventListener(connectionEventCallback);
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#unregisterConnectionEventListener()}
   */
  @Override
  public void unregisterConnectionEventListener() {
    isometrik.removeConnectionEventListener(connectionEventCallback);
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#registerStreamsEventListener()}
   */
  @Override
  public void registerStreamsEventListener() {
    isometrik.addStreamEventListener(streamEventCallback);
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#unregisterStreamsEventListener()}
   */
  @Override
  public void unregisterStreamsEventListener() {
    isometrik.removeStreamEventListener(streamEventCallback);
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#registerPresenceEventListener()}
   */
  @Override
  public void registerPresenceEventListener() {
    isometrik.addPresenceEventListener(presenceEventCallback);
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#unregisterPresenceEventListener()}
   */
  @Override
  public void unregisterPresenceEventListener() {
    isometrik.removePresenceEventListener(presenceEventCallback);
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#registerStreamMessagesEventListener()}
   */
  @Override
  public void registerStreamMessagesEventListener() {
    isometrik.addMessageEventListener(messageEventCallback);
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#unregisterStreamMessagesEventListener()}
   */
  @Override
  public void unregisterStreamMessagesEventListener() {
    isometrik.removeMessageEventListener(messageEventCallback);
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#registerStreamMembersEventListener()}
   */
  @Override
  public void registerStreamMembersEventListener() {
    isometrik.addMemberEventListener(memberEventCallback);
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#unregisterStreamMembersEventListener()}
   */
  @Override
  public void unregisterStreamMembersEventListener() {
    isometrik.removeMemberEventListener(memberEventCallback);
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#registerStreamViewersEventListener()}
   */
  @Override
  public void registerStreamViewersEventListener() {
    isometrik.addViewerEventListener(viewerEventCallback);
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#unregisterStreamViewersEventListener()}
   */
  @Override
  public void unregisterStreamViewersEventListener() {
    isometrik.removeViewerEventListener(viewerEventCallback);
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#registerCopublishRequestsEventListener()}
   */
  @Override
  public void registerCopublishRequestsEventListener() {
    isometrik.addCopublishEventListener(copublishEventCallback);
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#unregisterCopublishRequestsEventListener()}
   */
  @Override
  public void unregisterCopublishRequestsEventListener() {
    isometrik.removeCopublishEventListener(copublishEventCallback);
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#requestStreamMessagesData(String, int, boolean)}
   */
  @Override
  public void requestStreamMessagesData(String streamId, int pageSize, boolean refreshRequest) {
    isLoadingMessages = true;

    if (refreshRequest) {
      pageToken = null;
      isLastMessagesPage = false;
    }

    String userId = IsometrikUiSdk.getInstance().getUserSession().getUserId();

    isometrik.fetchMessages(new FetchMessagesQuery.Builder().setStreamId(streamId)
        .setUserId(userId)
        .setCount(pageSize)
        .setPageToken(pageToken)
        .build(), (var1, var2) -> {

      if (var1 != null) {
        if (this.streamId.equals(streamId)) {
          ArrayList<MessagesModel> messagesModels = new ArrayList<>();

          pageToken = var1.getPageToken();
          if (pageToken == null) {

            isLastMessagesPage = true;
          }

          ArrayList<FetchMessagesResult.StreamMessage> messages = var1.getStreamMessages();
          int size = messages.size();

          boolean givenUserIsMember = memberIds.contains(userId);

          for (int i = 0; i < size; i++) {

            if (givenUserIsMember) {
              messagesModels.add(0, new MessagesModel(messages.get(i), isBroadcaster, userId));
            } else {
              messagesModels.add(0, new MessagesModel(messages.get(i), false, userId));
            }
          }

          scrollableStreamsView.onStreamMessagesDataReceived(messagesModels, refreshRequest);
        }
      } else {
        if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 0) {
          //Stream not live anymore
          scrollableStreamsView.onStreamOffline(
              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_stream_offline),
              StreamDialogEnum.StreamOffline.getValue(), streamId, false);
        } else if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {
          if (this.streamId.equals(streamId)) {
            if (refreshRequest) {
              //No messages found
              scrollableStreamsView.onStreamMessagesDataReceived(new ArrayList<>(), true);
            } else {
              isLastMessagesPage = true;
            }
          }
        } else {
          if (this.streamId.equals(streamId)) {
            scrollableStreamsView.onError(var2.getErrorMessage());
          }
        }
      }
      isLoadingMessages = false;
    });
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#requestStreamMessagesDataOnScroll(String)}
   */
  @Override
  public void requestStreamMessagesDataOnScroll(String streamId) {
    if (!isLoadingMessages && !isLastMessagesPage) {

      requestStreamMessagesData(streamId, Constants.MESSAGES_PAGE_SIZE, false);
    }
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#sendMessage(String, String,
   * int, int, String)}
   */
  @Override
  public void sendMessage(String streamId, String message, int messageType, int coinsValue,
      String giftName) {

    long timestamp = System.currentTimeMillis();

    UserSession userSession = IsometrikUiSdk.getInstance().getUserSession();

    SendMessageQuery.Builder sendMessageQueryBuilder =
        new SendMessageQuery.Builder().setMessage(message)
            .setStreamId(streamId)
            .setSenderId(userSession.getUserId())
            .setSenderIdentifier(userSession.getUserIdentifier())
            .setSenderName(userSession.getUserName())
            .setSenderImage(userSession.getUserProfilePic());

    switch (messageType) {

      case 0:
        if (this.streamId.equals(streamId)) {
          scrollableStreamsView.onTextMessageSent(
              new MessagesModel(message, String.valueOf(timestamp),
                  MessageTypeEnum.NormalMessage.getValue(), timestamp,
                  memberIds.contains(userSession.getUserId()), userSession));
        }

        sendMessageQueryBuilder.setMessageType(MessageTypeEnum.NormalMessage.getValue());
        break;

      case 1:

        sendMessageQueryBuilder.setMessageType(MessageTypeEnum.HeartMessage.getValue());
        break;

      case 2:

        JSONObject jsonObject = new JSONObject();
        try {
          jsonObject.put("coinsValue", coinsValue);
          jsonObject.put("message", message);
          jsonObject.put("giftName", giftName);
        } catch (JSONException e) {
          e.printStackTrace();
        }
        sendMessageQueryBuilder.setMessage(jsonObject.toString());
        sendMessageQueryBuilder.setMessageType(MessageTypeEnum.GiftMessage.getValue());
        break;
    }

    isometrik.sendMessage(sendMessageQueryBuilder.build(), (var1, var2) -> {

      if (var1 != null) {
        if (this.streamId.equals(streamId)) {
          switch (messageType) {
            case 0:
              scrollableStreamsView.onMessageDelivered(var1.getMessageId(), var1.getSentAt(),
                  String.valueOf(timestamp));
              break;

            case 1:
              scrollableStreamsView.onLikeEvent();
              break;

            case 2:
              scrollableStreamsView.onGiftEvent(
                  new GiftsModel(userSession.getUserId(), userSession.getUserName(),
                      userSession.getUserProfilePic(), userSession.getUserIdentifier(), message,
                      coinsValue, giftName));

              break;
          }
        }
      } else {
        if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 1) {
          //Stream not live anymore
          scrollableStreamsView.onStreamOffline(
              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_stream_offline),
              StreamDialogEnum.StreamOffline.getValue(), streamId, false);
        } else {
          if (this.streamId.equals(streamId)) {
            scrollableStreamsView.onError(var2.getErrorMessage());
          }
        }
      }
    });
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#removeMessage(String, String, long)}
   */
  @Override
  public void removeMessage(String streamId, String messageId, long timestamp) {

    UserSession userSession = IsometrikUiSdk.getInstance().getUserSession();

    DeleteMessageQuery deleteMessageQuery = new DeleteMessageQuery.Builder().setMessageId(messageId)
        .setStreamId(streamId)
        .setSentAt(timestamp)
        .setMemberId(userSession.getUserId())
        .setMemberName(userSession.getUserName())
        .build();

    isometrik.deleteMessage(deleteMessageQuery, (var1, var2) -> {
      if (var1 != null) {
        if (this.streamId.equals(streamId)) {
          scrollableStreamsView.onMessageRemovedEvent(messageId, IsometrikUiSdk.getInstance()
              .getContext()
              .getString(R.string.ism_message_removed, userSession.getUserName(),
                  DateUtil.getDate(var1.getDeletedAt())));
        }
      } else {
        if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 1) {
          //Stream not live anymore
          scrollableStreamsView.onStreamOffline(
              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_stream_offline),
              StreamDialogEnum.StreamOffline.getValue(), streamId, false);
        } else {
          if (this.streamId.equals(streamId)) {
            scrollableStreamsView.onError(var2.getErrorMessage());
          }
        }
      }
    });
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#updateBroadcastingStatus(boolean, String, boolean)}
   */
  @Override
  public void updateBroadcastingStatus(boolean startBroadcast, String streamId,
      boolean rejoinRequest) {

    isometrik.updateStreamPublishingStatus(
        new UpdateStreamPublishingStatusQuery.Builder().setStreamId(streamId)
            .setMemberId(IsometrikUiSdk.getInstance().getUserSession().getUserId())
            .setStartPublish(startBroadcast)
            .build(), (var1, var2) -> {

          if (startBroadcast) {
            if (var1 != null) {
              if (this.streamId.equals(streamId)) {
                scrollableStreamsView.onBroadcastStatusUpdated(true, rejoinRequest,
                    var1.getRtcToken());

                fetchLatestMembers(streamId);
                //fetchViewersCount();
                requestStreamViewersData(streamId);
              }
            } else {

              if (var2.getHttpResponseCode() == 409 && var2.getRemoteErrorCode() == 0) {
                if (this.streamId.equals(streamId)) {
                  //Member already publishing
                  scrollableStreamsView.onBroadcastStatusUpdated(true, rejoinRequest, null);

                  fetchLatestMembers(streamId);
                  //fetchViewersCount();
                  requestStreamViewersData(streamId);
                }
              } else if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 0) {
                //Stream not live anymore
                scrollableStreamsView.onStreamOffline(IsometrikUiSdk.getInstance()
                        .getContext()
                        .getString(R.string.ism_stream_offline),
                    StreamDialogEnum.StreamOffline.getValue(), streamId, false);
              } else {
                if (this.streamId.equals(streamId)) {
                  scrollableStreamsView.onBroadcastingStatusUpdateFailed(true,
                      var2.getErrorMessage());
                }
              }
            }
          }
        });
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#leaveStreamGroup(String)}
   */
  @Override
  public void leaveStreamGroup(String streamId) {

    isometrik.leaveMember(new LeaveMemberQuery.Builder().setStreamId(streamId)
        .setMemberId(IsometrikUiSdk.getInstance().getUserSession().getUserId())
        .build(), (var1, var2) -> {

      if (var1 != null) {
        if (this.streamId.equals(streamId)) {
          scrollableStreamsView.onStreamGroupLeft();
        }
      } else {
        if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 0) {
          //Stream not live anymore
          scrollableStreamsView.onStreamOffline(
              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_stream_offline),
              StreamDialogEnum.StreamOffline.getValue(), streamId, false);
        } else {
          if (this.streamId.equals(streamId)) {
            scrollableStreamsView.onError(var2.getErrorMessage());
          }
        }
      }
    });
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#stopBroadcast(String)}
   */
  @Override
  public void stopBroadcast(String streamId) {

    isometrik.stopStream(new StopStreamQuery.Builder().setStreamId(streamId)
        .setCreatedBy(IsometrikUiSdk.getInstance().getUserSession().getUserId())
        .build(), (var1, var2) -> {

      if (var1 != null) {
        if (this.streamId.equals(streamId)) {
          scrollableStreamsView.onBroadcastEnded();
        }
      } else {
        if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 1) {
          //Stream not live anymore
          scrollableStreamsView.onStreamOffline(
              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_stream_offline),
              StreamDialogEnum.StreamOffline.getValue(), streamId, false);
        } else {
          if (this.streamId.equals(streamId)) {
            scrollableStreamsView.onError(var2.getErrorMessage());
          }
        }
      }
    });
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#getMemberIds()}
   */
  @Override
  public ArrayList<String> getMemberIds() {
    return memberIds;
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#getMembers()}
   */
  @Override
  public Map<String, String> getMembers() {
    return members;
  }

  /**
   * Update list of members on reconnect request or when first time stream group is joined as
   * member.
   */
  private void fetchLatestMembers(String streamId) {

    isometrik.fetchMembers(new FetchMembersQuery.Builder().setStreamId(streamId).build(),
        (var1, var2) -> {

          if (var1 != null) {
            if (this.streamId.equals(streamId)) {
              memberIds = new ArrayList<>();
              members = new HashMap<>();
              memberMutedState = new HashMap<>();

              ArrayList<FetchMembersResult.StreamMember> members = var1.getStreamMembers();
              int size = members.size();
              FetchMembersResult.StreamMember member;
              Map<String, Object> memberInfo;
              for (int i = 0; i < size; i++) {
                member = members.get(i);
                memberIds.add(member.getMemberId());
                memberInfo = new HashMap<>();
                memberInfo.put("audioMuted", false);
                memberInfo.put("videoMuted", false);
                memberInfo.put("userId", member.getMemberId());
                memberInfo.put("userName", member.getMemberName());
                //IF a remote user has been muted before response of this api,than it might lead to incorrect mute state,thats why disabled updating settings before response
                memberMutedState.put(UserIdGenerator.getUid(member.getMemberId()), memberInfo);
                this.members.put(member.getMemberId(), member.getMemberName());
              }

              scrollableStreamsView.updateMembersAndViewersCount(size, -1);

              if (memberIds.contains(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {
                scrollableStreamsView.updateVisibilityForJoinButton();
              }
              scrollableStreamsView.updateRemoteUserNames(this.members);
            } else {

              if (!isBroadcaster) {
                scrollableStreamsView.updateMembersAndViewersCountInList(streamId,
                    var1.getStreamMembers().size(), -1);
              }
            }
          } else {
            if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 0) {
              //Stream not live anymore
              scrollableStreamsView.onStreamOffline(
                  IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_stream_offline),
                  StreamDialogEnum.StreamOffline.getValue(), streamId, false);
            } else if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 4) {
              if (this.streamId.equals(streamId)) {
                //No stream members
                memberIds = new ArrayList<>();
                members = new HashMap<>();
                memberMutedState = new HashMap<>();
              }
            } else {
              if (this.streamId.equals(streamId)) {
                scrollableStreamsView.onError(var2.getErrorMessage());
              }
            }
          }
        });
  }

  /**
   * Fetch viewers count in a stream group.
   */
  private void fetchViewersCount(String streamId) {
    isometrik.fetchViewersCount(new FetchViewersCountQuery.Builder().setStreamId(streamId).build(),
        (var1, var2) -> {

          if (var1 != null) {
            if (this.streamId.equals(streamId)) {
              scrollableStreamsView.updateMembersAndViewersCount(-1, var1.getNumberOfViewers());
            } else {
              if (!isBroadcaster) {
                scrollableStreamsView.updateMembersAndViewersCountInList(streamId, -1,
                    var1.getNumberOfViewers());
              }
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {

              if (this.streamId.equals(streamId)) {
                //No viewers found
                scrollableStreamsView.updateMembersAndViewersCount(-1, 0);
              } else {
                if (!isBroadcaster) {
                  scrollableStreamsView.updateMembersAndViewersCountInList(streamId, -1, 0);
                }
              }
            } else if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 0) {
              //Stream not live anymore
              scrollableStreamsView.onStreamOffline(
                  IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_stream_offline),
                  StreamDialogEnum.StreamOffline.getValue(), streamId, false);
            } else {
              if (this.streamId.equals(streamId)) {
                scrollableStreamsView.onError(var2.getErrorMessage());
              }
            }
          }
        });
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#joinAsViewer(String, boolean)}
   */
  @Override
  public void joinAsViewer(String streamId, boolean rejoinRequest) {
    isometrik.addViewer(new AddViewerQuery.Builder().setStreamId(streamId)
        .setViewerId(IsometrikUiSdk.getInstance().getUserSession().getUserId())
        .build(), (var1, var2) -> {

      if (var1 != null) {
        if (this.streamId.equals(streamId)) {
          scrollableStreamsView.onStreamJoined(var1.getNumberOfViewers(), rejoinRequest,
              var1.getRtcToken());

          fetchLatestMembers(streamId);
          requestStreamViewersData(streamId);
        }
      } else {

        if (var2.getHttpResponseCode() == 409 && var2.getRemoteErrorCode() == 0) {

          if (this.streamId.equals(streamId)) {
            //User already a viewer of stream
            scrollableStreamsView.onStreamJoined(-1, rejoinRequest, null);

            fetchLatestMembers(streamId);
            //fetchViewersCount();
            requestStreamViewersData(streamId);
          }
        } else if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 1) {
          //Stream not live anymore
          scrollableStreamsView.onStreamOffline(
              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_stream_offline),
              StreamDialogEnum.StreamOffline.getValue(), streamId, false);
        } else {
          if (this.streamId.equals(streamId)) {
            scrollableStreamsView.onStreamJoinError(var2.getErrorMessage());
          }
        }
      }
    });
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#leaveAsViewer(String)}
   */
  @Override
  public void leaveAsViewer(String streamId) {
    isometrik.leaveViewer(new LeaveViewerQuery.Builder().setStreamId(streamId)
        .setViewerId(IsometrikUiSdk.getInstance().getUserSession().getUserId())
        .build(), (var1, var2) -> {

    });
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#checkCopublishRequestStatus(String)}
   */
  @Override
  public void checkCopublishRequestStatus(String streamId) {
    isometrik.fetchCopublishRequestStatus(
        new FetchCopublishRequestStatusQuery.Builder().setStreamId(streamId)
            .setUserId(IsometrikUiSdk.getInstance().getUserSession().getUserId())
            .build(), (var1, var2) -> {

          if (var1 != null) {
            if (this.streamId.equals(streamId)) {
              scrollableStreamsView.onCopublishRequestStatusFetched(true, var1.isPending(),
                  var1.isAccepted());
            }
          } else {

            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {

              if (this.streamId.equals(streamId)) {
                //Copublish request not found
                scrollableStreamsView.onCopublishRequestStatusFetched(false, false, false);
              }
            } else if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 0) {
              //Stream not live anymore
              scrollableStreamsView.onStreamOffline(
                  IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_stream_offline),
                  StreamDialogEnum.StreamOffline.getValue(), streamId, false);
            } else {
              if (this.streamId.equals(streamId)) {
                scrollableStreamsView.onError(var2.getErrorMessage());
              }
            }
          }
        });
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#addCopublishRequest(String)}
   */
  @Override
  public void addCopublishRequest(String streamId) {
    isometrik.addCopublishRequest(new AddCopublishRequestQuery.Builder().setStreamId(streamId)
        .setUserId(IsometrikUiSdk.getInstance().getUserSession().getUserId())
        .build(), (var1, var2) -> {

      if (var1 != null) {
        if (this.streamId.equals(streamId)) {
          scrollableStreamsView.onCopublishRequestAdded();
        }
      } else {

        if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 1) {
          //Stream not live anymore

          scrollableStreamsView.onStreamOffline(
              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_stream_offline),
              StreamDialogEnum.StreamOffline.getValue(), streamId, false);
        } else {
          if (this.streamId.equals(streamId)) {
            scrollableStreamsView.onError(var2.getErrorMessage());
          }
        }
      }
    });
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#removeCopublishRequest(String)}
   */
  @Override
  public void removeCopublishRequest(String streamId) {
    isometrik.deleteCopublishRequest(new DeleteCopublishRequestQuery.Builder().setStreamId(streamId)
        .setUserId(IsometrikUiSdk.getInstance().getUserSession().getUserId())
        .build(), (var1, var2) -> {

      if (var1 != null) {
        if (this.streamId.equals(streamId)) {

          scrollableStreamsView.onCopublishRequestRemoved();
        }
      } else {

        if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 0) {
          //Stream not live anymore

          scrollableStreamsView.onStreamOffline(
              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_stream_offline),
              StreamDialogEnum.StreamOffline.getValue(), streamId, false);
        } else {
          if (this.streamId.equals(streamId)) {
            scrollableStreamsView.onError(var2.getErrorMessage());
          }
        }
      }
    });
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#switchProfile(String, boolean)}
   */
  @Override
  public void switchProfile(String streamId, boolean isPublic) {
    isometrik.switchProfile(new SwitchProfileQuery.Builder().setStreamId(streamId)
        .setUserId(IsometrikUiSdk.getInstance().getUserSession().getUserId())
        .setPublic(isPublic)
        .build(), (var1, var2) -> {

      if (var1 != null) {
        if (this.streamId.equals(streamId)) {

          scrollableStreamsView.onProfileSwitched(var1.getRtcToken());
        }
      } else {

        if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 0) {
          //Stream not live anymore

          scrollableStreamsView.onStreamOffline(
              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_stream_offline),
              StreamDialogEnum.StreamOffline.getValue(), streamId, false);
        } else {
          if (this.streamId.equals(streamId)) {
            scrollableStreamsView.onError(var2.getErrorMessage());
          }
        }
      }
    });
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#acceptCopublishRequest(String, String)}
   */
  @Override
  public void acceptCopublishRequest(String userId, String streamId) {

    isometrik.acceptCopublishRequest(new AcceptCopublishRequestQuery.Builder().setStreamId(streamId)
        .setUserId(userId)
        .setInitiatorId(IsometrikUiSdk.getInstance().getUserSession().getUserId())
        .build(), (var1, var2) -> {

      if (var1 != null) {
        if (this.streamId.equals(streamId)) {

          scrollableStreamsView.onCopublishRequestAcceptedApiResult(userId);
        }
      } else {
        //Stream not live anymore
        if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 0) {

          scrollableStreamsView.onStreamOffline(
              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_stream_offline),
              StreamDialogEnum.StreamOffline.getValue(), streamId, false);
        } else {
          if (this.streamId.equals(streamId)) {
            scrollableStreamsView.onError(var2.getErrorMessage());
          }
        }
      }
    });
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#declineCopublishRequest(String, String)}
   */
  @Override
  public void declineCopublishRequest(String userId, String streamId) {

    isometrik.denyCopublishRequest(new DenyCopublishRequestQuery.Builder().setStreamId(streamId)
        .setUserId(userId)
        .setInitiatorId(IsometrikUiSdk.getInstance().getUserSession().getUserId())
        .build(), (var1, var2) -> {

      if (var1 != null) {
        if (this.streamId.equals(streamId)) {

          scrollableStreamsView.onCopublishRequestDeclinedApiResult(userId);
        }
      } else {
        //Stream not live anymore
        if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 0) {

          scrollableStreamsView.onStreamOffline(
              IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_stream_offline),
              StreamDialogEnum.StreamOffline.getValue(), streamId, false);
        } else {
          if (this.streamId.equals(streamId)) {
            scrollableStreamsView.onError(var2.getErrorMessage());
          }
        }
      }
    });
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#requestStreamViewersData(String)}
   */
  @Override
  public void requestStreamViewersData(String streamId) {

    isometrik.fetchViewers(new FetchViewersQuery.Builder().setStreamId(streamId).build(),
        (var1, var2) -> {

          if (var1 != null) {
            if (this.streamId.equals(streamId)) {
              ArrayList<ViewersListModel> viewersModels = new ArrayList<>();

              ArrayList<FetchViewersResult.StreamViewer> viewers = var1.getStreamViewers();
              int size = viewers.size();

              FetchViewersResult.StreamViewer viewer;

              for (int i = 0; i < size; i++) {

                viewer = viewers.get(i);

                viewersModels.add(
                    new ViewersListModel(viewer.getViewerId(), viewer.getViewerProfilePic()));
              }

              scrollableStreamsView.onStreamViewersDataReceived(viewersModels);
            } else {

              if (!isBroadcaster) {
                scrollableStreamsView.updateMembersAndViewersCountInList(streamId, -1,
                    var1.getStreamViewers().size());
              }
            }
          } else {
            if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 2) {
              //No viewers found
              if (this.streamId.equals(streamId)) {
                scrollableStreamsView.onStreamViewersDataReceived(new ArrayList<>());
              } else {
                if (!isBroadcaster) {
                  scrollableStreamsView.updateMembersAndViewersCountInList(streamId, -1, 0);
                }
              }
            } else if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 0) {
              //Stream not live anymore
              scrollableStreamsView.onStreamOffline(
                  IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_stream_offline),
                  StreamDialogEnum.StreamOffline.getValue(), streamId, false);
            } else {
              if (this.streamId.equals(streamId)) {
                scrollableStreamsView.onError(var2.getErrorMessage());
              }
            }
          }
        });
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#requestRemoveMember(String, String)}
   */
  @Override
  public void requestRemoveMember(String streamId, String memberId) {

    isometrik.removeMember(new RemoveMemberQuery.Builder().setStreamId(streamId)
        .setMemberId(memberId)
        .setInitiatorId(IsometrikUiSdk.getInstance().getUserSession().getUserId())
        .build(), (var1, var2) -> {

      if (var1 != null) {
        if (this.streamId.equals(streamId)) {
          scrollableStreamsView.onMemberRemovedResult(memberId);
        }
      } else {
        if (var2.getHttpResponseCode() == 403 && var2.getRemoteErrorCode() == 0) {
          if (this.streamId.equals(streamId)) {
            //Stream not live anymore
            scrollableStreamsView.onStreamOffline(
                IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_stream_offline),
                StreamDialogEnum.StreamOffline.getValue(), streamId, false);
          }
        } else {
          if (this.streamId.equals(streamId)) {
            scrollableStreamsView.onError(var2.getErrorMessage());
          }
        }
      }
    });
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#requestLiveStreamsData(int, boolean)}
   */
  @Override
  public void requestLiveStreamsData(int pageSize, boolean refreshRequest) {
    isLoadingStreams = true;

    if (refreshRequest) {
      pageToken = null;
      isLastStreamsPage = false;
    }

    isometrik.fetchStreams(new FetchStreamsQuery.Builder().setCount(pageSize)
        .setPageToken(pageToken)
        .setStreamType(IMStreamType.IMAllStreams.getValue())
        .build(), (var1, var2) -> {

      if (var1 != null) {

        ArrayList<StreamsModel> streamsModels = new ArrayList<>();

        pageToken = var1.getPageToken();
        if (pageToken == null) {

          isLastStreamsPage = true;
        }

        String userId = IsometrikUiSdk.getInstance().getUserSession().getUserId();

        ArrayList<FetchStreamsResult.Stream> streams = var1.getStreams();
        int size = streams.size();

        FetchStreamsResult.Stream stream;

        for (int i = 0; i < size; i++) {

          stream = streams.get(i);
          if (!userId.equals(stream.getInitiatorId())) {

            boolean givenUserIsMember = false;
            ArrayList<String> membersIds = stream.getMemberIds();

            for (int j = 0; j < membersIds.size(); j++) {

              if (membersIds.get(j).equals(userId)) {
                givenUserIsMember = true;
                break;
              }
            }

            streamsModels.add(new StreamsModel(stream, givenUserIsMember));
          }
        }

        scrollableStreamsView.onLiveStreamsDataReceived(streamsModels, refreshRequest);
      } else {

        if (var2.getHttpResponseCode() == 404 && var2.getRemoteErrorCode() == 1) {

          if (refreshRequest) {
            //No streams found
            scrollableStreamsView.onLiveStreamsDataReceived(new ArrayList<>(), true);
          } else {
            isLastStreamsPage = true;
          }
        } else {
          scrollableStreamsView.onError(var2.getErrorMessage());
        }
      }
      isLoadingStreams = false;
    });
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#requestLiveStreamsDataOnScroll(int, int, int)}
   */
  @Override
  public void requestLiveStreamsDataOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {
    if (!isLoadingStreams && !isLastStreamsPage) {
      if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
          && firstVisibleItemPosition >= 0
          && totalItemCount >= Constants.STREAMS_PAGE_SIZE) {
        requestLiveStreamsData(Constants.STREAMS_PAGE_SIZE, false);
      }
    }
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#updateBroadcasterStatus(boolean)}
   */
  @Override
  public void updateBroadcasterStatus(boolean isBroadcaster) {
    this.isBroadcaster = isBroadcaster;
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#getMemberDetails(String, int)}
   */
  @Override
  public HashMap<String, Object> getMemberDetails(String streamId, int uid) {

    if (this.streamId.equals(streamId)) {
      Object object = memberMutedState.get(uid);
      HashMap<String, Object> map;

      if (object == null) {

        map = new HashMap<>();
      } else {
        //noinspection unchecked
        map = (HashMap<String, Object>) (object);
      }

      return map;
    } else {
      return null;
    }
  }

  /**
   * {@link ScrollableStreamsContract.Presenter#onRemoteUserMediaSettingsUpdated(String, String,
   * int,
   * String,
   * boolean,
   * boolean)}
   */
  @Override
  public void onRemoteUserMediaSettingsUpdated(String streamId, String userId, int uid,
      String userName, boolean audio, boolean muted) {
    if (this.streamId.equals(streamId)) {

      Object object = memberMutedState.get(uid);
      HashMap<String, Object> map;
      if (object == null) {
        map = new HashMap<>();
        if (audio) {
          map.put("audioMuted", muted);
          map.put("videoMuted", false);
        } else {
          map.put("audioMuted", false);
          map.put("videoMuted", muted);
        }
      } else {
        //noinspection unchecked
        map = (HashMap<String, Object>) (object);
        if (audio) {
          map.put("audioMuted", muted);
        } else {
          map.put("videoMuted", muted);
        }
      }

      map.put("userName", userName);
      map.put("userId", userId);
      memberMutedState.put(uid, map);
    }
  }
}
