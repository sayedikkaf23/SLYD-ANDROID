package com.kotlintestgradle.repository;

import com.kotlintestgradle.interactor.ecom.LikeDisLikeReviewUseCase;
import io.reactivex.Single;

public interface LikeDisLikeReviewRepository {

  Single<LikeDisLikeReviewUseCase.ResponseValues> likeReview(String reviewId, boolean like);

  Single<LikeDisLikeReviewUseCase.ResponseValues> disLikeReview(boolean disLike, String reviewId);

}
