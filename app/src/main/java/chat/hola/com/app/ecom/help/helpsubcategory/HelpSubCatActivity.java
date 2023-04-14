package chat.hola.com.app.ecom.help.helpsubcategory;

import static chat.hola.com.app.Utilities.Constants.SUB_CAT_DATA;
import static chat.hola.com.app.Utilities.Constants.TITLE;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ActivityHelpBinding;
import com.kotlintestgradle.model.ecom.help.HelpSubCatListData;
import dagger.android.support.DaggerAppCompatActivity;
import java.util.ArrayList;

/**
 * Created by dell on 13-Apr-18.
 */
public class HelpSubCatActivity extends DaggerAppCompatActivity {
  private HelpSubAdapter madapter;
  private ArrayList<HelpSubCatListData> subCatLists = new ArrayList<>();
  private ActivityHelpBinding mActivityHelpBinding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mActivityHelpBinding = DataBindingUtil.setContentView(this,
        R.layout.activity_help);
    ImageButton imageView = mActivityHelpBinding.ibBack;
    imageView.setOnClickListener(view -> onBackPressed());
    initialization();
  }

  /**
   * initialization for recyclerview views and title
   */
  private void initialization() {
    mActivityHelpBinding.ivEmpty.setVisibility(View.GONE);
    TextView titleTv = mActivityHelpBinding.tvTitle;
    mActivityHelpBinding.pbHelp.setVisibility(View.GONE);
    madapter = new HelpSubAdapter(subCatLists);
    mActivityHelpBinding.helpRv.setHasFixedSize(false);
    RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getApplicationContext(),
        LinearLayoutManager.VERTICAL, false);
    mActivityHelpBinding.helpRv.setLayoutManager(mlayoutManager);
    mActivityHelpBinding.helpRv.setItemAnimator(new DefaultItemAnimator());
    mActivityHelpBinding.helpRv.setNestedScrollingEnabled(false);
    mActivityHelpBinding.helpRv.setAdapter(madapter);
    titleTv.setText(getIntent().getStringExtra(TITLE));
    if (getIntent() != null && getIntent().getExtras() != null) {
      ArrayList<HelpSubCatListData> lists =
          (ArrayList<HelpSubCatListData>) getIntent().getExtras().getSerializable(SUB_CAT_DATA);
      subCatLists.clear();
      subCatLists.addAll(lists);
      madapter.notifyDataSetChanged();
    }
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finish();
  }
}
