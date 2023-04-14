package chat.hola.com.app.dublycategory.fagments;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;

/**
 * Created by ankit on 23/2/18.
 */

public interface DubFragmentContract {

  interface View extends BaseView {
    void setMax(Integer max);

    void startedDownload();

    void progress(int downloadedSize);

    void finishedDownload(String path, String name, String musicId);

    void play(String audio, boolean isPlaying, int position, String duration);

    void isLoading(boolean flag);
  }

  interface Presenter extends BasePresenter<View> {

    void loadData(int skip, int limit);

    void startedDownload();
  }
}
