package com.kotlintestgradle.interactor.ecom.address;

import com.kotlintestgradle.executor.PostExecutionThread;
import com.kotlintestgradle.executor.ThreadExecutor;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.model.ecom.getaddress.AddressData;
import com.kotlintestgradle.repository.GetAddressRepository;
import io.reactivex.Single;
import javax.inject.Inject;

public class GetAddressUseCase extends
    UseCase<GetAddressUseCase.RequestValues, GetAddressUseCase.ResponseValues> {
  private GetAddressRepository mRepository;

  @Inject
  public GetAddressUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
      GetAddressRepository repository) {
    super(threadExecutor, postExecutionThread);
    this.mRepository = repository;
  }

  @Override
  protected Single<ResponseValues> buildUseCaseObservable(
          GetAddressUseCase.RequestValues requestValues) {
    return mRepository.getFilteredProductList();
  }

  public static class RequestValues implements UseCase.RequestValues {

  }

  public static class ResponseValues implements UseCase.ResponseValue {

    private AddressData data;

    public ResponseValues(AddressData data) {
      this.data = data;
    }

    public AddressData getData() {
      return data;
    }

    public void setData(AddressData data) {
      this.data = data;
    }
  }
}
