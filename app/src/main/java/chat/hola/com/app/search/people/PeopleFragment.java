package chat.hola.com.app.search.people;

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
import chat.hola.com.app.home.contact.Friend;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.search.SearchAdapter;
import dagger.android.support.DaggerFragment;

/**
 * Created by ${3embed} on ${27-10-2017}.
 * Banglore
 */

public class PeopleFragment extends DaggerFragment implements PeopleContract.View, SearchAdapter.ClickListner {

    @Inject
    PeoplePresenter presenter;

    private SearchAdapter madapter;
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
    List<Friend> data;

    public PeopleFragment() {
    }

    @Inject
    SessionManager sessionManager;
    @Inject
    BlockDialog dialog;

    @Override
    public void userBlocked() {
        dialog.show();
    }

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        searchInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.length() > 3)
                    presenter.search(charSequence);
//                else if (searchInputEt.getText().toString().length()>3)
//                    presenter.search(searchInputEt.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        madapter = new SearchAdapter();
        madapter.setListener(this);
        peopleRv.setHasFixedSize(true);
        mlayoutManager = new LinearLayoutManager(getContext());
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
    public void showData(List<Friend> data) {
        try {
            this.data = data;
            ivEmpty.setImageDrawable(Objects.requireNonNull(getContext()).getResources().getDrawable(R.drawable.ic_default_people));
            peopleRv.setVisibility(data.isEmpty() ? View.GONE : View.VISIBLE);
            llEmpty.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
            madapter.setData(getContext(), data);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void noData() {
        peopleRv.setVisibility(View.GONE);
        llEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void isFollowing(int pos, int b) {
        data.get(pos).setFollowStatus(b);
        madapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra("userId", data.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onFollow(String userId, boolean follow, int position) {
        if (follow)
            presenter.follow(position, userId);
        else
            presenter.unfollow(position, userId);
    }


    @Override
    public void reload() {

    }
}
