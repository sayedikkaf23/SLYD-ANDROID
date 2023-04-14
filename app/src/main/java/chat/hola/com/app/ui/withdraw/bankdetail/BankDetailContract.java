package chat.hola.com.app.ui.withdraw.bankdetail;

import android.content.Context;

import chat.hola.com.app.base.BasePresenter;
import chat.hola.com.app.base.BaseView;
import chat.hola.com.app.models.StripeResponse;

public interface BankDetailContract {
    interface View extends BaseView {

        void bankDeleted();
    }

    interface Presenter extends BasePresenter<View> {
        void deleteBank(StripeResponse.Data.ExternalAccounts.Account data);
    }
}
