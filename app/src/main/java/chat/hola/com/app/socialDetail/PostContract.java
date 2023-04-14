package chat.hola.com.app.socialDetail;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;

/**
 * <h1>PostContract</h1>
 *
 * @author 3Embed
 * @since 24/2/2018.
 */

public interface PostContract {

    interface View extends BaseView {

        /**
         * <p>called when liked/unliked action completed</p>
         *
         * @param flag : contains boolean true/false is liked or not
         */
        void liked(boolean flag);

        void deleted();
    }

    interface Presenter extends BasePresenter<View> {
        /**
         * <p>to like the post</p>
         *
         * @param postId : posts id
         */
        void like(String postId);

        /**
         * <p>to unlike the post</p>
         *
         * @param postId : posts id
         */
        void unlike(String postId);

        /**
         * <p>to delete the post</p>
         *
         * @param postId : posts id
         */
        void deletePost(String postId);

        /**
         * <p>to report a post</p>
         *
         * @param postId:posts id
         * @param reason       : selected reason for report
         * @param message      : report message
         */
        void reportPost(String postId, String reason, String message);
    }
}
