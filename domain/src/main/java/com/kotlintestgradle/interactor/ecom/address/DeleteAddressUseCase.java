package com.kotlintestgradle.interactor.ecom.address;

import com.kotlintestgradle.executor.PostExecutionThread;
import com.kotlintestgradle.executor.ThreadExecutor;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.CommonData;
import com.kotlintestgradle.repository.DeleteAddressRepository;
import io.reactivex.Single;
import javax.inject.Inject;

public class DeleteAddressUseCase extends
    UseCase<DeleteAddressUseCase.RequestValues, DeleteAddressUseCase.ResponseValues> {

  private DeleteAddressRepository mRepository;

  @Inject
  public DeleteAddressUseCase(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread,
      DeleteAddressRepository repository) {
    super(threadExecutor, postExecutionThread);
    this.mRepository = repository;
  }

  @Override
  protected Single<ResponseValues> buildUseCaseObservable(RequestValues requestValues) {
    return mRepository.deleteAddress(requestValues.mAddressId);
  }

  public static class RequestValues implements UseCase.RequestValues {
    private String mAddressId;

    public RequestValues(String addressId) {
      this.mAddressId = addressId;
    }

    public String getAddressId() {
      return mAddressId;
    }

    public void setAddressId(String addressId) {
      this.mAddressId = addressId;
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
