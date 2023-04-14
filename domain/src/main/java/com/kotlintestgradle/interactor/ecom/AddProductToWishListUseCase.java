package com.kotlintestgradle.interactor.ecom;

import com.kotlintestgradle.executor.PostExecutionThread;
import com.kotlintestgradle.executor.ThreadExecutor;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.repository.AddProductToWishListRepository;
import io.reactivex.Single;
import javax.inject.Inject;

public class AddProductToWishListUseCase extends
    UseCase<AddProductToWishListUseCase.RequestValues, AddProductToWishListUseCase.ResponseValues> {
  private AddProductToWishListRepository mRepository;

  @Inject
  public AddProductToWishListUseCase(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread,
      AddProductToWishListRepository repository) {
    super(threadExecutor, postExecutionThread);
    this.mRepository = repository;
  }

  @Override
  protected Single<ResponseValues> buildUseCaseObservable(RequestValues requestValues) {
    return mRepository.addProductWishList(requestValues.mProductId, requestValues.mIpAddress,
        requestValues.mLatitude, requestValues.mLongitude,requestValues.cityId,requestValues.countryId,
        requestValues.userId);
  }

  public static class RequestValues implements UseCase.RequestValues {
    private String mProductId;
    private String mIpAddress;
    private double mLatitude;
    private double mLongitude;
    private String cityId;
    private String countryId;
    private String userId;

    public RequestValues(String productId, String ipAddress, double latitude, double longitude,
        String cityId, String countryId, String userId) {
      mProductId = productId;
      mIpAddress = ipAddress;
      mLatitude = latitude;
      mLongitude = longitude;
      this.cityId = cityId;
      this.countryId = countryId;
      this.userId = userId;
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
