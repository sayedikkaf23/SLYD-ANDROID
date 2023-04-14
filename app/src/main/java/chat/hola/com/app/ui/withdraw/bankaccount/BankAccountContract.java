package chat.hola.com.app.ui.withdraw.bankaccount;

import java.util.List;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.models.StripeResponse;


/**
 * <h1>{@link BankAccountContract}</h1>
 * <p>contains presenter and view interfaces of {@link BankAccountActivity}</p>
 *
 * @author : Hardik Karkar
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 06 March 2020
 */

public interface BankAccountContract {

    interface View extends BaseView {

        /**
         * <h1>This method used to show loading view</h1>
         */
        void showLoader();

        /**
         * <h1>This method used to hide loading view</h1>
         */
        void hideLoader();

        /**
         * <h1>This method is used to show fetched bank account</h1>
         *
         * @param data
         */
        void showBankAccount(List<StripeResponse.Data.ExternalAccounts.Account> data, String fee);

        /**
         * To check account setup
         *
         * @param statusMessage : status message
         * @param statusCode : s
         */
        void setStatus(String statusMessage, int statusCode);

        void setList(String id, List<StripeResponse.Data.ExternalAccounts.Account> accounts, String fee);
    }

    interface Presenter extends BasePresenter<View> {

        /**
         * <h1>To fetch the saved bank account of current user</h1>
         */
        void getBankAccounts();
    }
}
