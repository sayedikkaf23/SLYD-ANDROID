package chat.hola.com.app.search.locations;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import chat.hola.com.app.Networking.HowdooService;

/**
 * Created by ankit on 24/2/18.
 */

public class LocationsPresenter implements LocationsContract.Presenter {

    @Nullable
    private LocationsContract.View view;
    @Inject
    HowdooService service;


    @Inject
    LocationsPresenter() {
    }

    @Override
    public void attachView(LocationsContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {

    }

    @Override
    public void search(CharSequence charSequence) {

    }
}
