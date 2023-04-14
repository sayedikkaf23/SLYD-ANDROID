package chat.hola.com.app.ui.withdraw.detail;

import java.util.List;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.models.WithdawStatus;
import chat.hola.com.app.ui.withdraw.bankaccount.BankAccountActivity;


/**
 * <h1>{@link WithdrawDetailContract}</h1>
 * <p>contains presenter and view interfaces of {@link BankAccountActivity}</p>
 *
 * @author : Hardik Karkar
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 06 March 2020
 */

public interface WithdrawDetailContract {

    interface View extends BaseView {

        /**
         * <h1>This method used to show loading view</h1>
         */
        void showLoader();

        /**
         * <h1>This method used to hide loading view</h1>
         */
        void hideLoader();

        void withdawStatuses(List<WithdawStatus> withdawStatuses);
    }

    interface Presenter extends BasePresenter<View> {

        void withdawStatuses(String withdawId);
    }
}
