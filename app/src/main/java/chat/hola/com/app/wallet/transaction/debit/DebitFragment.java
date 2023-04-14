package chat.hola.com.app.wallet.transaction.debit;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ezcall.android.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.WalletTransactionData;
import chat.hola.com.app.wallet.transaction.Model.TransactionData;
import chat.hola.com.app.wallet.transaction.TransactionAdap;
import dagger.android.support.DaggerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class DebitFragment extends DaggerFragment implements DebitContract.View, SwipeRefreshLayout.OnRefreshListener {


    ArrayList<WalletTransactionData> debitList = new ArrayList<>();

    @Inject
    public DebitFragment() {
        // Required empty public constructor
    }

    private Unbinder unbinder;
    @BindView(R.id.rV_transaction)
    RecyclerView rV_transaction;
    public TransactionAdap transactionAdap;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.llError)
    LinearLayout llError;
    @Inject
    DebitPresenter presenter;

    LinearLayoutManager linearLayoutManager;
    private String pageState = null;
    private Integer type;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int PAGE_SIZE = 50;
    private int page = 0;
    @Inject
    SessionManager sessionManager;

    public RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = linearLayoutManager.getChildCount();
            int totalItemCount = linearLayoutManager.getItemCount();
            int[] firstVisibleItemPositions = new int[PAGE_SIZE];
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            presenter.callApiOnScroll(type, false, pageState, firstVisibleItemPosition, visibleItemCount, totalItemCount);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_debit, container, false);
        unbinder = ButterKnife.bind(this, view);

        presenter.attachView(this);

        unbinder = ButterKnife.bind(this, view);

        presenter.init();

        swipeRefresh.setOnRefreshListener(this);

        type = Constants.Transaction.Type.DEBIT;
        presenter.getTransactionList(sessionManager.getCoinWalletId(), Constants.Transaction.Type.DEBIT, true, null);

        return view;
    }

    @Override
    public void recyclerViewSetup() {
        transactionAdap = new TransactionAdap(getActivity(), debitList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rV_transaction.setHasFixedSize(false);
        rV_transaction.setLayoutManager(linearLayoutManager);
        rV_transaction.addOnScrollListener(recyclerViewOnScrollListener);
        rV_transaction.setAdapter(transactionAdap);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }

    @Override
    public void showMessage(String msg, int msgId) {

    }

    @Override
    public void sessionExpired() {

    }

    @Override
    public void isInternetAvailable(boolean flag) {

    }

    @Override
    public void userBlocked() {

    }

    @Override
    public void reload() {

    }

    @Override
    public void onRefresh() {
        if (!isLoading) {
            debitList.clear();
            page = 0;
            presenter.getTransactionList(sessionManager.getCoinWalletId(), Constants.Transaction.Type.DEBIT, true, null);
        }
    }


    @Override
    public void isDataLoading(boolean b) {
        if (swipeRefresh != null)
            swipeRefresh.setRefreshing(b);
    }

    @Override
    public void setData(List<WalletTransactionData> data, String pageState, boolean isFirst) {
        this.pageState = pageState;
        llError.setVisibility(View.GONE);
        swipeRefresh.setVisibility(data.isEmpty() ? View.GONE : View.VISIBLE);
        if (data.isEmpty())
            noData();
        if (data.size() < PAGE_SIZE)
            isLastPage = true;
        if (isFirst)
            this.debitList.clear();
        this.debitList.addAll(data);
        transactionAdap.setData(debitList);
    }

    @Override
    public void noData() {
        llError.setVisibility(View.VISIBLE);
    }
}
