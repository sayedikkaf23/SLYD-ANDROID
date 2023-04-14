package io.isometrik.groupstreaming.ui.members.list;

import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.utils.DateUtil;
import io.isometrik.gs.events.member.MemberAddEvent;
import io.isometrik.gs.response.member.FetchMembersResult;

/**
 * The type Members model.
 */
class MembersModel {

  private boolean isPublishing;
  private boolean isAdmin;
  private String memberId;
  private String memberName;
  private String memberIdentifier;
  private String memberProfilePic;
  private String joinTime;

  private boolean canRemoveMember;

  /**
   * Instantiates a new Members model.
   *
   * @param member the member
   * @param canRemoveMember the can remove member
   */
  MembersModel(FetchMembersResult.StreamMember member, boolean canRemoveMember) {

    memberId = member.getMemberId();

    if (memberId.equals(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {

      memberName = IsometrikUiSdk.getInstance()
          .getContext()
          .getString(R.string.ism_you, member.getMemberName());
    } else {
      memberName = member.getMemberName();
    }

    memberIdentifier = member.getMemberIdentifier();
    memberProfilePic = member.getMemberProfilePic();

    if (member.getJoinTime() != 0) {
      joinTime = DateUtil.getDate(member.getJoinTime());
    } else {
      joinTime = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_not_publishing);
    }
    isPublishing = member.getPublishing();
    isAdmin = member.getAdmin();

    this.canRemoveMember = canRemoveMember;
  }

  /**
   * Instantiates a new Members model.
   *
   * @param memberAddEvent the member add event
   * @param canRemoveMember the can remove member
   */
  MembersModel(MemberAddEvent memberAddEvent, boolean canRemoveMember) {

    memberId = memberAddEvent.getMemberId();

    if (memberId.equals(IsometrikUiSdk.getInstance().getUserSession().getUserId())) {

      memberName = IsometrikUiSdk.getInstance()
          .getContext()
          .getString(R.string.ism_you, memberAddEvent.getMemberName());
    } else {
      memberName = memberAddEvent.getMemberName();
    }

    memberIdentifier = memberAddEvent.getMemberIdentifier();
    memberProfilePic = memberAddEvent.getMemberProfilePic();
    joinTime = IsometrikUiSdk.getInstance().getContext().getString(R.string.ism_not_publishing);

    isPublishing = false;
    isAdmin = false;
    this.canRemoveMember = canRemoveMember;
  }

  /**
   * Is publishing boolean.
   *
   * @return the boolean
   */
  boolean isPublishing() {
    return isPublishing;
  }

  /**
   * Is admin boolean.
   *
   * @return the boolean
   */
  boolean isAdmin() {
    return isAdmin;
  }

  /**
   * Gets member id.
   *
   * @return the member id
   */
  String getMemberId() {
    return memberId;
  }

  /**
   * Gets member name.
   *
   * @return the member name
   */
  String getMemberName() {
    return memberName;
  }

  /**
   * Gets member identifier.
   *
   * @return the member identifier
   */
  String getMemberIdentifier() {
    return memberIdentifier;
  }

  /**
   * Gets member profile pic.
   *
   * @return the member profile pic
   */
  String getMemberProfilePic() {
    return memberProfilePic;
  }

  /**
   * Gets join time.
   *
   * @return the join time
   */
  String getJoinTime() {
    return joinTime;
  }

  /**
   * Is can remove member boolean.
   *
   * @return the boolean
   */
  boolean isCanRemoveMember() {
    return canRemoveMember;
  }

  /**
   * Sets publishing.
   *
   * @param publishing the publishing
   */
  void setPublishing(boolean publishing) {
    isPublishing = publishing;
  }

  /**
   * Sets jointime
   *
   * @param joinTime the time of stream group join
   */
  public void setJoinTime(String joinTime) {
    this.joinTime = joinTime;
  }
}
