package chat.hola.com.app.ui.validate;

import android.content.Context;

import javax.inject.Inject;

import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.ui.kyc.KycActivity;

/**
 * <h1>IdentificationPresenter</h1>
 *
 * <p>This is implemented presenter of {@link KycActivity},
 * it will call apis and handles apis responses and
 * communicate between models and {@link  KycActivity}</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 05 Dec 2019
 */
public class ValidatePresenter implements ValidateContract.Presenter {

    private ValidateContract.View view;

    @Inject
    Context context;
    @Inject
    HowdooService service;
    @Inject
    SessionManager sessionManager;


    @Inject
    public ValidatePresenter() {

    }

    @Override
    public void attach(ValidateContract.View view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
    }


    @Override
    public void checkKyc() {

    }
}
