package chat.hola.com.app.ui.cards;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.appscrip.stripe.Constants;
import com.appscrip.stripe.StripePaymentIntentActivity;
import com.ezcall.android.R;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.base.BaseActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Card;
import chat.hola.com.app.ui.adapter.CardAdapter;
import chat.hola.com.app.ui.recharge.RechargeActivity;

public class CardActivity extends BaseActivity implements CardContract.View, CardAdapter.ClickListner {

    private Card selectedCard;

    @Inject
    SessionManager sessionManager;
    @Inject
    CardPresenter mPresenter;
    @Inject
    Loader loader;
    @Inject
    List<Card> cards;
    @Inject
    CardAdapter cardAdapter;

    @BindView(R.id.btnAddCard)
    Button btnAddCard;
    @BindView(R.id.btnConfirm)
    Button btnConfirm;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.llEmpty)
    LinearLayout llEmpty;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_card;
    }

    @Override
    public void initView() {
        super.initView();
        mPresenter.attach(this);
        refresh.setOnRefreshListener(() -> mPresenter.cards(CardActivity.this));

        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(cardAdapter);
        cardAdapter.setClickListner(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.cards(this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detach();
        super.onDestroy();
    }

    @OnClick(R.id.btnConfirm)
    public void btnConfirm() {
        if (selectedCard != null) {
            startActivity(new Intent(this, RechargeActivity.class).putExtra("card", selectedCard));
        } else {
            message(R.string.select_card_message);
        }
    }

    @OnClick(R.id.ibBack)
    public void ibBack() {
        super.onBackPressed();
    }

    @OnClick(R.id.btnAddCard)
    public void btnAddCard() {
        Intent intent = new Intent(this, StripePaymentIntentActivity.class);
        intent.putExtra(Constants.USER_ID, sessionManager.getUserId());
        intent.putExtra(Constants.AUTHORIZATION, AppController.getInstance().getApiToken());
        intent.putExtra(Constants.LANGUAGE, chat.hola.com.app.Utilities.Constants.LANGUAGE);
        startActivity(intent);
    }

    @Override
    public void message(String message) {
        runOnUiThread(() -> Toast.makeText(CardActivity.this, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void message(int message) {
        message(getString(message));
    }

    @Override
    public void showLoader() {
        runOnUiThread(() -> loader.show());
    }

    @Override
    public void hideLoader() {
        runOnUiThread(() -> {
            if (loader != null && loader.isShowing())
                loader.dismiss();
            if (refresh.isRefreshing())
                refresh.setRefreshing(false);
        });
    }

    @Override
    public void setCardData(List<Card> cards) {
        runOnUiThread(() -> {
            rvList.setVisibility(cards.isEmpty() ? View.GONE : View.VISIBLE);
            llEmpty.setVisibility(!cards.isEmpty() ? View.GONE : View.VISIBLE);
            this.cards = cards;
            cardAdapter.setData(cards);
        });
    }

    @Override
    public void onCardSelect(Card card) {
        selectedCard = card;
        btnConfirm.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCardDelete(Card card) {
        mPresenter.deleteCard(this, card);
    }
}
