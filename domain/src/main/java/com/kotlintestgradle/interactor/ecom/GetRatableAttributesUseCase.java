package com.kotlintestgradle.interactor.ecom;

import com.kotlintestgradle.executor.PostExecutionThread;
import com.kotlintestgradle.executor.ThreadExecutor;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.model.ecom.getratable.RatableData;
import com.kotlintestgradle.repository.GetRatableAttributesRepository;
import io.reactivex.Single;
import javax.inject.Inject;

public class GetRatableAttributesUseCase extends
    UseCase<GetRatableAttributesUseCase.RequestValues, GetRatableAttributesUseCase.ResponseValues> {

  private GetRatableAttributesRepository mRepository;

  @Inject
  public GetRatableAttributesUseCase(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread,
      GetRatableAttributesRepository repository) {
    super(threadExecutor, postExecutionThread);
    this.mRepository = repository;
  }

  @Override
  protected Single<ResponseValues> buildUseCaseObservable(RequestValues requestValues) {
    return mRepository.getRatableAttributes(requestValues.storeCatId,requestValues.mProductId,requestValues.driverId,requestValues.orderId);
  }

  public static class RequestValues implements UseCase.RequestValues {
    private String mProductId,orderId,driverId, storeCatId;

    public RequestValues(String storeCatId,String productId,String orderId,String driverId) {
      this.storeCatId = storeCatId;
      this.mProductId = productId;
      this.orderId = orderId;
      this.driverId = driverId;
    }
  }

  public static class ResponseValues implements UseCase.ResponseValue {

    private RatableData mData;

    public ResponseValues(RatableData data) {
      this.mData = data;
    }

    public RatableData getData() {
      return mData;
    }

    public void setData(RatableData data) {
      this.mData = data;
    }
  }
}
