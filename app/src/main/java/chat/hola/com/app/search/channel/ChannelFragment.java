package chat.hola.com.app.search.channel;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.R;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.search.channel.module.Channels;
import chat.hola.com.app.trendingDetail.TrendingDetail;
import dagger.android.support.DaggerFragment;

/**
 * Created by ${3embed} on ${27-10-2017}.
 * Banglore
 */

public class ChannelFragment extends DaggerFragment implements ChannelContract.View, ChannelAdapter.ClickListner {

    @Inject
    ChannelPresenter presenter;

    private ChannelAdapter madapter;

    private RecyclerView.LayoutManager mlayoutManager;

    @BindView(R.id.howdooSugTv)
    TextView howdooSugTv;
    @BindView(R.id.peopleRv)
    RecyclerView peopleRv;
    @BindView(R.id.llEmpty)
    LinearLayout llEmpty;
    @BindView(R.id.ivEmpty)
    ImageView ivEmpty;
    private Unbinder unbinder;
    private EditText searchInputEt;
    List<Channels> data;
    @Inject
    BlockDialog dialog;

    @Override
    public void userBlocked() {
        dialog.show();
    }
    public ChannelFragment() {
    }

    @Inject
    SessionManager sessionManager;

    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(getContext());
    }

    @Override
    public void isInternetAvailable(boolean flag) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_people, container, false);
        unbinder = ButterKnife.bind(this, view);
        searchInputEt = (EditText) getActivity().findViewById(R.id.searchInputEt);

        return view;
    }

    @Override
    public void noData() {
        peopleRv.setVisibility(View.GONE);
        llEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        searchInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.length() > 0)
                    presenter.search(charSequence);
//                else
//                    presenter.search(searchInputEt.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        madapter = new ChannelAdapter();
        madapter.setListener(this);
        peopleRv.setHasFixedSize(true);
        mlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        peopleRv.setLayoutManager(mlayoutManager);
        peopleRv.setItemAnimator(new DefaultItemAnimator());
        peopleRv.setAdapter(madapter);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachView(this);
        presenter.search(searchInputEt.getText().toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void showMessage(String msg, int msgId) {
        if (msg != null && !msg.isEmpty()) {
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        } else if (msgId != 0) {
            Toast.makeText(getContext(), getResources().getString(msgId), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), TrendingDetail.class);
        intent.putExtra("channelId", data.get(position).getId());
        intent.putExtra("call", "channel");
        startActivity(intent);
    }

    @Override
    public void onSubscribe(int position, boolean flag) {
        if (!flag)
            presenter.subscribeChannel(data.get(position).getId());
        else
            presenter.unSubscribeChannel(data.get(position).getId());
    }

    @Override
    public void showData(List<Channels> data) {
        this.data = data;
        try {
            ivEmpty.setImageDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.ic_channel_gray));
        }catch (NullPointerException ignored){

        }
        peopleRv.setVisibility(data.isEmpty() ? View.GONE : View.VISIBLE);
        llEmpty.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
        madapter.setData(getContext(), data);
    }

    @Override
    public void reload() {

    }
}
