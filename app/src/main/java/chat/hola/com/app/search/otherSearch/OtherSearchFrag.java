package chat.hola.com.app.search.otherSearch;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ezcall.android.R;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.search.SearchAdapter;
import dagger.android.support.DaggerFragment;


/**
 * Created by ${3embed} on ${27-10-2017}.
 * Banglore
 */

public class OtherSearchFrag extends DaggerFragment implements OtherSearchContract.View{

    @Inject
    OtherSearchPresenter presenter;

    @BindView(R.id.trendingRv)
    RecyclerView trendingRv;
    private Unbinder unbinder;
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

    public static OtherSearchFrag newInstance() {
        return new OtherSearchFrag();
    }

    public OtherSearchFrag() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.frag_trending, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView.Adapter mAdapter = new SearchAdapter();
        trendingRv.setHasFixedSize(true);
        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        trendingRv.setLayoutManager(mlayoutManager);
        trendingRv.setItemAnimator(new DefaultItemAnimator());
        trendingRv.setAdapter(mAdapter);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        if(unbinder != null)
            unbinder.unbind();
    }

    @Override
    public void showMessage(String msg,int msgId) {
        if(msg != null && !msg.isEmpty()) {
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        }
        else if(msgId != 0){
            Toast.makeText(getContext(),getResources().getString(msgId), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void reload() {

    }
}
