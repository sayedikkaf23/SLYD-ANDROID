package chat.hola.com.app.profileScreen.business.post;

import android.os.Bundle;

import com.ezcall.android.R;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.profileScreen.business.post.actionbutton.ActionButtonFragment;
import chat.hola.com.app.profileScreen.business.post.link.LinkFragment;
import chat.hola.com.app.profileScreen.business.post.price.PriceFragment;
import chat.hola.com.app.profileScreen.business.post.type.PostTypeFragment;
import dagger.android.support.DaggerAppCompatActivity;


/**
 * <h1>BusinessPostActivity</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 05 September 2019
 */
public class BusinessPostActivity extends DaggerAppCompatActivity {

    @Inject
    TypefaceManager typefaceManager;
    @Inject
    ActionButtonFragment actionButtonFragment;
    @Inject
    PriceFragment priceFragment;
    @Inject
    LinkFragment linkFragment;
    @Inject
    PostTypeFragment postTypeFragment;

    private FragmentTransaction ft;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_post);
        ButterKnife.bind(this);
        int VIEW_ID = getIntent().getIntExtra("VIEW_ID", 1111);
        ft = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        switch (VIEW_ID) {
            case 4444:
                String postType = getIntent().getStringExtra("postType");
                bundle.putString("postType", postType);
                postTypeFragment.setArguments(bundle);
                pushFragment(postTypeFragment);
                break;
            case 1111:
                String businessButtonText = getIntent().getStringExtra("businessButtonText");
                bundle.putString("businessButtonText", businessButtonText);
                actionButtonFragment.setArguments(bundle);
                pushFragment(actionButtonFragment);
                break;
            case 2222:
                String businessPrice = getIntent().getStringExtra("businessPrice");
                String businessCurrency = getIntent().getStringExtra("businessCurrency");
                bundle.putString("businessPrice", businessPrice);
                bundle.putString("businessCurrency", businessCurrency);
                priceFragment.setArguments(bundle);
                pushFragment(priceFragment);
                break;
            case 3333:
                String businessUrl = getIntent().getStringExtra("businessUrl");
                bundle.putString("businessUrl", businessUrl);
                linkFragment.setArguments(bundle);
                pushFragment(linkFragment);
                break;
        }
    }


    private void pushFragment(Fragment fragment) {
        try{
        ft.replace(R.id.fragment, fragment);
        ft.commit();}catch(IllegalStateException e){e.printStackTrace();}
    }
}
