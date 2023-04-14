package com.kotlintestgradle.data.repository.ecom;

import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.HelpMapper;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.interactor.ecom.HelpUseCase;
import com.kotlintestgradle.remote.model.response.help.HelpListDetails;
import com.kotlintestgradle.repository.HelpRepository;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import javax.inject.Inject;

public class HelpRepositoryImpl extends BaseRepository implements
    HelpRepository {
  private DataSource mDataSource;
  private HelpMapper mMapper = new HelpMapper();

  @Inject
  public HelpRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    this.mDataSource = dataSource;
  }

  @Override
  public Single<HelpUseCase.ResponseValues> help() {
    return mDataSource.api().handler().getHelp().flatMap(
        (Function<HelpListDetails, SingleSource<? extends HelpUseCase.ResponseValues>>) details -> Single.just(
            new HelpUseCase.ResponseValues(mMapper.mapper(details))));
  }
}
