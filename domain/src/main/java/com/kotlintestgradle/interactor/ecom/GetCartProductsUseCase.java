package com.kotlintestgradle.interactor.ecom;

import com.kotlintestgradle.executor.PostExecutionThread;
import com.kotlintestgradle.executor.ThreadExecutor;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.model.ecom.getcart.CartData;
import com.kotlintestgradle.repository.GetCartProductsRepository;
import io.reactivex.Single;
import javax.inject.Inject;

public class GetCartProductsUseCase extends
    UseCase<GetCartProductsUseCase.RequestValues, GetCartProductsUseCase.ResponseValues> {

  private GetCartProductsRepository mRepository;

  @Inject
  public GetCartProductsUseCase(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread, GetCartProductsRepository repository) {
    super(threadExecutor, postExecutionThread);
    this.mRepository = repository;
  }

  @Override
  protected Single<ResponseValues> buildUseCaseObservable(RequestValues requestValues) {
    return mRepository.getCartProducts(requestValues.isDine, requestValues.userId,
        requestValues.currencySymbol, requestValues.currencyCode);
  }

  public static class RequestValues implements UseCase.RequestValues {
    private boolean isDine;
    private String userId;
    private String currencySymbol;
    private String currencyCode;

    public RequestValues(boolean isDine, String userId, String currencySymbol,
        String currencyCode) {
      this.isDine = isDine;
      this.userId = userId;
      this.currencySymbol = currencySymbol;
      this.currencyCode = currencyCode;
    }
  }

  public static class ResponseValues implements UseCase.ResponseValue {
    private CartData mData;

    public ResponseValues(CartData data) {
      this.mData = data;
    }

    public CartData getData() {
      return mData;
    }

    public void setData(CartData data) {
      this.mData = data;
    }
  }
}
