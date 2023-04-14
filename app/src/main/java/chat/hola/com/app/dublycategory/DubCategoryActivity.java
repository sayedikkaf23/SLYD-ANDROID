package chat.hola.com.app.dublycategory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.ezcall.android.R;

import javax.inject.Inject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dubly.DubsActivity;
import chat.hola.com.app.dublycategory.modules.DubCategory;
import chat.hola.com.app.dublycategory.modules.DubCategoryAdapter;
import chat.hola.com.app.dublycategory.modules.SimpleFragmentPagerAdapter;
import chat.hola.com.app.live_stream.LiveStream.LiveStreamViewer.DotsIndicatorDecoration;
import chat.hola.com.app.manager.session.SessionManager;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>DubCategoryActivity</h1>
 * <p>All the Dubs appears on this screen.
 * User can add new Dubs also</p>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */

public class DubCategoryActivity extends DaggerAppCompatActivity implements DubCategoryContract.View {
    public static int page = 0;
    private Unbinder unbinder;

    @Inject
    TypefaceManager typefaceManager;
    @Inject
    SessionManager sessionManager;
    @Inject
    DubCategoryContract.Presenter presenter;
    @Inject
    DubCategoryAdapter categoryAdapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rvCategories)
    RecyclerView rvCategories;
    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.sliding_tabs)
    TabLayout sliding_tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @Inject
    BlockDialog dialog;

    @Override
    public void userBlocked() {
        dialog.show();
    }

    //    private LinearLayoutManager layoutManager;
//    private GridLayoutManager gridLayoutManager;
//    public String call;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dub_activity);
        unbinder = ButterKnife.bind(this);
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
//        layoutManager = new LinearLayoutManager(this);

        // tvTbTitle.setTypeface(typefaceManager.getSemiboldFont());
//        call = getIntent().getStringExtra("call");
        rvCategories.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        categoryAdapter.setListener(presenter.getCategoryPresenter());
        rvCategories.setAdapter(categoryAdapter);

        rvCategories.setNestedScrollingEnabled(false);
        rvCategories.setHasFixedSize(true);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rvCategories);

        presenter.getCategories(false);

        toolbarSetup();

        // Create an adapter that knows which fragment should be shown on each page
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        sliding_tabs.setupWithViewPager(viewpager);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                adapter.setPosition(position);
                viewpager.getParent().requestDisallowInterceptTouchEvent(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewpager.setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });
    }


    private void toolbarSetup() {
        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setDisplayShowHomeEnabled(true);
//        }
        tvTitle.setTypeface(typefaceManager.getSemiboldFont());
    }

    @OnClick(R.id.ivBack)
    public void backpress() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        setResult(RESULT_CANCELED, new Intent());
        supportFinishAfterTransition();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void showMessage(String msg, int msgId) {

    }

    @Override
    public void sessionExpired() {
        if (sessionManager != null)
            sessionManager.sessionExpired(this);
    }

    @Override
    public void isInternetAvailable(boolean flag) {

    }

    @Override
    public void reload() {
    }


    @Override
    public void getList(String categoryId, String name) {
        startActivityForResult(new Intent(this, DubsActivity.class).putExtra("categoryId", categoryId).putExtra("categoryName", name), 0);
    }

    @Override
    public void categories(List<DubCategory> dubs) {
        if (dubs.size() > 8)
            rvCategories.addItemDecoration(new DotsIndicatorDecoration(10, 7, 20, getResources().getColor(R.color.color_black), getResources().getColor(R.color.base_color)));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {

            if (resultCode == Activity.RESULT_OK) {

                returnSelectedDubSound(data.getStringExtra("musicId"), data.getStringExtra("audio"), data.getStringExtra("name"));

            }
        }


    }

    public void returnSelectedDubSound(String musicId, String audio, String name) {

        Intent intent = new Intent();

        intent.putExtra("musicId", musicId);
        intent.putExtra("audio", audio);
        intent.putExtra("name", name);


        setResult(RESULT_OK, intent);
        supportFinishAfterTransition();
    }


}
