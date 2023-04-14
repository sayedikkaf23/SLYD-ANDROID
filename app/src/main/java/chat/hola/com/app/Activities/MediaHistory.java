package chat.hola.com.app.Activities;

/*
 * Created by moda on 14/04/16.
 */


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.ezcall.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Fragments.MediaHistory_Received;
import chat.hola.com.app.Fragments.MediaHistory_Sent;
import chat.hola.com.app.Utilities.ZoomOutPageTransformer;


/*
 *
 * Activity containing the fragmnents for the media sent and received respectively
 * */
public class MediaHistory extends AppCompatActivity

{
    /*
     * Activity to show history of media shared along with option of switching between list and grid view
     */


    public static String docId;
    private Bus bus = AppController.getBus();

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @SuppressWarnings("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        supportRequestWindowFeature(AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.media_history);
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        RelativeLayout toolbar = (RelativeLayout) findViewById(R.id.toolbar);


        toolbar.setVisibility(View.VISIBLE);

        View v2 = findViewById(R.id.seperator2);
        v2.setVisibility(View.GONE);

        TextView title = (TextView) findViewById(R.id.title);


        title.setText(getString(R.string.string_279));

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        setupActivity(getIntent());

        ImageView close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                onBackPressed();

            }
        });

        bus.register(this);
    }


    public String getDocId() {
        return docId;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


        adapter.addFrag(new MediaHistory_Received(), getString(R.string.string_806));
        adapter.addFrag(new MediaHistory_Sent(), getString(R.string.string_805));


        viewPager.setAdapter(adapter);


        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public void onBackPressed() {


        if (AppController.getInstance().isActiveOnACall()) {
            if (AppController.getInstance().isCallMinimized()) {
                super.onBackPressed();
                supportFinishAfterTransition();
            }
        } else {
            super.onBackPressed();
            supportFinishAfterTransition();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


        Glide.get(this).clearMemory();
        Glide.get(this).getBitmapPool().clearMemory();

        bus.unregister(this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setupActivity(intent);
    }


    private void setupActivity(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {


            docId = extras.getString("docId");
        }

        setupViewPager(viewPager);


        if (tabLayout != null)
            tabLayout.setupWithViewPager(viewPager);
    }

    private void minimizeCallScreen(JSONObject obj) {
        try {
            Intent intent = new Intent(MediaHistory.this, ChatMessageScreen.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("receiverUid", obj.getString("receiverUid"));
            intent.putExtra("receiverName", obj.getString("receiverName"));
            intent.putExtra("documentId", obj.getString("documentId"));
            intent.putExtra("isStar", obj.getBoolean("isStar"));
            intent.putExtra("receiverImage", obj.getString("receiverImage"));
            intent.putExtra("colorCode", obj.getString("colorCode"));

            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void getMessage(JSONObject object) {
        try {
            if (object.getString("eventName").equals("callMinimized")) {

                minimizeCallScreen(object);
            }

        } catch (
                JSONException e)

        {
            e.printStackTrace();
        }

    }

}