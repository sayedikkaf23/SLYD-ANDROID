package chat.hola.com.app.subscription.cancelled;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;
import com.ezcall.android.databinding.FragmentActiveBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.subscription.adapter.StatusAdapter;
import chat.hola.com.app.subscription.model.SubData;
import dagger.android.support.DaggerFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CancelledFragment} factory method to
 * create an instance of this fragment.
 */
public class CancelledFragment extends DaggerFragment implements CancelledContract.View, StatusAdapter.ClickListener {

    private LinearLayoutManager layoutManager;

    @Inject
    public CancelledFragment() {
        // Required empty public constructor
    }

    FragmentActiveBinding mDataBinding;
    @Inject
    CancelledPresenter presenter;
    List<SubData> list = new ArrayList<>();
    StatusAdapter adapter;
    private MediaPlayer mediaPlayer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        presenter.attach(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDataBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_active,container,false);
        // Inflate the layout for this fragment
        return mDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new StatusAdapter(getActivity(),list,false);
        adapter.setClickListener(this);
        layoutManager = new LinearLayoutManager(getActivity());
        mDataBinding.recyclerView.setLayoutManager(layoutManager);
        mDataBinding.recyclerView.setAdapter(adapter);
        mDataBinding.recyclerView.addOnScrollListener(recyclerViewOnScrollListener);

        refresh();
        mediaPlayer = MediaPlayer.create(getContext(), R.raw.coin_spend);
        mDataBinding.swipeRefresh.setOnRefreshListener(() -> {
            refresh();
        });
    }

    @Override
    public void refresh() {
        CancelledPresenter.page = 0;
        presenter.getSubscriptions(CancelledPresenter.page, CancelledPresenter.PAGE_SIZE);
    }

    @Override
    public void showEmpty(boolean b) {
        mDataBinding.emptyTitle.setText(getString(R.string.no_cancelled_subscriptions));
        mDataBinding.llEmpty.setVisibility(b?View.VISIBLE:View.GONE);
    }

    public RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            presenter.callApiOnScroll(firstVisibleItemPosition, visibleItemCount, totalItemCount);
        }
    };

    @Override
    public void showData(List<SubData> data, boolean clear) {
        if(clear)list.clear();
            adapter.setData(data);
        if(data!=null && !data.isEmpty()) {
            if(clear)list.clear();
            list.addAll(data);
            adapter.setData(data);
            showEmpty(false);
        }else if(clear) {
            showEmpty(true);
        }
    }

    @Override
    public void onSuccessSubscribe(boolean isChecked) {
        if (isChecked)
            mediaPlayer.start();
        refresh();
    }

    @Override
    public void insufficientBalance() {
        Utilities.openInsufficientBalanceDialog(getActivity());
        //showSnackMsg(getString(R.string.insufficient_balance_message));
    }

    @Override
    public void message(String message) {
        showSnackMsg(message);
    }

    @Override
    public void message(int message) {
        showSnackMsg(getString(R.string.message));
    }

    public void showSnackMsg(String msg) {
        Snackbar snackbar = Snackbar.make(mDataBinding.root, "" + msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setGravity(Gravity.CENTER_HORIZONTAL);
    }

    @Override
    public void showLoader() {
        mDataBinding.swipeRefresh.setRefreshing(true);
    }

    @Override
    public void hideLoader() {
        mDataBinding.swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onItemClick(int position, SubData d) {
        presenter.subscribeStarUser(true,d.getId());
    }
}