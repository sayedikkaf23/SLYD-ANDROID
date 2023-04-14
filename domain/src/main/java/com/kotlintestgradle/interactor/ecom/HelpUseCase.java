package com.kotlintestgradle.interactor.ecom;

import com.kotlintestgradle.executor.PostExecutionThread;
import com.kotlintestgradle.executor.ThreadExecutor;
import com.kotlintestgradle.interactor.UseCase;
import com.kotlintestgradle.model.ecom.help.HelpListData;
import com.kotlintestgradle.repository.HelpRepository;
import io.reactivex.Single;
import javax.inject.Inject;

public class HelpUseCase extends
    UseCase<HelpUseCase.RequestValues, HelpUseCase.ResponseValues> {
  private HelpRepository mRepository;

  @Inject
  public HelpUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
      HelpRepository repository) {
    super(threadExecutor, postExecutionThread);
    this.mRepository = repository;
  }

  @Override
  protected Single<ResponseValues> buildUseCaseObservable(RequestValues requestValues) {
    return mRepository.help();
  }

  public static class RequestValues implements UseCase.RequestValues {

  }

  public static class ResponseValues implements UseCase.ResponseValue {
    private HelpListData mData;

    public ResponseValues(HelpListData data) {
      this.mData = data;
    }

    public HelpListData getData() {
      return mData;
    }

    public void setData(HelpListData data) {
      this.mData = data;
    }
  }
}
