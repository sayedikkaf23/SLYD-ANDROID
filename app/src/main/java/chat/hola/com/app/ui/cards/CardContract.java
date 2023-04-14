package chat.hola.com.app.ui.cards;

import android.content.Context;

import java.util.List;

import chat.hola.com.app.base.BasePresenter;
import chat.hola.com.app.base.BaseView;
import chat.hola.com.app.models.Card;

public interface CardContract {
    interface View extends BaseView {

        void setCardData(List<Card> cards);
    }

    interface Presenter extends BasePresenter<View> {
        void cards(Context context);

        void deleteCard(Context context, Card card);
    }
}
