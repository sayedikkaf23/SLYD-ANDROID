package com.kotlintestgradle.interactor.ecom.cart;

import com.kotlintestgradle.executor.PostExecutionThread;
import com.kotlintestgradle.executor.ThreadExecutor;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.model.ecom.pdp.ProductDataModel;
import com.kotlintestgradle.repository.ProductDetailsRepository;
import io.reactivex.Single;
import javax.inject.Inject;

public class PdpUseCase extends UseCase<PdpUseCase.RequestValues, PdpUseCase.ResponseValues> {
  private ProductDetailsRepository mRepository;

  @Inject
  public PdpUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
      ProductDetailsRepository repository) {
    super(threadExecutor, postExecutionThread);
    this.mRepository = repository;
  }

  @Override
  protected Single<ResponseValues> buildUseCaseObservable(RequestValues requestValues) {
    return mRepository.getPdp(requestValues.mProductId, requestValues.mParentProductId,
        requestValues.lat, requestValues.longitude,requestValues.ipAddress,requestValues.city,requestValues.country);
  }

  public static class RequestValues implements UseCase.RequestValues {
    private String mProductId;
    private String mParentProductId;
    private double lat;
    private double longitude;
    private String ipAddress;
    private String city;
    private String country;

    public RequestValues(String productId, String parentProductId, double lat, double longitude,
        String ipAddress, String city, String country) {
      mProductId = productId;
      mParentProductId = parentProductId;
      this.lat = lat;
      this.longitude = longitude;
      this.ipAddress = ipAddress;
      this.city = city;
      this.country = country;
    }

    public double getLat() {
      return lat;
    }

    public void setLat(double lat) {
      this.lat = lat;
    }

    public double getLongitude() {
      return longitude;
    }

    public void setLongitude(double longitude) {
      this.longitude = longitude;
    }

    public String getIpAddress() {
      return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
      this.ipAddress = ipAddress;
    }

    public String getCity() {
      return city;
    }

    public void setCity(String city) {
      this.city = city;
    }

    public String getCountry() {
      return country;
    }

    public void setCountry(String country) {
      this.country = country;
    }

    public String getParentProductId() {
      return mParentProductId;
    }

    public void setParentProductId(String parentProductId) {
      this.mParentProductId = parentProductId;
    }

    public String getProductId() {
      return mProductId;
    }

    public void setProductId(String productId) {
      this.mProductId = productId;
    }
  }

  public static class ResponseValues implements UseCase.ResponseValue {
    private ProductDataModel mData;

    public ResponseValues(ProductDataModel data) {
      this.mData = data;
    }

    public ProductDataModel getData() {
      return mData;
    }

    public void setData(ProductDataModel data) {
      this.mData = data;
    }
  }
}
