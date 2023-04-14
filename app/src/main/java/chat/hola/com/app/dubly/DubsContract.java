package chat.hola.com.app.dubly;

import chat.hola.com.app.Utilities.BaseView;

/**
 * <h1>DubCategoryContract</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 7/16/2018.
 */

public interface DubsContract {

    interface View extends BaseView {
        void play(String audio, boolean isPlaying, int position, String duration);

        void progress(int i);

        void finishedDownload(String s, String filename, String musicId);

        void startDownload(String url, String musicId, String name);

        void startedDownload();
    }

    interface Presenter {

        void getDubs(int offset, int limit, String categoryId);

        void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount, String categoryId);

        ClickListner getPresenter();

        void startedDownload();

        void download(int position);
    }
}
