package com.kotlintestgradle.interactor.ecom;

import com.kotlintestgradle.executor.PostExecutionThread;
import com.kotlintestgradle.executor.ThreadExecutor;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.model.ecom.wishlist.WishListData;
import com.kotlintestgradle.repository.GetWishListRepository;
import io.reactivex.Single;
import javax.inject.Inject;

public class GetWishListUseCase extends
    UseCase<GetWishListUseCase.RequestValues, GetWishListUseCase.ResponseValues> {
  private GetWishListRepository mRepository;

  @Inject
  public GetWishListUseCase(ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread,
      GetWishListRepository repository) {
    super(threadExecutor, postExecutionThread);
    this.mRepository = repository;
  }

  @Override
  protected Single<ResponseValues> buildUseCaseObservable(RequestValues requestValues) {
    return mRepository.getWishListProducts(requestValues.mSortType, requestValues.mSearchQuery);
  }

  public static class RequestValues implements UseCase.RequestValues {
    private int mSortType;
    private String mSearchQuery;

    public RequestValues(int sortType) {
      this.mSortType = sortType;
    }

    public RequestValues(int sortType, String searchQuery) {
      this.mSortType = sortType;
      this.mSearchQuery = searchQuery;
    }
  }

  public static class ResponseValues implements UseCase.ResponseValue {
    private WishListData mData;

    public ResponseValues(WishListData data) {
      this.mData = data;
    }

    public WishListData getData() {
      return mData;
    }

    public void setData(WishListData data) {
      this.mData = data;
    }
  }
}
