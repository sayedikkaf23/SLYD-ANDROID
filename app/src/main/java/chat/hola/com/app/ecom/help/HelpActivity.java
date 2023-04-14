package chat.hola.com.app.ecom.help;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import chat.hola.com.app.Utilities.Utilities;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ActivityHelpBinding;
import com.kotlintestgradle.model.ecom.help.HelpItemData;
import dagger.android.support.DaggerAppCompatActivity;
import java.util.ArrayList;
import javax.inject.Inject;

/**
 * this show frequently asked questions.
 */
public class HelpActivity extends DaggerAppCompatActivity {
  @Inject
  ViewModelProvider.Factory mViewModelFactory;
  private ActivityHelpBinding mActivityHelpBinding;
  private HelpViewModel mHelpViewModel;
  private HelpAdapter mHelpAdapter;
  private ArrayList<HelpItemData> mHelpItemData = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mActivityHelpBinding = DataBindingUtil.setContentView(this,
        R.layout.activity_help);
    mHelpViewModel = ViewModelProviders.of(this, mViewModelFactory).get(
        HelpViewModel.class);
    mActivityHelpBinding.setViewmodel(mHelpViewModel);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
    }
    ImageView imageView = mActivityHelpBinding.ibBack;
    imageView.setOnClickListener(view -> onBackPressed());
    subscribeHelpData();
    initialization();
  }

  /**
   * initialization for recyclerview and title
   */
  private void initialization() {
    mHelpAdapter = new HelpAdapter(mHelpItemData);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    mActivityHelpBinding.helpRv.setLayoutManager(layoutManager);
    mActivityHelpBinding.helpRv.setItemAnimator(new DefaultItemAnimator());
    mActivityHelpBinding.helpRv.setAdapter(mHelpAdapter);
    mHelpViewModel.callGetHelpApi();
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finish();
  }

  /**
   * subscribe to help data
   */
  private void subscribeHelpData() {
    mHelpViewModel.getHelpData().observe(this, helpItemData -> setHelpList(helpItemData));
  }

  /**
   * set the help list
   *
   * @param arrayList list contains the help data.
   */
  private void setHelpList(ArrayList<HelpItemData> arrayList) {
    mActivityHelpBinding.ivEmpty.setVisibility(
        (Utilities.isEmptyArray(arrayList) ? View.VISIBLE : View.GONE));
    mActivityHelpBinding.tvEmptyFaqMsg.setVisibility(
        Utilities.isEmptyArray(arrayList) ? View.VISIBLE : View.GONE);
    mHelpItemData.addAll(arrayList);
    mHelpAdapter.notifyDataSetChanged();
  }
}
