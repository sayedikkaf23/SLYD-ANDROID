package com.kotlintestgradle.interactor.ecom.cart;

import static com.kotlintestgradle.DomainConstants.UPDATE_ADDRESS_IN_CART;

import com.kotlintestgradle.executor.PostExecutionThread;
import com.kotlintestgradle.executor.ThreadExecutor;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.CommonData;
import com.kotlintestgradle.model.ecom.AddOnsObject;
import com.kotlintestgradle.model.ecom.pdp.PdpOfferData;
import com.kotlintestgradle.repository.AddProductToCartRepository;
import io.reactivex.Single;
import java.util.ArrayList;
import javax.inject.Inject;

public class AddProductToCartUseCase extends
    UseCase<AddProductToCartUseCase.RequestValues, AddProductToCartUseCase.ResponseValues> {
  private AddProductToCartRepository mRepository;

  @Inject
  public AddProductToCartUseCase(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread, AddProductToCartRepository repository) {
    super(threadExecutor, postExecutionThread);
    this.mRepository = repository;
  }

  @Override
  protected Single<ResponseValues> buildUseCaseObservable(RequestValues requestValues) {
    if (requestValues.action == UPDATE_ADDRESS_IN_CART) {
      return mRepository.addProductToCart(requestValues.action, requestValues.mDeliveryAddressId, requestValues.mStoreId);
    } else {
      return mRepository.addProductToCart(requestValues.mUserType, requestValues.mCentralProductId,
              requestValues.mProductId, requestValues.mUnitId, requestValues.mStoreId,
              requestValues.mQuantity, requestValues.mCartType,
              requestValues.mNotes, requestValues.mUserIp, requestValues.mUserPostCode,
              requestValues.mLatitude, requestValues.mLongitude,
              requestValues.mCity, requestValues.mStoreTypeId, requestValues.mProductName,
              requestValues.mEstimatedProductPrice,
              requestValues.mExtraNotes, requestValues.mOfferData, requestValues.action, requestValues.addtoCartId, requestValues.addsOnDataRecords,
              requestValues.oldProductId);
    }
  }

  public static class RequestValues implements UseCase.RequestValues {
    private int mUserType;
    private String mCentralProductId;
    private String mProductId;
    private String mUnitId;
    private String mStoreId;
    private int mQuantity;
    private int mCartType;
    private String mNotes;
    private String mUserIp;
    private String mUserPostCode;
    private double mLatitude;
    private double mLongitude;
    private String mCity;
    private int mStoreTypeId;
    private String mProductName;
    private String mEstimatedProductPrice;
    private String mExtraNotes;
    private String addtoCartId;
    private PdpOfferData mOfferData;
    private int selectedAddOns;
    private int addOnsPrice;
    private int action;
    private String mDeliveryAddressId;
    private String oldProductId;
    private ArrayList<AddOnsObject> addsOnDataRecords;

    public RequestValues(int action, String addressId, String storeCategoryId) {
      this.action = action;
      this.mStoreId = storeCategoryId;
      this.mDeliveryAddressId = addressId;
    }

    public RequestValues(int userType, String centralProductId, String productId,
        String unitId, String storeId, int quantity, int cartType, String notes,
        String userIp, String userPostCode, double latitude, double longitude, String city,
        int storeTypeId, String productName, String estimatedProductPrice,String extraNotes,String addtoCartId,
        PdpOfferData offerData,int action,ArrayList<AddOnsObject> addsOnDataRecords,String oldProductId) {
      this.mUserType = userType;
      this.mCentralProductId = centralProductId;
      this.mProductId = productId;
      this.mUnitId = unitId;
      this.mStoreId = storeId;
      this.mQuantity = quantity;
      this.mCartType = cartType;
      this.mNotes = notes;
      this.mUserIp = userIp;
      this.mUserPostCode = userPostCode;
      this.mLatitude = latitude;
      this.mLongitude = longitude;
      this.mCity = city;
      this.mStoreTypeId = storeTypeId;
      this.mProductName = productName;
      this.mEstimatedProductPrice = estimatedProductPrice;
      this.mExtraNotes = extraNotes;
      this.mOfferData = offerData;
      this.action = action;
      this.addsOnDataRecords = addsOnDataRecords;
      this.addtoCartId = addtoCartId;
      this.oldProductId = oldProductId;
    }
  }

  public static class ResponseValues implements UseCase.ResponseValue {
    private CommonData mData;

    public ResponseValues(CommonData data) {
      this.mData = data;
    }

    public CommonData getData() {
      return mData;
    }
  }
}
