package chat.hola.com.app.orderdetails;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;


import com.ezcall.android.R;
import com.ezcall.android.databinding.ActivityHistoryOrderDetailBinding;
import com.kotlintestgradle.model.ecom.getcart.CartTaxData;
import com.kotlintestgradle.model.orderdetails.OrderData;
import com.kotlintestgradle.model.orderdetails.pharmacy.PackingDetailItem;
import com.kotlintestgradle.model.orderhistory.OrderHistProductData;
import com.kotlintestgradle.model.orderhistory.OrderHistoryAccountingData;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.inject.Inject;

import chat.hola.com.app.ecom.pdp.ProductDetailsActivity;
import chat.hola.com.app.historyproductdetail.HistoryProductDetailActivity;
import chat.hola.com.app.historyproductdetail.OrderDetailClick;
import chat.hola.com.app.orderdetails.orderboxes.CancelledItemsAdapter;
import chat.hola.com.app.orderdetails.orderboxes.PendingShipmentAdapter;
import chat.hola.com.app.orderdetails.orderboxes.SplitBoxesAdapter;
import dagger.android.support.DaggerAppCompatActivity;

import static chat.hola.com.app.Utilities.Constants.BOX_COUNT;
import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.FIVE;
import static chat.hola.com.app.Utilities.Constants.FOUR;
import static chat.hola.com.app.Utilities.Constants.HISTORY_PRODUCT_DETAIL;
import static chat.hola.com.app.Utilities.Constants.IS_SPIT_PRODUCT;
import static chat.hola.com.app.Utilities.Constants.ORDER_CANCELLED;
import static chat.hola.com.app.Utilities.Constants.ORDER_PENDING_CONFIRMATION;
import static chat.hola.com.app.Utilities.Constants.SIX;
import static chat.hola.com.app.Utilities.Constants.STORE_NAME;
import static chat.hola.com.app.Utilities.Constants.THREE;
import static chat.hola.com.app.Utilities.Constants.TRUE;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static com.appscrip.myapplication.utility.Constants.ONE;
import static com.appscrip.myapplication.utility.Constants.TWO;
import static com.kotlintestgradle.remote.RemoteConstants.IS_PACKAGE;
import static com.kotlintestgradle.remote.RemoteConstants.ORDER_ID;
import static com.kotlintestgradle.remote.RemoteConstants.ORDER_TIME;
import static com.kotlintestgradle.remote.RemoteConstants.PACK_ID;
import static com.kotlintestgradle.remote.RemoteConstants.PARENT_PRODUCT_ID;
import static com.kotlintestgradle.remote.RemoteConstants.PRODUCT_ID;
import static com.kotlintestgradle.remote.RemoteConstants.PRODUCT_ORDER_ID;


/**
 * this activity will represent the history order detail.
 */
public class HistoryOrderDetailActivity extends DaggerAppCompatActivity implements
        OrderDetailClick, SelectItem , PackageAdapter.OnPackageTrackingClickListener {
  @Inject
  ViewModelProvider.Factory mViewModelFactory;
  private ActivityHistoryOrderDetailBinding mBinding;
  private HistoryOrderDetailViewModel mHistoryOrderDetailViewModel;
  private ArrayList<OrderHistProductData> mOrderHistProductData = new ArrayList<>();
  private HistoryOrderDetailAdapter mHistoryOrderDetailAdapter;
  private PackageAdapter packageAdapter;
  private OrderHistoryAccountingData mOrderHistoryAccountingData;
  private ArrayList<OrderHistProductData> mOrderHistBoxesData = new ArrayList<>();
  private ArrayList<OrderHistProductData> mPendingShipmentData = new ArrayList<>();
  private ArrayList<PackingDetailItem> packingdetails = new ArrayList<>();
  private ArrayList<OrderHistProductData> mCancelledProductsData = new ArrayList<>();
  private LinkedHashMap<String, ArrayList<OrderHistProductData>> mHashMap =
      new LinkedHashMap<>();
  private CancelledItemsAdapter mCancelledItemsAdapter;
  private PendingShipmentAdapter mPendingShipmentAdapter;
  private String mStoreName;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initializeView();
    subscribeOnClick();
    subScribeProductData();
    subScribeProductBoxesData();
    subScribePackingData();
    subScribePriceDetails();
    subscribePaymentMethod();
    subscribeTipValue();
  }

  private void subScribePackingData() {
    mHistoryOrderDetailViewModel.onpackingdetails().observe(this,
            orderData -> {
              if (orderData != null) {
                for (PackingDetailItem orders : orderData) {
                  if (orders != null) {
                    packingdetails.add(orders);
                  }
                }
                //EcomUtil.printLog("exe" + "mOrderHistProductData" + mOrderHistProductData.size());
                packageAdapter.notifyDataSetChanged();
                mBinding.rvOrders.setVisibility(View.GONE);
                mBinding.rvOrdersPacking.setVisibility(View.VISIBLE);
              }else {
                mBinding.rvOrders.setVisibility(View.VISIBLE);
                mBinding.rvOrdersPacking.setVisibility(View.GONE);
              }
            });
  }

  /**
   * subscribe to tip data
   */
  private void subscribeTipValue() {
    mHistoryOrderDetailViewModel.tipValue.observe(this, tip -> {
      mBinding.accoutingDetails.tvCartTip.setVisibility(View.VISIBLE);
      mBinding.accoutingDetails.tvCartTipAmt.setVisibility(View.VISIBLE);
      float tipAmount;
      try {
        tipAmount = Float.parseFloat(tip);
      } catch (NumberFormatException e) {
        tipAmount = 0f;
      }
      mBinding.accoutingDetails.tvCartTipAmt.setText(String.format("%s %.2f",
              mHistoryOrderDetailViewModel.currency, tipAmount));
    });
  }


  /**
   * Initialising the View using Data Binding
   */
  private void initializeView() {
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_history_order_detail);
    mHistoryOrderDetailViewModel = ViewModelProviders.of(this, mViewModelFactory).get(
        HistoryOrderDetailViewModel.class);
    mBinding.setViewModel(mHistoryOrderDetailViewModel);
    mBinding.incHeader.tvCenterCategoryName.setText(
        getResources().getString(R.string.allOrderDetails));
    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      mBinding.tvOrderNo.setText(
          String.format("%s %s", getResources().getString(R.string.historyDetailOrderNo),
              extras.getString(ORDER_ID)));
      mBinding.tvOrderTime.setText(extras.getString(ORDER_TIME));
      mHistoryOrderDetailViewModel.mOrderId = extras.getString(ORDER_ID);
      boolean splitOrder = extras.getBoolean(IS_SPIT_PRODUCT, FALSE);
      if (splitOrder) {
        mBinding.rvOrders.setVisibility(View.GONE);
        mBinding.clBoxItem.setVisibility(View.VISIBLE);
        mStoreName = extras.getString(STORE_NAME);
        mCancelledItemsAdapter = new CancelledItemsAdapter(mStoreName, mCancelledProductsData,
            this::orderItemClick);
        mBinding.rvCancelled.setAdapter(mCancelledItemsAdapter);
        mPendingShipmentAdapter = new PendingShipmentAdapter(mStoreName,
            mPendingShipmentData);
        mBinding.rvPendingShipmentOrders.setAdapter(mPendingShipmentAdapter);
      }
      mHistoryOrderDetailViewModel.callGetHistoryApi(extras.getString(ORDER_ID), splitOrder);
    }
    mHistoryOrderDetailAdapter = new HistoryOrderDetailAdapter(mOrderHistProductData,
        this::orderItemClick);
    packageAdapter = new PackageAdapter(packingdetails,
        this::orderItemClick,this::onTrackClicked);
    mBinding.rvOrders.setAdapter(mHistoryOrderDetailAdapter);
    mBinding.rvOrdersPacking.setAdapter(packageAdapter);
  }

  /**
   * subscribe to product data
   */
  private void subScribeProductData() {
    mHistoryOrderDetailViewModel.onHistoryProductsData().observe(this,
        orderData -> {
          if (orderData != null) {
            for (OrderData orders : orderData) {
              if (orders != null) {
                for (OrderHistProductData orderHistProductData : orders.getProducts()) {
                  orderHistProductData.setSellerName(orders.getStoreName());
                }
                mOrderHistProductData.addAll(orders.getProducts());
              }
            }
            mHistoryOrderDetailAdapter.notifyDataSetChanged();
          }
        });
  }

  /**
   * subscribe to product boxes  data
   */
  private void subScribeProductBoxesData() {
    mHistoryOrderDetailViewModel.onHistoryBoxData().observe(this,
        orderHistProductData -> {
          for (int i = ZERO; i < orderHistProductData.size(); i++) {
            int status;
            if (orderHistProductData.get(i).getStatus() != null) {
              status = Integer.parseInt(orderHistProductData.get(i).getStatus().getStatus());
              if (status == ORDER_CANCELLED) {
                mCancelledProductsData.add(orderHistProductData.get(i));
              } else if (status == ORDER_PENDING_CONFIRMATION) {
                mPendingShipmentData.add(orderHistProductData.get(i));
              } else {
                String packageId = orderHistProductData.get(i).getPackageId();
                Log.d("exe", "packageId" + packageId);
                if (mHashMap.containsKey(packageId)) {
                  ArrayList<OrderHistProductData> value = mHashMap.get(packageId);
                  if (value != null) {
                    value.add(orderHistProductData.get(i));
                  }
                } else {
                  ArrayList<OrderHistProductData> orderHistProductDataList = new ArrayList<>();
                  orderHistProductDataList.add(orderHistProductData.get(i));
                  mHashMap.put(packageId, orderHistProductDataList);
                }
              }
            }
          }
          mBinding.rvBoxOrders.setVisibility((mHashMap.size() > ZERO ? View.VISIBLE : View.GONE));
          if (mHashMap.size() > ZERO) {
            SplitBoxesAdapter splitBoxesAdapter = new SplitBoxesAdapter(mStoreName, mHashMap,
                this::orderItemClick, this::onSelectItem);
            mBinding.rvBoxOrders.setAdapter(splitBoxesAdapter);
          }
          mBinding.vgCancelled.setVisibility(
              mCancelledProductsData.size() > ZERO ? View.VISIBLE : View.GONE);
          mBinding.vgPendingShipment.setVisibility(
              mPendingShipmentData.size() > ZERO ? View.VISIBLE : View.GONE);
          if (mCancelledProductsData.size() > ZERO) {
            mCancelledItemsAdapter.notifyDataSetChanged();
          }
          if (mPendingShipmentData.size() > ZERO) {
            mPendingShipmentAdapter.notifyDataSetChanged();
          }
        });
  }

  /**
   * subscribe price details
   */
  private void subScribePriceDetails() {
    mHistoryOrderDetailViewModel.getPriceDet().observe(this,
        orderHistoryAccountingData -> {
          if (orderHistoryAccountingData != null) {
            mOrderHistoryAccountingData = orderHistoryAccountingData;
            mBinding.accoutingDetails.tvCartSubTotalAmt.setText(
                String.format("%s%s", orderHistoryAccountingData.getCurrencySymbol(),
                    orderHistoryAccountingData.getTaxableAmount()));
            if (orderHistoryAccountingData.getDeliveryFee() != null) {
              mBinding.accoutingDetails.tvCartShippingAmt.setText(
                  (orderHistoryAccountingData.getDeliveryFee().equals(String.valueOf(ZERO)))
                      ? getResources().getString(R.string.free) :
                      String.format("%s%s", orderHistoryAccountingData.getCurrencySymbol(),
                          orderHistoryAccountingData.getDeliveryFee()));
            }
            if (orderHistoryAccountingData.getFinalTotal() != null
                && !orderHistoryAccountingData.getFinalTotal().isEmpty()) {
              mBinding.accoutingDetails.tvCartOrderTotalAmt.setText(
                  String.format("%s%s", orderHistoryAccountingData.getCurrencySymbol(),
                      String.format("%.2f",
                          Float.parseFloat(orderHistoryAccountingData.getFinalTotal()))));
            }
            if (orderHistoryAccountingData.getOfferDiscount() != null
                && !orderHistoryAccountingData.getOfferDiscount().isEmpty()) {
              mBinding.accoutingDetails.tvCartTotalSavingsAmt.setText(
                  String.format("%s%s", orderHistoryAccountingData.getCurrencySymbol(),
                      String.format("%.2f",
                          Float.parseFloat(orderHistoryAccountingData.getOfferDiscount()))));
            }
            setTaxData(mOrderHistoryAccountingData.getTax());
          }
        });
  }

  /**
   * subscribe to payment method.
   */
  private void subscribePaymentMethod() {
    mHistoryOrderDetailViewModel.getPaymentMethod().observe(this,
        integer -> setPaymentMethod(integer));
  }

  /**
   * subscribe for the back button on click
   */
  private void subscribeOnClick() {
    mHistoryOrderDetailViewModel.onClick().observe(this,
        historyOrderDetailUiAction -> {
          switch (historyOrderDetailUiAction) {
            case TAX_INFO:
              break;
            case BACK:
              finish();
              break;
            case DELIVERY_PHONE_NUM:
              String phone = mBinding.tvDetailDeliveryAddphoneNum.getText().toString();
              if (isValidPhone(phone)) {
                launchDialPad(phone.substring(SIX).trim());
              }
              break;
            case BILLING_PHONE_NUM:
              String phoneNumber = mBinding.tvDetailBillingAddphoneNum.getText().toString();
              if (isValidPhone(phoneNumber)) {
                launchDialPad(phoneNumber.substring(SIX).trim());
              }
              break;
          }
        });
  }

  public  boolean isValidPhone(String phone) {
    return phone.trim().length() >= 7;
  }

  /**
   * launch dial pad
   * @param phoneNumber String
   */
  private void launchDialPad(String phoneNumber) {
    Intent intent = new Intent(Intent.ACTION_DIAL);
    intent.setData(Uri.parse(String.format("tel:%s", phoneNumber)));
    startActivity(intent);
  }

  /**
   * set the tax data
   *
   * @param taxData cart data object from server.
   */
  private void setTaxData(ArrayList<CartTaxData> taxData) {
    LinearLayout linearLayout = mBinding.accoutingDetails.cartAddTaxLl;
    linearLayout.removeAllViews();
    LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(
        Context.LAYOUT_INFLATER_SERVICE);
    View viewInflater;
    if (taxData != null && taxData.size() > ZERO) {
      for (int i = ZERO; i < taxData.size(); i++) {
        viewInflater = layoutInflater.inflate(R.layout.tax_row, null);
        TextView taxTxtTv = viewInflater.findViewById(R.id.taxTxtTv);
        TextView taxValueTv = viewInflater.findViewById(R.id.taxValueTv);
        taxTxtTv.setText(taxData.get(i).getTaxName());
        taxValueTv.setText(
            String.format("%s%s", mOrderHistoryAccountingData.getCurrencySymbol(),
                taxData.get(i).getTotalValue()));
        linearLayout.addView(viewInflater);
      }
    }
  }

  /**
   * set payment method
   */
  private void setPaymentMethod(int payment) {
    mBinding.accoutingDetails.tvPayMentMethodTxt.setVisibility(View.VISIBLE);
    mBinding.accoutingDetails.ivCartDashViewPmBelow.setVisibility(View.VISIBLE);
    mBinding.accoutingDetails.vgTotal.setVisibility(View.VISIBLE);
    mBinding.accoutingDetails.tvPaymentOrderTotalAmt.setText(
        String.format("%s %s", mOrderHistoryAccountingData.getCurrencySymbol(),
            String.format("%.2f", Float.parseFloat(mOrderHistoryAccountingData.getFinalTotal()))));
    switch (payment) {
      case ONE:
        mBinding.accoutingDetails.vgWallet.setVisibility(View.VISIBLE);
        mBinding.accoutingDetails.vgCard.setVisibility(View.VISIBLE);
        mBinding.accoutingDetails.tvCashOnDelivery.setVisibility(View.GONE);
        mBinding.accoutingDetails.tvWalletAmt.setText(
            String.format("%s %s", mOrderHistoryAccountingData.getCurrencySymbol(),
                String.format("%.2f",
                    Float.parseFloat(mOrderHistoryAccountingData.getPayBy().getWallet()))));
        mBinding.accoutingDetails.tvCardAmt.setText(
            String.format("%s %s", mOrderHistoryAccountingData.getCurrencySymbol(),
                String.format("%.2f",
                    Float.parseFloat(mOrderHistoryAccountingData.getPayBy().getCard()))));
        break;
      case TWO:
        mBinding.accoutingDetails.vgWallet.setVisibility(View.VISIBLE);
        mBinding.accoutingDetails.tvCashOnDelivery.setVisibility(View.VISIBLE);
        mBinding.accoutingDetails.vgCard.setVisibility(View.GONE);
        mBinding.accoutingDetails.tvWalletAmt.setText(
            String.format("%s %s", mOrderHistoryAccountingData.getCurrencySymbol(),
                String.format("%.2f",
                    Float.parseFloat(mOrderHistoryAccountingData.getPayBy().getWallet()))));
        break;
      case THREE:
        mBinding.accoutingDetails.vgWallet.setVisibility(View.VISIBLE);
        mBinding.accoutingDetails.vgCard.setVisibility(View.GONE);
        mBinding.accoutingDetails.tvCashOnDelivery.setVisibility(View.GONE);
        mBinding.accoutingDetails.tvWalletAmt.setText(
            String.format("%s %s", mOrderHistoryAccountingData.getCurrencySymbol(),
                String.format("%.2f",
                    Float.parseFloat(mOrderHistoryAccountingData.getPayBy().getWallet()))));
        break;
      case FOUR:
        mBinding.accoutingDetails.tvCashOnDelivery.setVisibility(View.GONE);
        mBinding.accoutingDetails.vgWallet.setVisibility(View.GONE);
        mBinding.accoutingDetails.vgCard.setVisibility(View.VISIBLE);
        mBinding.accoutingDetails.tvCardAmt.setText(
            String.format("%s %s", mOrderHistoryAccountingData.getCurrencySymbol(),
                String.format("%.2f",
                    Float.parseFloat(mOrderHistoryAccountingData.getPayBy().getCard()))));
        break;
      case FIVE:
        mBinding.accoutingDetails.tvCashOnDelivery.setVisibility(View.VISIBLE);
        mBinding.accoutingDetails.vgWallet.setVisibility(View.GONE);
        mBinding.accoutingDetails.vgCard.setVisibility(View.GONE);
        break;
    }
  }

  @Override
  public void orderItemClick(String productId, String parentProductId) {
    Intent intent = new Intent(this, ProductDetailsActivity.class);
    intent.putExtra(PARENT_PRODUCT_ID, parentProductId);
    intent.putExtra(PRODUCT_ID, productId);
    startActivity(intent);
  }

  @Override
  public void onSelectItem(int position) {
    if (mHashMap.size() > ZERO) {
      String packageId = (String) mHashMap.keySet().toArray()[position];
      ArrayList<OrderHistProductData> orderHistProductDataList = mHashMap.get(packageId);
      if (orderHistProductDataList.size() > ZERO) {
        OrderHistProductData orderHistProductData = orderHistProductDataList.get(ZERO);
        Bundle bundle = new Bundle();
        bundle.putString(PRODUCT_ID, orderHistProductData.getProductId());
        bundle.putString(PRODUCT_ORDER_ID, orderHistProductData.getProductOrderId());
        bundle.putString(PACK_ID, orderHistProductData.getPackageId());
        bundle.putString(BOX_COUNT,
            String.format("%s%d", getResources().getString(R.string.box), position + ONE));
        bundle.putBoolean(IS_SPIT_PRODUCT, TRUE);
        Intent intent = new Intent(this, HistoryProductDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
      }
    }
  }

  @Override
  public void onTrackClicked(PackingDetailItem data,String position) {
    Bundle bundle = new Bundle();
    if (data.getProductOrderIds()!=null && data.getProductOrderIds().size()>0)
    bundle.putString(PRODUCT_ORDER_ID, data.getProductOrderIds().get(0));
    bundle.putString(PACK_ID, data.getPackageId());
    bundle.putBoolean(IS_PACKAGE, true);
    bundle.putString(BOX_COUNT, position);
    Intent historyDetailIntent = new Intent(this, HistoryProductDetailActivity.class);
    historyDetailIntent.putExtras(bundle);
    startActivityForResult(historyDetailIntent, HISTORY_PRODUCT_DETAIL);
  }
}
