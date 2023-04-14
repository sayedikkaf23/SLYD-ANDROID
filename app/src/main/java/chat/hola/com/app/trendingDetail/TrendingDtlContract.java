package chat.hola.com.app.trendingDetail;

import android.content.Intent;

import java.util.List;

import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.home.model.Location;
import chat.hola.com.app.profileScreen.channel.Model.ChannelData;

/**
 * <h1>TrendingDtlContract</h1>
 *
 * @author ankit
 * @version 1.0
 * @since 21/2/18.
 */

public interface TrendingDtlContract {

    interface View extends BaseView {
        /**
         * <p>applies fonts</p>
         */
        void applyingFont();

        /**
         * <p>initialize recycler view</p>
         */
        void initDetailRecycler();

        /**
         * <p>sets data to the screen</p>
         *
         * @param data : channel's data
         */
        void showData(chat.hola.com.app.profileScreen.channel.Model.ChannelData data, String text, int drawable, int onText, int offText, boolean isChecked);

        /**
         * <p>its invalidates the subscribe button</p>
         *
         * @param isChecked : based on parameter we enables and disables the button
         */
        void invalidateSubsButton(boolean isChecked);

        void invalidateUnSubsButton(boolean isChecked);

        /**
         * <p>sets data</p>
         *
         * @param data : channel data
         */
        void setData(ChannelData data, int onText, int offText, boolean isChecked);

        /**
         * <p>calls when item click action happens</p>
         * @param position : item's position
         * @param view     : item view
         * @param data
         */
        void itemClick(int position, android.view.View view, List<Data> data);

        void tbSubscribeVisibility(boolean isVisible);

        void llSubscribeVisibility(boolean isVisible);

        void mapVisibility(boolean isVisible);

        void setLocation(Location latlong);
    }

    interface Presenter {
        /**
         * <p>initializes</p>
         */
        void init();

        /**
         * <p>subscribes the channel</p>
         *
         * @param channelId: channel id
         */
        void subscribeChannel(String channelId);

        /**
         * <p>unsubscribes the channel</p>
         *
         * @param channelId: channel id
         */
        void unSubscribeChannel(String channelId);

        /**
         * <p>gets the categories data</p>
         *
         * @param category : category id
         * @param skip
         * @param limit
         */
        void getCategoryData(String category, int skip, int limit);

        /**
         * <p>gets the channels data</p>
         *
         * @param channelId: channels id
         */
        void getChannelData(String channelId);

        /**
         * <p>gets hashtags data</p>
         *
         * @param hashtag: hashtag string
         */
        void getHashTagData(String hashtag);

        /**
         * <p>gets location data</p>
         *
         * @param stringExtra
         * @param location : place name
         */
        void getLocationData(String stringExtra, String location);

        /**
         * <p>sets the channels data</p>
         *
         * @param data: channels data
         */
        void setData(ChannelData data);

        void selectType(Intent intent);

        void callApiOnScroll(String postId, int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) ;
    }
}
