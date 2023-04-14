package chat.hola.com.app.profileScreen.business.category;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.R;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.profileScreen.model.BusinessCategory;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>BusinessCategoryActivity</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 16 August 2019
 */
public class BusinessCategoryActivity extends DaggerAppCompatActivity implements BusinessCategoryContact.View {

    @Inject
    SessionManager sessionManager;
    @Inject
    BusinessCategoryAdapter adapter;
    @Inject
    TypefaceManager typefaceManager;
    @Inject
    BusinessCategoryContact.Presenter presenter;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.srRefresh)
    SwipeRefreshLayout srRefresh;
    @BindView(R.id.rvCategories)
    RecyclerView rvCategories;

    public static String category;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_category);
        ButterKnife.bind(this);

        category = getIntent().getStringExtra("category");

        rvCategories.setLayoutManager(new LinearLayoutManager(this));
        rvCategories.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        rvCategories.setAdapter(adapter);
        adapter.setClickListner(presenter.getPresenter());

        reload();

        // refreshes the data
        srRefresh.setOnRefreshListener(this::reload);

        //set fonts
        tvTitle.setTypeface(typefaceManager.getSemiboldFont());
    }

    @OnClick(R.id.ivBack)
    public void back() {
        super.onBackPressed();
    }

    @Override
    public void showMessage(String msg, int msgId) {
        Toast.makeText(this, msg != null ? msg : getString(msgId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(this);
    }

    @Override
    public void isInternetAvailable(boolean flag) {
        if (flag)
            showMessage(null, R.string.no_internet);
    }

    @Override
    public void userBlocked() {
        sessionManager.logOut(this);
    }

    /**
     * loads the business categories data
     */
    @Override
    public void reload() {
        presenter.businessCategories();
    }

    @Override
    public void success() {
        if (srRefresh.isRefreshing())
            srRefresh.setRefreshing(false);
    }

    @Override
    public void failed() {
        if (srRefresh.isRefreshing())
            srRefresh.setRefreshing(false);
    }

    @Override
    public void selectCategory(BusinessCategory.Data category) {
        Intent intent = new Intent();
        intent.putExtra("category", category.getName());
        intent.putExtra("category_id", category.getId());
        setResult(RESULT_OK, intent);
        finish();
    }
}
