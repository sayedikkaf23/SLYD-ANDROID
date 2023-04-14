package com.kotlintestgradle.interactor.ecom.address;

import com.kotlintestgradle.executor.PostExecutionThread;
import com.kotlintestgradle.executor.ThreadExecutor;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.model.ecom.deliveryfee.DeliveryFee;
import com.kotlintestgradle.repository.GetDeliveryFeeRepository;
import io.reactivex.Single;
import javax.inject.Inject;

public class GetDeliveryFeeUseCase extends
    UseCase<GetDeliveryFeeUseCase.RequestValues, GetDeliveryFeeUseCase.ResponseValues> {
  private GetDeliveryFeeRepository mRepository;

  @Inject
  public GetDeliveryFeeUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                           GetDeliveryFeeRepository repository) {
    super(threadExecutor, postExecutionThread);
    this.mRepository = repository;
  }

  @Override
  protected Single<ResponseValues> buildUseCaseObservable(
          GetDeliveryFeeUseCase.RequestValues requestValues) {
    return mRepository.calculateDeliveryFee(requestValues.mCartId,
        requestValues.mDeliveryAddressId);
  }

  public static class RequestValues implements UseCase.RequestValues {
    private String mCartId;
    private String mDeliveryAddressId;

    public RequestValues(String mCartId, String mDeliveryAddressId) {
      this.mCartId = mCartId;
      this.mDeliveryAddressId = mDeliveryAddressId;
    }
  }

  public static class ResponseValues implements UseCase.ResponseValue {

    private DeliveryFee data;

    public ResponseValues(DeliveryFee data) {
      this.data = data;
    }

    public DeliveryFee getData() {
      return data;
    }

    public void setData(DeliveryFee data) {
      this.data = data;
    }
  }
}

