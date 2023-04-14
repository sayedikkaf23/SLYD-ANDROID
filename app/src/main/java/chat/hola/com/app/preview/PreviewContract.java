package chat.hola.com.app.preview;

import java.util.List;

import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.home.stories.model.Viewer;

/**
 * <h1>PreviewContract</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 4/24/2018.
 */

public interface PreviewContract {

    interface View extends BaseView {


        void updateViewerList(String storyId, List<Viewer> data, boolean isClear);
    }

    interface Presenter {

        void viewStory(String storyId);

        void getViewerList(String storyId, int skip, int limit);

        void viewerListScroll(String storyId, int visibleItemCount, int firstVisibleItemPosition, int totalItemCount);
    }
}
