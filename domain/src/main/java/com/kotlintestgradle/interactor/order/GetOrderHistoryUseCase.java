package com.kotlintestgradle.interactor.order;

import com.kotlintestgradle.executor.PostExecutionThread;
import com.kotlintestgradle.executor.ThreadExecutor;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.model.orderhistory.OrderHistoryData;
import com.kotlintestgradle.repository.GetOrderHistoryRepository;

import javax.inject.Inject;

import io.reactivex.Single;

public class GetOrderHistoryUseCase extends
    UseCase<GetOrderHistoryUseCase.RequestValues, GetOrderHistoryUseCase.ResponseValues> {
  private GetOrderHistoryRepository mRepository;

  @Inject
  public GetOrderHistoryUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread, GetOrderHistoryRepository repository) {
    super(threadExecutor, postExecutionThread);
    this.mRepository = repository;
  }

  @Override
  protected Single<ResponseValues> buildUseCaseObservable(RequestValues requestValues) {
    return mRepository.getOrderHistory(requestValues.limit, requestValues.skip,
        requestValues.status, requestValues.orderType,
        requestValues.storeType, requestValues.orderBy, requestValues.search,
        requestValues.orderTime);
  }

  public static class RequestValues implements UseCase.RequestValues {
    private int limit;
    private int skip;
    private int status;
    private int orderType;
    private int storeType;
    private int orderBy;
    private String search;
    private String orderTime;

    public RequestValues(int limit, int skip, int status, int orderType,
        int storeType, int orderBy, String search, String orderTime) {
      this.limit = limit;
      this.skip = skip;
      this.status = status;
      this.orderType = orderType;
      this.storeType = storeType;
      this.orderBy = orderBy;
      this.search = search;
      this.orderTime = orderTime;
    }
  }

  public static class ResponseValues implements UseCase.ResponseValue {
    private OrderHistoryData mData;

    public ResponseValues(OrderHistoryData data) {
      this.mData = data;
    }

    public OrderHistoryData getData() {
      return mData;
    }

    public void setData(OrderHistoryData data) {
      this.mData = data;
    }
  }
}
