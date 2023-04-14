package chat.hola.com.app.poststatus;

import android.content.Context;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;

/**
 * Created by embed on 15/12/18.
 */

public interface PostStatusPresenterImpl {

    interface PostStatusPresenterView extends BaseView{
        void onPostStatusSuccess();
        void showProgress();
        void hideProgress();
    }

    interface PostStatusPresent extends BasePresenter{

        void postStatus(Context mContext, String absolutePath, int type, boolean isPrivate, String bgcolor, String statusMsg, String fontType);

    }

}
