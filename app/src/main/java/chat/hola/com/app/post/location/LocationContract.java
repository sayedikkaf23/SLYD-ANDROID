package chat.hola.com.app.post.location;


import android.content.Context;

/**
 * <h1>LocationContract</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 23 September 2019
 */
public interface LocationContract {

    interface View {
        /**
         * to show loader
         */
        void showLoader();

        /**
         * to hide loader
         */
        void hideLoader();

        /**
         * refresh data;
         */
        void refresh();
    }

    interface Presenter {

        /**
         * searches the locations and returns
         *
         * @param searchText : search text
         */
        void location(String searchText);

        /**
         * gets nearby locations
         */
        void nearByLocation(Context context);
    }
}
