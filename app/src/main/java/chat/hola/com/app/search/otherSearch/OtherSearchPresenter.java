package chat.hola.com.app.search.otherSearch;

import androidx.annotation.Nullable;

import javax.inject.Inject;

/**
 * Created by ankit on 24/2/18.
 */

public class OtherSearchPresenter implements OtherSearchContract.Presenter {

    @Nullable
    private OtherSearchContract.View view;

    @Inject
    OtherSearchPresenter(){
    }

    @Override
    public void attachView(OtherSearchContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {

    }
}
