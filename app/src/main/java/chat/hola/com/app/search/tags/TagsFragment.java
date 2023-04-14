package chat.hola.com.app.search.tags;

import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.music.MusicActivity;
import chat.hola.com.app.search.tags.module.Tags;

import com.ezcall.android.R;

import dagger.android.support.DaggerFragment;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by ${3embed} on ${27-10-2017}.
 * Banglore
 */

public class TagsFragment extends DaggerFragment
        implements TagsContract.View, TagsAdapter.ClickListner {

    @Inject
    TagsPresenter presenter;
    @Inject
    BlockDialog dialog;

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
    private List<Tags> data;
    private TagsAdapter madapter;
    private RecyclerView.LayoutManager mlayoutManager;

    @Override
    public void userBlocked() {
        dialog.show();
    }

    public TagsFragment() {
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
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
                //                else if (searchInputEt.getText().toString().length() > 3)
                //                    presenter.search(searchInputEt.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        madapter = new TagsAdapter();
        madapter.setListener(this);
        peopleRv.setHasFixedSize(true);
        mlayoutManager = new LinearLayoutManager(getActivity());
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
        Intent intent = new Intent(getContext(), MusicActivity.class);
        intent.putExtra("hashtag", data.get(position).getHashTags());
        intent.putExtra("call", "hashtag");
        intent.putExtra("from", "node");
        intent.putExtra("totalPosts", data.get(position).getTotalPublicPosts() + "");
        startActivity(intent);
    }

    @Override
    public void showData(List<Tags> data) {
        this.data = data;

        if (getContext() != null) {
            ivEmpty.setImageDrawable(
                    ContextCompat.getDrawable(getContext(), R.drawable.ic_default_hashtag));

            ivEmpty.setImageDrawable(
                    getContext().getResources().getDrawable(R.drawable.ic_default_hashtag));
            peopleRv.setVisibility(data.isEmpty() ? View.GONE : View.VISIBLE);
            llEmpty.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
            madapter.setData(getContext(), data);
        } else {

            ivEmpty.setImageDrawable(
                    ContextCompat.getDrawable(AppController.getInstance(), R.drawable.ic_default_hashtag));

            ivEmpty.setImageDrawable(
                    AppController.getInstance().getResources().getDrawable(R.drawable.ic_default_hashtag));
            peopleRv.setVisibility(data.isEmpty() ? View.GONE : View.VISIBLE);
            llEmpty.setVisibility(data.isEmpty() ? View.VISIBLE : View.GONE);
            madapter.setData(AppController.getInstance(), data);
        }
    }

    @Override
    public void noData() {
        peopleRv.setVisibility(View.GONE);
        llEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void reload() {

    }
}
