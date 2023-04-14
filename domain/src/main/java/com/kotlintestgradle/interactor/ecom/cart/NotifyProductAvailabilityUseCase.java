package com.kotlintestgradle.interactor.ecom.cart;

import com.kotlintestgradle.executor.PostExecutionThread;
import com.kotlintestgradle.executor.ThreadExecutor;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.interactor.ecom.CommonData;
import com.kotlintestgradle.repository.NotifyProductAvailabilityRepository;
import io.reactivex.Single;
import javax.inject.Inject;

public class NotifyProductAvailabilityUseCase extends
    UseCase<NotifyProductAvailabilityUseCase.RequestValues,
            NotifyProductAvailabilityUseCase.ResponseValues> {

  private NotifyProductAvailabilityRepository mRepository;

  @Inject
  public NotifyProductAvailabilityUseCase(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread,
      NotifyProductAvailabilityRepository repository) {
    super(threadExecutor, postExecutionThread);
    this.mRepository = repository;
  }

  @Override
  protected Single<ResponseValues> buildUseCaseObservable(RequestValues requestValues) {
    return mRepository.notifyProductAvailabilityRepository(requestValues.mChildProductId,
        requestValues.mEmail,
        requestValues.mParentProductId);
  }

  public static class RequestValues implements UseCase.RequestValues {

    private String mChildProductId;
    private String mEmail;
    private String mParentProductId;

    public RequestValues(String childProductId, String email, String parentProductId) {
      this.mChildProductId = childProductId;
      this.mEmail = email;
      this.mParentProductId = parentProductId;
    }

    public RequestValues(String email) {
      this.mEmail = email;
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
