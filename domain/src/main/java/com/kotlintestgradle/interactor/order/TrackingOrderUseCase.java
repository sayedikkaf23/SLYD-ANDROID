package com.kotlintestgradle.interactor.order;



import com.kotlintestgradle.executor.PostExecutionThread;
import com.kotlintestgradle.executor.ThreadExecutor;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.model.tracking.TrackingData;
import com.kotlintestgradle.repository.TrackingRepository;

import javax.inject.Inject;

import io.reactivex.Single;

public class TrackingOrderUseCase extends
    UseCase<TrackingOrderUseCase.RequestValues, TrackingOrderUseCase.ResponseValues> {
  private TrackingRepository mRepository;

  @Inject
  public TrackingOrderUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread, TrackingRepository repository) {
    super(threadExecutor, postExecutionThread);
    this.mRepository = repository;
  }

  @Override
  protected Single<ResponseValues> buildUseCaseObservable(RequestValues requestValues) {
    return mRepository.trackOrder(requestValues.productOrderId);
  }

  public static class RequestValues implements UseCase.RequestValues {
    private String productOrderId;

    public RequestValues(String productOrderId) {
      this.productOrderId = productOrderId;
    }
  }

  public static class ResponseValues implements UseCase.ResponseValue {
    private TrackingData mData;

    public ResponseValues(TrackingData data) {
      this.mData = data;
    }

    public TrackingData getData() {
      return mData;
    }

    public void setData(TrackingData data) {
      this.mData = data;
    }
  }
}
