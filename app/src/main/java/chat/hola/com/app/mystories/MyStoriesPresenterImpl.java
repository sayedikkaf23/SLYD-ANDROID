package chat.hola.com.app.mystories;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;

/**
 * Created by embed on 13/12/18.
 */

public interface MyStoriesPresenterImpl
{
    interface MyStoriesPresenterView extends BaseView
    {
        void onStoriesDeleteSuccess(int position);
    }

    interface MyStoriesPresent extends BasePresenter
    {

        void onToDeleteStory(String storyId,int position);
    }
}
