package chat.hola.com.app.profileScreen.business.post.type;


/**
 * <h1>PostTypeContract</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 09 September 2019
 */
public interface PostTypeContract {

    interface View {

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
        void postType();
    }
}
