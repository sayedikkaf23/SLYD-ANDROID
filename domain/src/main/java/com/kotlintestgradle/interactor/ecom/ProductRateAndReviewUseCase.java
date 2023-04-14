package com.kotlintestgradle.interactor.ecom;

import com.kotlintestgradle.executor.PostExecutionThread;
import com.kotlintestgradle.executor.ThreadExecutor;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.repository.ProductRateAndReviewRepository;
import io.reactivex.Single;
import java.util.ArrayList;
import javax.inject.Inject;

public class ProductRateAndReviewUseCase extends
    UseCase<ProductRateAndReviewUseCase.RequestValues, ProductRateAndReviewUseCase.ResponseValues> {
  private ProductRateAndReviewRepository mRepository;

  @Inject
  public ProductRateAndReviewUseCase(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread,
      ProductRateAndReviewRepository homePageRepository) {
    super(threadExecutor, postExecutionThread);
    this.mRepository = homePageRepository;
  }

  @Override
  protected Single<ResponseValues> buildUseCaseObservable(RequestValues requestValues) {
    return mRepository.rateAndReview(requestValues.mType, requestValues.mReviewType,
        requestValues.mProductId, requestValues.mAttributeId, requestValues.mReviewTitle,
        requestValues.mReviewDescription, requestValues.mRating, requestValues.mImages,
        requestValues.mCity, requestValues.mCountry, requestValues.mIpAddress,
        requestValues.mLatitude,
        requestValues.mLongitude, requestValues.mSellerReview,requestValues.sellerId,requestValues.orderId,requestValues.driverId,requestValues.driverReview);
  }

  public static class RequestValues implements UseCase.RequestValues {
    private int mType;
    private int mReviewType;
    private String mProductId;
    private String mAttributeId;
    private String mReviewTitle;
    private String mReviewDescription;
    private String mSellerReview;
    private double mRating;
    private ArrayList<String> mImages;
    private String mCity;
    private String mCountry;
    private String mIpAddress;
    private String sellerId;
    private double mLatitude;
    private double mLongitude;
    private String orderId;
    private String driverId;
    private String driverReview;

    public RequestValues(int type, int reviewType, String productId, String attributeId,
        String reviewTitle,
        String reviewDescription, double rating, ArrayList<String> images, String city,
        String country, String ipAddress, double latitude, double longitude, String sellerReview,String sellerId,String orderId,String driverId,
                         String driverReview) {
      this.mType = type;
      this.mProductId = productId;
      this.sellerId = sellerId;
      this.orderId = orderId;
      this.mAttributeId = attributeId;
      this.mReviewTitle = reviewTitle;
      this.mReviewDescription = reviewDescription;
      this.mRating = rating;
      this.mImages = images;
      this.mCity = city;
      this.mCountry = country;
      this.mIpAddress = ipAddress;
      this.mLatitude = latitude;
      this.mLongitude = longitude;
      this.mReviewType = reviewType;
      this.mSellerReview = sellerReview;
      this.driverId = driverId;
      this.driverReview = driverReview;
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
      mData = data;
    }
  }
}
