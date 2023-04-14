package chat.hola.com.app.home.callhistory;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import chat.hola.com.app.ModelClasses.CallItem;
import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.cameraActivities.manager.CameraOutputModel;
import chat.hola.com.app.home.stories.model.StoryData;
import chat.hola.com.app.home.stories.model.StoryPost;

/**
 * <h1>CallHistoryContract<h1/>
 *
 * @author 3Embed
 * @version 1.0
 * @since 09/07/21
 */

public interface CallHistoryContract {

    interface View extends BaseView {
        void setupRecyclerView();

        void isLoading(boolean flag);

        void showCallHistory(ArrayList<CallItem> callArray, boolean isFirstPage);

        void isDataAvailable(boolean empty);

        void noData(boolean isFirstPage);

        void loadData();


    }

    interface Presenter extends BasePresenter<View> {

        void init();

        void loadData(int skip, int limit);

        void callHistoryObserver();
    }
}
