package com.kotlintestgradle.interactor.ecom;

import com.kotlintestgradle.executor.PostExecutionThread;
import com.kotlintestgradle.executor.ThreadExecutor;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.model.ecom.location.IpAddressToLocationData;
import com.kotlintestgradle.repository.IpAddressToLocationRepository;
import io.reactivex.Single;
import javax.inject.Inject;

public class IpAddressToLocationUseCase extends
    UseCase<IpAddressToLocationUseCase.RequestValues, IpAddressToLocationUseCase.ResponseValues> {

  private IpAddressToLocationRepository mRepository;

  @Inject
  public IpAddressToLocationUseCase(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread, IpAddressToLocationRepository repository) {
    super(threadExecutor, postExecutionThread);
    this.mRepository = repository;
  }

  @Override
  protected Single<ResponseValues> buildUseCaseObservable(RequestValues requestValues) {
    return mRepository.ipAddressToLocation(requestValues.mIpAddress,requestValues.currencySymbol,
        requestValues.currencyCode);
  }

  public static class RequestValues implements UseCase.RequestValues {

    private String mIpAddress;
    private String currencySymbol;
    private String currencyCode;

    public RequestValues(String ipAddress, String currencySymbol, String currencyCode) {
      mIpAddress = ipAddress;
      this.currencySymbol = currencySymbol;
      this.currencyCode = currencyCode;
    }
  }

  public static class ResponseValues implements UseCase.ResponseValue {

    private IpAddressToLocationData mData;

    public ResponseValues(IpAddressToLocationData data) {
      mData = data;
    }

    public IpAddressToLocationData getData() {
      return mData;
    }

    public void setData(IpAddressToLocationData data) {
      mData = data;
    }
  }
}
