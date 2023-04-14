package com.kotlintestgradle.data.repository.ecom;

import com.kotlintestgradle.data.DataSource;
import com.kotlintestgradle.data.mapper.SuccessMapper;
import com.kotlintestgradle.data.preference.PreferenceManager;
import com.kotlintestgradle.data.repository.BaseRepository;
import com.kotlintestgradle.interactor.ecom.LikeDisLikeReviewUseCase;
import com.kotlintestgradle.remote.model.request.ecom.ReviewLikeDisLikeRequest;
import com.kotlintestgradle.remote.model.response.ecom.CommonModel;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import javax.inject.Inject;
import com.kotlintestgradle.repository.LikeDisLikeReviewRepository;

public class LikeDisLikeReviewRepositoryImpl extends BaseRepository implements
    LikeDisLikeReviewRepository {

  private DataSource dataSource;
  private PreferenceManager mPreferenceManager;
  private SuccessMapper mSuccessMapper = new SuccessMapper();

  @Inject
  public LikeDisLikeReviewRepositoryImpl(DataSource dataSource) {
    super(dataSource);
    this.dataSource = dataSource;
    this.mPreferenceManager = dataSource.preference();
  }

  @Override
  public Single<LikeDisLikeReviewUseCase.ResponseValues> likeReview(String reviewId, boolean like) {
    return dataSource.api().handler().reviewLikeDisLike(mPreferenceManager.getAuthToken(),
        new ReviewLikeDisLikeRequest(reviewId, like)).flatMap(
        new Function<CommonModel,
            SingleSource<? extends LikeDisLikeReviewUseCase.ResponseValues>>() {
            @Override
            public SingleSource<? extends LikeDisLikeReviewUseCase.ResponseValues> apply(
                CommonModel model) throws Exception {
              return Single.just(
                  new LikeDisLikeReviewUseCase.ResponseValues(mSuccessMapper.mapper(model)));
            }
          });
  }

  @Override
  public Single<LikeDisLikeReviewUseCase.ResponseValues> disLikeReview(boolean disLike,
      String reviewId) {
    return dataSource.api().handler().reviewLikeDisLike(mPreferenceManager.getAuthToken(),
        new ReviewLikeDisLikeRequest(disLike, reviewId)).flatMap(
        new Function<CommonModel,
            SingleSource<? extends LikeDisLikeReviewUseCase.ResponseValues>>() {
            @Override
            public SingleSource<? extends LikeDisLikeReviewUseCase.ResponseValues> apply(
                CommonModel model) throws Exception {
              return Single.just(
                  new LikeDisLikeReviewUseCase.ResponseValues(mSuccessMapper.mapper(model)));
            }
          });
  }
}
