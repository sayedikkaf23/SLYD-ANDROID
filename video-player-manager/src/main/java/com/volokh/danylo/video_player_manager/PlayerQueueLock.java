package com.volokh.danylo.video_player_manager;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PlayerQueueLock {

  private static final String TAG = PlayerQueueLock.class.getSimpleName();
  private static final boolean SHOW_LOGS = Config.SHOW_LOGS;
  private final ReentrantLock mQueueLock = new ReentrantLock();
  private final Condition mProcessQueueCondition = mQueueLock.newCondition();

  public void lock(String owner) {
    //if(SHOW_LOGS) Logger.v(TAG, ">> lock, owner [" + owner + "]");
    //mQueueLock.lock();


    if (!mQueueLock.isHeldByCurrentThread()) {
      try {
        mQueueLock.lock();
      } catch (IllegalMonitorStateException e) {
        e.printStackTrace();
      }
    }
    //if(SHOW_LOGS) Logger.v(TAG, "<< lock, owner [" + owner + "]");
  }

  public void unlock(String owner) {
    //if(SHOW_LOGS) Logger.v(TAG, ">> unlock, owner [" + owner + "]");
    if ( mQueueLock.isHeldByCurrentThread() ) {
      try {
        mQueueLock.unlock();
      } catch (IllegalMonitorStateException e) {
        e.printStackTrace();
      }
    }
    //if(SHOW_LOGS) Logger.v(TAG, "<< unlock, owner [" + owner + "]");
  }

  public boolean isLocked(String owner) {
    boolean isLocked = mQueueLock.isLocked();
    //if(SHOW_LOGS) Logger.v(TAG, "isLocked, owner [" + owner + "]");
    return isLocked;
  }

  public void wait(String owner) throws InterruptedException {
    //if(SHOW_LOGS) Logger.v(TAG, ">> wait, owner [" + owner + "]");
    try {
      mProcessQueueCondition.await();
    } catch (InterruptedException e) {
    }
    //if(SHOW_LOGS) Logger.v(TAG, "<< wait, owner [" + owner + "]");
  }

  public void notify(String owner) {
    //if(SHOW_LOGS) Logger.v(TAG, ">> notify, owner [" + owner + "]");

    try {

      mProcessQueueCondition.signal();
    } catch (IllegalMonitorStateException e) {
      e.printStackTrace();
    }
    //if(SHOW_LOGS) Logger.v(TAG, "<< notify, owner [" + owner + "]");
  }
}
