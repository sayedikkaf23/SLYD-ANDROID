package chat.hola.com.app.profileScreen.business.post.actionbutton;


import java.util.List;

import chat.hola.com.app.models.ActionButtonResponse;

/**
 * <h1>PriceContract</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 09 September 2019
 */
public interface ActionButtonContract {

    interface View {
        void defaultItemSelected(ActionButtonResponse.BizButton bizButton);

        /**
         * sets the currency on dropdown
         */
//        void buttonText(List<ActionButtonResponse.ButtonText> currencyList);
    }

    interface Presenter {
        /**
         * attache view
         *
         * @param view : {@link View}
         */
        void attacheView(View view);

        /**
         * detached view
         */
        void detachedView();

        /**
         * load currency
         */
        void buttonText();
    }
}
