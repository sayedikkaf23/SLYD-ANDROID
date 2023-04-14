package com.kotlintestgradle.interactor.ecom.address;

import com.kotlintestgradle.executor.PostExecutionThread;
import com.kotlintestgradle.executor.ThreadExecutor;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.CommonData;
import com.kotlintestgradle.repository.AddAddressRepository;
import io.reactivex.Single;
import javax.inject.Inject;

public class AddAddressUseCase extends
    UseCase<AddAddressUseCase.RequestValues, AddAddressUseCase.ResponseValues> {

  private AddAddressRepository mRepository;

  @Inject
  public AddAddressUseCase(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread,
      AddAddressRepository repository) {
    super(threadExecutor, postExecutionThread);
    this.mRepository = repository;
  }

  @Override
  protected Single<ResponseValues> buildUseCaseObservable(RequestValues requestValues) {
    return mRepository.addAddress(requestValues.isApiCall,requestValues.mName,
        requestValues.mMobileNumber,
        requestValues.mMobileNumberCode, requestValues.mLocality, requestValues.mAddLine1,
        requestValues.mAddLine2, requestValues.mLandmark, requestValues.mCity,
        requestValues.mCountry, requestValues.mPlaceId, requestValues.mPinCode,
        requestValues.mLatitude,
        requestValues.mLongitude, requestValues.mTaggedAs, requestValues.mState,
        requestValues.mTagged);
  }

  public static class RequestValues implements UseCase.RequestValues {

    private String mName;
    private String mMobileNumber;
    private String mMobileNumberCode;
    private String mLocality;
    private String mAddLine1;
    private String mAddLine2;
    private String mLandmark;
    private String mCity;
    private String mState;
    private String mCountry;
    private String mPlaceId;
    private String mPinCode;
    private String mLatitude;
    private String mLongitude;
    private String mTaggedAs;
    private int mTagged;
    private boolean isApiCall;

    public RequestValues(boolean isApiCall,String name, String mobileNumber,
        String mobileNumberCode,
        String locality, String addLine1, String addLine2, String landmark,
        String city, String country, String placeId, String pinCode, String latitude,
        String longitude, String taggedAs, String state, int tagged) {

      this.isApiCall=isApiCall;
      this.mName = name;
      this.mMobileNumber = mobileNumber;
      this.mMobileNumberCode = mobileNumberCode;
      this.mLocality = locality;
      this.mAddLine1 = addLine1;
      this.mAddLine2 = addLine2;
      this.mLandmark = landmark;
      this.mCity = city;
      this.mState = state;
      this.mCountry = country;
      this.mPlaceId = placeId;
      this.mPinCode = pinCode;
      this.mLatitude = latitude;
      this.mLongitude = longitude;
      this.mTaggedAs = taggedAs;
      this.mTagged = tagged;
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

    public void setData(CommonData data) {
      this.mData = data;
    }
  }
}
