package chat.hola.com.app.DublyCamera;

public class ProgressBarModel {


    private boolean isPlaying;

    private String filterColor="";
    private boolean filterSelected;
    private FilterType selectedGlFilter;

    private String videoPath;

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    private boolean isVideoItem;


    private int progressViewWidth;

    private int cumulativeMusicLengthInMs;

    public int getCumulativeMusicLengthInMs() {
        return cumulativeMusicLengthInMs;
    }

    public  void setCumulativeMusicLengthInMs(int cumulativeMusicLengthInMs) {
        this.cumulativeMusicLengthInMs = cumulativeMusicLengthInMs;
    }

    public boolean isVideoItem() {
        return isVideoItem;
    }

    public  int getProgressViewWidth() {
        return progressViewWidth;
    }

    public void setProgressViewWidth(int progressViewWidth) {
        this.progressViewWidth = progressViewWidth;
    }

    public   void setVideoItem(boolean videoItem) {
        isVideoItem = videoItem;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public  void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public  String getFilterColor() {
        return filterColor;
    }

    public  void setFilterColor(String filterColor) {
        this.filterColor = filterColor;
    }

    public  boolean isFilterSelected() {
        return filterSelected;
    }

    public void setFilterSelected(boolean filterSelected) {
        this.filterSelected = filterSelected;
    }

    public  FilterType getSelectedGlFilter() {
        return selectedGlFilter;
    }

    public  void setSelectedGlFilter(FilterType selectedGlFilter) {
        this.selectedGlFilter = selectedGlFilter;
    }
}
