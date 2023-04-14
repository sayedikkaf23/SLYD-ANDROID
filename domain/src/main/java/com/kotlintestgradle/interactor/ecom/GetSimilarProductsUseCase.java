package com.kotlintestgradle.interactor.ecom;

import com.kotlintestgradle.executor.PostExecutionThread;
import com.kotlintestgradle.executor.ThreadExecutor;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.model.ecom.similarproducts.SimilarProductsData;
import com.kotlintestgradle.repository.GetSimilarProductsRepository;
import io.reactivex.Single;
import javax.inject.Inject;

public class GetSimilarProductsUseCase extends
    UseCase<GetSimilarProductsUseCase.RequestValues, GetSimilarProductsUseCase.ResponseValues> {

  private GetSimilarProductsRepository mRepository;

  @Inject
  public GetSimilarProductsUseCase(ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread, GetSimilarProductsRepository repository) {
    super(threadExecutor, postExecutionThread);
    this.mRepository = repository;
  }

  @Override
  protected Single<ResponseValues> buildUseCaseObservable(
          GetSimilarProductsUseCase.RequestValues requestValues) {
    return mRepository.getSimilarProducts(requestValues.mItemId, requestValues.mLimit);
  }

  public static class RequestValues implements UseCase.RequestValues {
    private String mItemId;
    private int mLimit;

    public RequestValues(String itemId, int limit) {
      this.mItemId = itemId;
      this.mLimit = limit;
    }

  }

  public static class ResponseValues implements UseCase.ResponseValue {

    private SimilarProductsData mData;

    public ResponseValues(SimilarProductsData data) {
      this.mData = data;
    }

    public SimilarProductsData getData() {
      return mData;
    }

    public void setData(SimilarProductsData data) {
      this.mData = data;
    }
  }
}
