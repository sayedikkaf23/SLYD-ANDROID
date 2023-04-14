package io.isometrik.groupstreaming.ui.viewers;

public class ViewersListModel {

  private String viewerId;
  private String viewerProfilePic;

  public ViewersListModel(String viewerId, String viewerProfilePic) {
    this.viewerId = viewerId;
    this.viewerProfilePic = viewerProfilePic;
  }

  public String getViewerId() {
    return viewerId;
  }

  public String getViewerProfilePic() {
    return viewerProfilePic;
  }
}
