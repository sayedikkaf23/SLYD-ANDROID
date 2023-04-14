package com.volokh.danylo.video_player_manager;

import android.util.Log;

import com.volokh.danylo.video_player_manager.player_messages.Message;
import com.volokh.danylo.video_player_manager.utils.Logger;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is designed to process a message queue.
 * It calls very specific methods of {@link Message} in very specific times.
 * <p>
 * 1. When message is polled from queue it calls {@link Message#polledFromQueue()}
 * 2. When message should be run it calls {@link Message#runMessage()}
 * 3. When message finished running it calls {@link Message#messageFinished()}
 */
public class MessagesHandlerThread {

    private static final String TAG = MessagesHandlerThread.class.getSimpleName();
    private static final boolean SHOW_LOGS = Config.SHOW_LOGS;

    private final Queue<Message> mPlayerMessagesQueue = new ConcurrentLinkedQueue<>();
    private final PlayerQueueLock mQueueLock = new PlayerQueueLock();
    private final ExecutorService mQueueProcessingThread = Executors.newSingleThreadExecutor();
//    private final Executor mQueueProcessingThread = Executors.newSingleThreadExecutor();

    private AtomicBoolean mTerminated = new AtomicBoolean(false); // TODO: use it
    private Message mLastMessage;

    private boolean queueLockAwaiting = false;

    public MessagesHandlerThread() {
        resumeQueueProcessingManually();
    }

    /**
     * Use it if you need to add a single message
     */
    public void addMessage(Message message) {

        //if (SHOW_LOGS) Logger.v(TAG, ">> addMessage, lock " + message);
        mQueueLock.lock(TAG);

        mPlayerMessagesQueue.add(message);
        mQueueLock.notify(TAG);

        //if (SHOW_LOGS) Logger.v(TAG, "<< addMessage, unlock " + message);
        mQueueLock.unlock(TAG);
    }

    /**
     * Use it if you need to add a multiple messages
     */
    public void addMessages(List<? extends Message> messages) {
        //if (SHOW_LOGS) Logger.v(TAG, ">> addMessages, lock " + messages);
        mQueueLock.lock(TAG);

        mPlayerMessagesQueue.addAll(messages);
        mQueueLock.notify(TAG);

        //if (SHOW_LOGS) Logger.v(TAG, "<< addMessages, unlock " + messages);
        mQueueLock.unlock(TAG);
    }

    public void pauseQueueProcessing(String outer) {
        //if (SHOW_LOGS) Logger.v(TAG, "pauseQueueProcessing, lock " + mQueueLock);
        mQueueLock.lock(outer);
    }

    public void resumeQueueProcessing(String outer) {
        //if (SHOW_LOGS) Logger.v(TAG, "resumeQueueProcessing, unlock " + mQueueLock);

        if (!queueLockAwaiting) {

            resumeQueueProcessingManually();

        }
        mQueueLock.unlock(outer);



    }

    public void clearAllPendingMessages(String outer) {
        //if (SHOW_LOGS)
        //    Logger.v(TAG, ">> clearAllPendingMessages, mPlayerMessagesQueue " + mPlayerMessagesQueue);

        if (mQueueLock.isLocked(outer)) {
            mPlayerMessagesQueue.clear();
        } else {
            throw new RuntimeException("cannot perform action, you are not holding a lock");
        }
        //if (SHOW_LOGS)
        //    Logger.v(TAG, "<< clearAllPendingMessages, mPlayerMessagesQueue " + mPlayerMessagesQueue);
    }

    public void terminate() {
        mTerminated.set(true);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            //if (SHOW_LOGS) Logger.v(TAG, "start worker thread");
            do {


                mQueueLock.lock(TAG);
                //if (SHOW_LOGS) Logger.v(TAG, "mPlayerMessagesQueue " + mPlayerMessagesQueue);

                if (mPlayerMessagesQueue.isEmpty()) {

                    try {
                        //if (SHOW_LOGS) Logger.v(TAG, "queue is empty, wait for new messages");
                        queueLockAwaiting = true;
                        mQueueLock.wait(TAG);
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                        throw new RuntimeException("InterruptedException");
                    }
                    queueLockAwaiting = false;

                }
                mLastMessage = mPlayerMessagesQueue.poll();

                mLastMessage.polledFromQueue();

                //if (SHOW_LOGS) Logger.v(TAG, "poll mLastMessage " + mLastMessage);

                mQueueLock.unlock(TAG);

                //if (SHOW_LOGS) Logger.v(TAG, "run, mLastMessage " + mLastMessage);
                mLastMessage.runMessage();

                mQueueLock.lock(TAG);

                mLastMessage.messageFinished();

                mQueueLock.unlock(TAG);

            } while (!mTerminated.get());

        }
    };
    private Future<?> future;

    private void resumeQueueProcessingManually() {


        if (future != null) {
            future.cancel(true);

        }

        future = mQueueProcessingThread.submit(runnable);


    }
}
