package chat.hola.com.app.category;

import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.category.model.CategoryAdapter;
import chat.hola.com.app.manager.session.SessionManager;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>CategoryActivity</h1>
 * <p>It shows list of categories</p>
 *
 * @author 3Embed
 * @version 1.0
 * @since 28/3/18
 */

public class CategoryActivity extends DaggerAppCompatActivity implements CategoryContract.View, SwipeRefreshLayout.OnRefreshListener {

    private Unbinder unbinder;
    String categoryId;

    @Inject
    CategoryPresenter presenter;
    @Inject
    TypefaceManager typefaceManager;
    @Inject
    CategoryAdapter categoryAdapter;
    @Inject
    SessionManager sessionManager;

    @BindView(R.id.tvTbTitle)
    TextView tvTbTitle;
    @BindView(R.id.btnSelect)
    Button btnSelect;
    @BindView(R.id.recyclerCategory)
    RecyclerView recyclerCategory;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.srSwipe)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.back)
    ImageView back;
//    @BindView(R.id.llNetworkError)
//    InternetErrorView llNetworkError;
    @Inject
    BlockDialog dialog;

    @Override
    public void userBlocked() {
        dialog.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        unbinder = ButterKnife.bind(this);
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        //toolbarSetup();
        categoryId = getIntent().getStringExtra("categoryId");
        categoryAdapter.setListener(presenter);
        presenter.getCategory(categoryId);
        presenter.init();
//        llNetworkError.setErrorListner(this);
    }

    /**
     * Setup actionbar
     */
    private void toolbarSetup() {
        setSupportActionBar(toolbar);
    }

    @OnClick(R.id.back)
    public void back(){
        onBackPressed();
    }


    /**
     * call when session expires.
     */
    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(getApplicationContext());
    }

    @Override
    public void isInternetAvailable(boolean flag) {
//        llNetworkError.setVisibility(flag ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    /**
     * shows toast message.
     */
    @Override
    public void showMessage(String msg, int msgId) {
    }

    /**
     * applies fonts.
     */
    @Override
    public void applyFont() {
        tvTbTitle.setTypeface(typefaceManager.getSemiboldFont());
        btnSelect.setTypeface(typefaceManager.getMediumFont());
    }

    /**
     * list view setup.
     */
    @Override
    public void recyclerSetup() {
        recyclerCategory.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerCategory.setHasFixedSize(true);
        recyclerCategory.setAdapter(categoryAdapter);
    }

    @OnClick(R.id.btnSelect)
    public void btnSelect() {
//        presenter.getSelectedCategory()
        if (presenter.getSelectedCategory() != null) {
            setResult(RESULT_OK, presenter.getSelectedCategory());
            finish();
        } else {
            Toast.makeText(this, R.string.select_category, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * it reloads the data
     */
    @Override
    public void onRefresh() {
        presenter.getCategory(categoryId);
    }

    /**
     * checks whether data is loading or not
     */
    public void isLoadingData(boolean show) {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(show);
    }

    @Override
    public void reload() {
        onRefresh();
    }
}
