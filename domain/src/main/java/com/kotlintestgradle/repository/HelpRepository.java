package com.kotlintestgradle.repository;

import com.kotlintestgradle.interactor.ecom.HelpUseCase;
import io.reactivex.Single;

public interface HelpRepository {
  Single<HelpUseCase.ResponseValues> help();
}
