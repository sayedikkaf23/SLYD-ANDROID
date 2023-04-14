package com.kotlintestgradle;

import android.os.Handler;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;


/**
 * @author 3Embed
 *
 *for thread pool Scheduler
 *
 * @since 1.0 (23-Aug-2019)
 */

public class UseCaseThreadPoolScheduler implements UseCaseScheduler {

  public static final int POOL_SIZE = 2;
  public static final int MAX_POOL_SIZE = 4;
  public static final int TIMEOUT = 30;
  private final Handler mHandler = new Handler();
  ThreadPoolExecutor mThreadPoolExecutor;

  public UseCaseThreadPoolScheduler() {
    mThreadPoolExecutor =
        new ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, TIMEOUT, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(POOL_SIZE));
  }

  @Override
  public void execute(@NotNull Runnable runnable) {
    mThreadPoolExecutor.execute(runnable);
  }


}
