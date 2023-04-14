package chat.hola.com.app.orders.historyscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.appscrip.myapplication.injection.scope.ActivityScoped;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ActivityOrderHistoryBinding;
import com.kotlintestgradle.model.orderhistory.OrderHistStoreOrderData;
import com.kotlintestgradle.model.orderhistory.OrderHistoryItemData;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.ecom.review.ReviewProductActivity;
import chat.hola.com.app.historyproductdetail.HistoryProductDetailActivity;
import chat.hola.com.app.orderdetails.HistoryOrderDetailActivity;
import chat.hola.com.app.ui.RxTextView;
import dagger.android.support.DaggerAppCompatActivity;
import dagger.android.support.DaggerFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static chat.hola.com.app.Utilities.Constants.BUFFERING_TIME;
import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.HISTORY_PRODUCT_DETAIL;
import static chat.hola.com.app.Utilities.Constants.HISTROY_ORDER_DETAIL;
import static chat.hola.com.app.Utilities.Constants.HISTROY_PRODUCT_DETAIL;
import static chat.hola.com.app.Utilities.Constants.LIMIT;
import static chat.hola.com.app.Utilities.Constants.MINUS_ONE;
import static chat.hola.com.app.Utilities.Constants.PRICE;
import static chat.hola.com.app.Utilities.Constants.PRODUCT_COLOUR;
import static chat.hola.com.app.Utilities.Constants.PRODUCT_IMAGE;
import static chat.hola.com.app.Utilities.Constants.PRODUCT_NAME;
import static chat.hola.com.app.Utilities.Constants.RATING;
import static chat.hola.com.app.Utilities.Constants.REVIEW_TYPE;
import static chat.hola.com.app.Utilities.Constants.SELLER_IMAGE;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static chat.hola.com.app.Utilities.Utilities.isEmptyArray;
import static com.appscrip.myapplication.utility.Constants.COMING_FROM;
import static com.kotlintestgradle.data.utils.DataConstants.PHARMACY_FLOW;
import static com.kotlintestgradle.data.utils.DataConstants.PHARMACY_TYPE;
import static com.kotlintestgradle.data.utils.DataConstants.STORE_ORDER_TYPE;
import static com.kotlintestgradle.remote.RemoteConstants.DRIVER_ID;
import static com.kotlintestgradle.remote.RemoteConstants.DRIVER_TYPE;
import static com.kotlintestgradle.remote.RemoteConstants.ORDER_ID;
import static com.kotlintestgradle.remote.RemoteConstants.ORDER_TIME;
import static com.kotlintestgradle.remote.RemoteConstants.ORDER_TYPE;
import static com.kotlintestgradle.remote.RemoteConstants.PARENT_PRODUCT_ID;
import static com.kotlintestgradle.remote.RemoteConstants.PRODUCT_ID;
import static com.kotlintestgradle.remote.RemoteConstants.STORE_ID;

/**
 * A simple {@link Fragment} subclass.
 */
@ActivityScoped
public class HistoryActivity extends DaggerAppCompatActivity implements HistoryItemClick,
        HistoryStoreAdapter.HistoryStoreClickListener {
  private static final int REVIEW_REQUEST_CODE = 10;
  @Inject
  ViewModelProvider.Factory mViewModelFactory;

  private HistoryViewModel mHistoryViewModel;
  private ActivityOrderHistoryBinding mBinding;
  private ArrayList<OrderHistoryItemData> mOrderHistoryItemData = new ArrayList<>();
  private HistoryAdapter mHistoryAdapter;
  private int mPenCount, status;
  private String searchList = "";
  private String mOrderTime = "", mName = "";
  private int mPosition = MINUS_ONE;
  private int mMasterPos, mStorePos, mProductPos;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_order_history);
    initializeView();
    subscribeOrderHistoryData();
    subscribeOrderHistoryCount();
    subscribeFilterData();
    subscribeToCrossClick();
  }
  /**
   * this method is used to initialize the viewmodel.
   */
  private void initializeView() {
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_history);

    mHistoryViewModel = ViewModelProviders.of(this, mViewModelFactory).get(HistoryViewModel.class);
    mBinding.setViewModel(mHistoryViewModel);//used to connect mBinding to viewModel
    mHistoryViewModel.onHistoryUpdate(PHARMACY_TYPE);
    mHistoryAdapter = new HistoryAdapter(false,mOrderHistoryItemData, this,
            this, 8);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    mBinding.rvHistory.setLayoutManager(linearLayoutManager);
    mBinding.rvHistory.setNestedScrollingEnabled(true);
    mBinding.ivBackButton.setVisibility(View.VISIBLE);
    mBinding.ivBackButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });
    mBinding.rvHistory.setAdapter(mHistoryAdapter);
    mHistoryViewModel.callGetHistoryApi(ZERO, LIMIT, status, "", "",0);
/*    mBinding.rvHistory.addOnScrollListener(new MyScrollListener(linearLayoutManager) {
      @Override
      protected void loadMoreItems() {
        mHistoryViewModel.itemLoadingVisible.set(mOrderHistoryItemData.size() < mPenCount);
        if (mOrderHistoryItemData.size() < mPenCount) {
          mHistoryViewModel.callGetHistoryApi(
                  mOrderHistoryItemData.size(),
                  mOrderHistoryItemData.size() + LIMIT, status, searchList, mOrderTime,userInfoHandler.getCategory());
        }
      }

      @Override
      public boolean isLastPage() {
        return mHistoryViewModel.isLoading;
      }

      @Override
      public boolean isLoading() {
        return mHistoryViewModel.isLoading;
      }
    });*/
    mBinding.refreshSr.setOnRefreshListener(() -> {
      if (mBinding.refreshSr.isRefreshing()) {
        mBinding.refreshSr.setRefreshing(FALSE);
      }
      mHistoryViewModel.callGetHistoryApi(ZERO, LIMIT, status, searchList, "",0);
    });
    RxTextView.textChanges(mBinding.etHomeSearch)
            .debounce(BUFFERING_TIME, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new io.reactivex.Observer<CharSequence>() {
              @Override
              public void onSubscribe(Disposable d) {
              }

              @Override
              public void onNext(CharSequence charSequence) {
                searchList = charSequence.toString().toLowerCase();
                mBinding.ivCross.setVisibility(searchList.length() == ZERO ? View.GONE : View.VISIBLE);
                if (searchList.length() == ZERO) {
                  mOrderHistoryItemData.clear();
                  mOrderHistoryItemData.addAll(mHistoryViewModel.mOrderHistoryItemData);
                  mHistoryAdapter.notifyDataSetChanged();
                } else {
                  mHistoryViewModel.callGetHistoryApi(ZERO, LIMIT, status, searchList, "",0);
                }
              }

              @Override
              public void onError(Throwable e) {
              }

              @Override
              public void onComplete() {
              }
            });
  }

  /**
   * subscribe for history data
   */
  private void subscribeOrderHistoryData() {
    mHistoryViewModel.getOrderHistoryLiveData().observe(this,
            orderHistoryItemData -> {
              if (orderHistoryItemData != null) {
                mOrderHistoryItemData.clear();
                mOrderHistoryItemData.addAll(orderHistoryItemData);
                mBinding.vgNoHistoryFound.setVisibility(
                        mOrderHistoryItemData.size() > ZERO ? View.GONE : View.VISIBLE);
                mHistoryAdapter.notifyDataSetChanged();
              }
            });
  }

  /**
   * subscribe for pen count
   */
  private void subscribeOrderHistoryCount() {
    mHistoryViewModel.getOrderHistoryCount().observe(this, count -> {
      mPenCount = count;
    });
  }

  /**
   * subscribe for pen count
   */
  private void subscribeFilterData() {
/*    mHistoryViewModel.applyFilter().observe(getActivity(), aBoolean -> {
      Intent intent = new Intent(getActivity(), HistoryOrderFilterActivity.class);
      intent.putExtra(ORDER_NAME, mName);
      intent.putExtra(POSITION, mPosition);
      intent.putExtra(STATUS, status);
      startActivityForResult(intent, FILTER_REQ_CODE);
    });*/
  }

  /**
   * subscribe for cross click
   */
  private void subscribeToCrossClick() {
    mHistoryViewModel.searchData().observe(this, s -> {
      mBinding.etHomeSearch.setText(s);
      mOrderHistoryItemData.clear();
      mOrderHistoryItemData.addAll(mHistoryViewModel.mOrderHistoryItemData);
      mBinding.vgNoHistoryFound.setVisibility(
              mOrderHistoryItemData.size() > ZERO ? View.GONE : View.VISIBLE);
      mHistoryAdapter.notifyDataSetChanged();
    });
  }

  @Override
  public void onClick(int action, int masterPos, int storePos, int productPos, Bundle bundle) {
    mMasterPos = masterPos;
    mStorePos = storePos;
    mProductPos = productPos;
    switch (action) {
      case REVIEW_TYPE:
        Intent intent = new Intent(this, ReviewProductActivity.class);
        intent.putExtra(PRODUCT_IMAGE, bundle.getString(PRODUCT_IMAGE));
        intent.putExtra(PRODUCT_COLOUR, bundle.getString(PRODUCT_COLOUR));
        intent.putExtra(PRODUCT_NAME, bundle.getString(PRODUCT_NAME));
        intent.putExtra(PRICE, bundle.getString(PRICE));
        intent.putExtra(PARENT_PRODUCT_ID, bundle.getString(PARENT_PRODUCT_ID));
        intent.putExtra(PRODUCT_ID, bundle.getString(PRODUCT_ID));
        startActivityForResult(intent, REVIEW_REQUEST_CODE);
        break;
      case HISTROY_PRODUCT_DETAIL:
        Intent historyDetailIntent = new Intent(this, HistoryProductDetailActivity.class);
        historyDetailIntent.putExtras(bundle);
        startActivityForResult(historyDetailIntent, HISTORY_PRODUCT_DETAIL);
        break;
      case HISTROY_ORDER_DETAIL:
        Intent historyOrderDetailIntent = new Intent(this,HistoryOrderDetailActivity.class);
        bundle.putString(ORDER_TIME, Utilities.getDate(
                mOrderHistoryItemData.get(mMasterPos).getCreatedTimeStamp()));
       /* if (mMasterPos!=-1 && !isEmptyArray(mOrderHistoryItemData) && !isEmptyArray(mOrderHistoryItemData.get(mMasterPos).getStoreOrders())
                && mOrderHistoryItemData.size() > mMasterPos && mOrderHistoryItemData.get(mMasterPos).getStoreOrders().size() > storePos) {
          bundle.putString(STORE_ID, mOrderHistoryItemData.get(mMasterPos).getStoreOrders().get(storePos).getId());
        }*/
        historyOrderDetailIntent.putExtras(bundle);
        startActivity(historyOrderDetailIntent);
        break;
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      switch (requestCode) {
        case REVIEW_REQUEST_CODE:
          if (data != null) {
            double rating = data.getDoubleExtra(RATING, ZERO);
            if (rating > ZERO) {
              mOrderHistoryItemData.get(mMasterPos).getStoreOrders().get(
                      mStorePos).getProducts().get(
                      mProductPos).getRattingData().setRating((float) rating);
              mHistoryAdapter.notifyItemChanged(mMasterPos);
            }
          }
          break;
      /*  case FILTER_REQ_CODE:
          if (data != null) {
            mOrderTime = (String) data.getStringExtra(ORDER_TIME);
            mName = (String) data.getStringExtra(ORDER_NAME);
            status = data.getIntExtra(STATUS, ZERO);
            mPosition = data.getIntExtra(POSITION, MINUS_ONE);
            if (status == ZERO && mOrderTime.length() > ZERO
                    || status != ZERO && mOrderTime.length() == ZERO || mOrderTime.length() > ZERO) {
              Long tsLong = System.currentTimeMillis() / THOUSAND;
              String ts = tsLong.toString();
              mHistoryViewModel.callGetHistoryApi(ZERO, LIMIT, status, searchList,
                      mOrderTime + "-" + ts,0);
            } else {
              mOrderHistoryItemData.clear();
              mOrderHistoryItemData.addAll(mHistoryViewModel.mOrderHistoryItemData);
              mBinding.vgNoHistoryFound.setVisibility(
                      mOrderHistoryItemData.size() > ZERO ? View.GONE : View.VISIBLE);
              mHistoryAdapter.notifyDataSetChanged();
            }
          }
          break;*/
      }
    }
  }

  @Override
  public void onTrackOrderClicked(boolean isFood,int masterPosition, int position) {
  }

  @Override
  public void onReviewOrderClicked(boolean isFood,int masterPosition, int position) {
    OrderHistoryItemData data = mOrderHistoryItemData.get(masterPosition);
  }

  @Override
  public void onReOrderClicked(boolean isFood, int masterPosition, int position) {

  }

  @Override
  public void onRateOrderClicked(String driverId,boolean isFood, int masterPosition, int position,String orderId) {
    OrderHistoryItemData data = mOrderHistoryItemData.get(masterPosition);
    Intent intent = new Intent(this, ReviewProductActivity.class);
    intent.putExtra(COMING_FROM,ORDER_TYPE);
    intent.putExtra(SELLER_IMAGE,data.getStoreOrders().get(position).getStoreId());
    intent.putExtra(ORDER_ID,orderId);
    intent.putExtra(DRIVER_ID,driverId);
    intent.putExtra(DRIVER_TYPE,data.getStoreOrders().get(position).getStatus().getStatus());
    startActivity(intent);
  }

  @Override
  public void onOrderDetailsClicked(boolean isFood,int masterPosition, int position, boolean isReviewOrder) {
  }
}
