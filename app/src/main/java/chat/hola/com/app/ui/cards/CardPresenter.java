package chat.hola.com.app.ui.cards;

import android.content.Context;

import com.appscrip.stripe.AccountsDelegate;
import com.appscrip.stripe.UserAccounts;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Card;

public class CardPresenter implements CardContract.Presenter {

    private CardContract.View mView;

    @Inject
    SessionManager sessionManager;

    @Inject
    CardPresenter() {
    }

    @Override
    public void cards(Context context) {
        if (mView != null) mView.showLoader();
        try {
            UserAccounts accounts = new UserAccounts(context);
            accounts.getCards(AppController.getInstance().getApiToken(), Constants.LANGUAGE, sessionManager.getUserId(), new AccountsDelegate() {
                @Override
                public void onSuccess(@NotNull Object successData) {
                    if (mView != null) mView.hideLoader();
                    List<Card> cards = new Gson().fromJson(successData.toString(), new TypeToken<List<Card>>() {
                    }.getType());
                    if (mView != null) mView.setCardData(cards);
                }

                @Override
                public void onFailure(@NotNull String failure) {
                    if (mView != null) {
                        mView.message(failure);
                        mView.hideLoader();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCard(Context context, Card card) {
        try {
            UserAccounts accounts = new UserAccounts(context);
            accounts.deleteCard(AppController.getInstance().getApiToken(), Constants.LANGUAGE, card.getId(), new AccountsDelegate() {
                @Override
                public void onSuccess(@NotNull Object successData) {
                    if (mView != null) {
                        mView.hideLoader();
                        mView.message("Card removed");
                    }
                    cards(context);
                }

                @Override
                public void onFailure(@NotNull String failure) {
                    if (mView != null) {
                        mView.message(failure);
                        mView.hideLoader();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void attach(CardContract.View view) {
        mView = view;
    }

    @Override
    public void detach() {
        mView = null;
    }
}
