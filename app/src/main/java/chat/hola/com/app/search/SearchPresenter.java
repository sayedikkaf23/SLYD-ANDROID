package chat.hola.com.app.search;

import javax.inject.Inject;

import chat.hola.com.app.Networking.HowdooService;

/**
 * Created by ${3embed} on ${27-10-2017}.
 * Banglore
 */

public class SearchPresenter implements SearchContract.Presenter {

    @Inject
    SearchContract.View view;

    @Inject
    HowdooService service;

    @Inject
    public SearchPresenter() {
    }

    @Override
    public void stop() {
        view.stop();
    }



}
