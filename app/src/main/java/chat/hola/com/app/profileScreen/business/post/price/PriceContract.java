package chat.hola.com.app.profileScreen.business.post.price;


import java.util.List;

import chat.hola.com.app.models.Currency;

/**
 * <h1>PriceContract</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 09 September 2019
 */
public interface PriceContract {

    interface View {

        /**
         * sets the currency on dropdown
         */
        void currency(List<Currency> currencyList);
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
        void currency();
    }
}
