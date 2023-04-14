package chat.hola.com.app.ui.withdraw.addbankaccount;

import java.util.List;
import java.util.Map;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.ui.withdraw.model.bankfield.Field;


/**
 * <h1>{@link AddBankAccountContract}</h1>
 * <p>contains presenter and view interfaces of {@link AddBankAccountActivity}</p>
 *
 * @author : Hardik Karkar
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 06 March 2020
 */

public interface AddBankAccountContract {

    interface View extends BaseView {

        /**
         * <h1>Used to show loading view</h1>
         * */
        void showLoader();

        /**
         * <h1>Used to hide loading view</h1>
         * */
        void hideLoader();

        /**
         * <h1>Used to show fields in view which is fetch from api</h1>
         * @param fields list of fields.
         * */
        void showFields(List<Field> fields);

        /**
         * <h1>Bank account added successfully to server</h1>
         * */
        void bankAccountAdded();
    }

    interface Presenter extends BasePresenter<View> {

        /**
         * <h1>Used to get required bank fields</h1>
         *
         * @param paymentGateWayId*/
        void getBankFields(String paymentGateWayId);

        /**
         * <h1>Used to add bank account through api</h1>
         * @param fieldList user filled list
         * @param country country of bank account
         * */
        void addBankAccount(List<Field> fieldList, String country);

        void addBank(Map<String, String> params);
    }
}
