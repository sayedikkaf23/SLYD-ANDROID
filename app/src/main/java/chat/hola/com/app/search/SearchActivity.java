package chat.hola.com.app.search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.ezcall.android.R;

import javax.inject.Inject;

import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.home.connect.ConnectPagerAdapter;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.search.channel.ChannelFragment;
import chat.hola.com.app.search.locations.LocationsFragment;
import chat.hola.com.app.search.people.PeopleFragment;
import chat.hola.com.app.search.tags.TagsFragment;
import dagger.android.support.DaggerAppCompatActivity;

public class SearchActivity extends DaggerAppCompatActivity implements SearchContract.View {
    @Inject
    SessionManager sessionManager;
    @Inject
    BlockDialog dialog;
    @Inject
    SearchPresenter presenter;
    @Inject
    TypefaceManager typefaceManager;

    @BindView(R.id.searchTl)
    TabLayout searchTl;
    @BindView(R.id.searchVp)
    ViewPager searchVp;
    @BindView(R.id.searchIv)
    ImageView searchIv;
    @BindView(R.id.searchInputEt)
    EditText searchInputEt;
    @BindView(R.id.ivClearSearch)
    ImageView ivClearSearch;

    private Unbinder unbinder;

    @Override
    public void userBlocked() {
        dialog.show();
    }

    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(getApplicationContext());
    }

    @Override
    public void isInternetAvailable(boolean flag) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        unbinder = ButterKnife.bind(this);
        mInitialization();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, LandingActivity.class));//.putExtra("menuIndex", 4));
        finish();
    }

    private void mInitialization() {

        ConnectPagerAdapter adapter = new ConnectPagerAdapter(getSupportFragmentManager());
        String people = getResources().getString(R.string.people);
        String tags = getResources().getString(R.string.tags);
       // String channel = getResources().getString(R.string.channel);
        String locations = getResources().getString(R.string.locations);

        try {
            adapter.addFragment(new PeopleFragment(), people);
            adapter.addFragment(new TagsFragment(), tags);
            //adapter.addFragment(new ChannelFragment(), channel);
            adapter.addFragment(new LocationsFragment(), locations);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        searchVp.setAdapter(adapter);
        searchTl.setupWithViewPager(searchVp);
        searchVp.setOffscreenPageLimit(0);

        TextView t1 = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_textview, null);
        t1.setTypeface(typefaceManager.getSemiboldFont());
        t1.setText(people);
        searchTl.getTabAt(0).setCustomView(t1);

        TextView t2 = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_textview, null);
        t2.setTypeface(typefaceManager.getSemiboldFont());
        t2.setText(tags);
        searchTl.getTabAt(1).setCustomView(t2);

//        TextView t3 = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_textview, null);
//        t3.setTypeface(typefaceManager.getSemiboldFont());
//        t3.setText(channel);
//        searchTl.getTabAt(2).setCustomView(t3);

        TextView t4 = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_textview, null);
        t4.setTypeface(typefaceManager.getSemiboldFont());
        t4.setText(locations);
        searchTl.getTabAt(2).setCustomView(t4);

        searchInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ivClearSearch.setVisibility(charSequence.length() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @OnClick(R.id.ivClearSearch)
    public void clearSearch() {
        searchInputEt.setText("");
        ivClearSearch.setVisibility(View.GONE);
    }

    @OnClick(R.id.searchIv)
    public void search() {
        presenter.stop();
    }

    @Override
    public void stop() {
        finish();
    }


    @Override
    public void showMessage(String msg, int msgId) {
        if (msg != null && !msg.isEmpty()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else if (msgId != 0) {
            Toast.makeText(this, getResources().getString(msgId), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void reload() {

    }
}
