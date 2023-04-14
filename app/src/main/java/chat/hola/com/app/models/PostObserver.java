package chat.hola.com.app.models;

import android.annotation.SuppressLint;
import android.util.Pair;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 5/24/2018.
 */

public class PostObserver {
    private ConnectableObservable<Boolean> connectableObservable;
    private ObservableEmitter<Boolean> emitor;
    private ConnectableObservable<PostUpdateData> likeUnlikeConnectableObservable;
    private ObservableEmitter<PostUpdateData> likeUnlikeObservableEmitter;
    private ConnectableObservable<PostUpdateData> savedConnectableObservable;
    private ObservableEmitter<PostUpdateData> savedObservableEmitter;
    private ConnectableObservable<PostUpdateData> deleteConnectableObservable;
    private ObservableEmitter<PostUpdateData> deleteObservableEmitter;
    private ConnectableObservable<PostUpdateData> updateConnectableObservable;
    private ObservableEmitter<PostUpdateData> updateObservableEmitter;

    private ConnectableObservable<PostUpdateData> postEditedObservable;
    private ObservableEmitter<PostUpdateData> postEditedObservableEmitter;

    /*
     * Bug Title: post was unlocked it did not show in my purchased post section
     * Bug Id: #2705
     * Fix Dev: Hardik
     * Fix Desc: new observer
     * Fix Date: 23/6/21
     * */

    private ConnectableObservable<Pair<String,Integer>> paidPostObservable;
    private ObservableEmitter<Pair<String,Integer>> paidPostObservableEmitter;

    /*
     * Bug Title: the user is shown as following in the home page even after we unfollow the user in the
     * Bug Id: DUBAND012
     * Fix Description: setup new observer to listen user action
     * Developer Name: Hardik
     * Fix Date: 6/4/2021
     * */
    private ConnectableObservable<Pair<Boolean,String>> followObservable;
    private ObservableEmitter<Pair<Boolean,String>> followObservableEmitter;

    private ConnectableObservable<PostUpdateData> tagConnectableObservable;
    private ObservableEmitter<PostUpdateData> tagObservableEmitter;


    @SuppressLint("CheckResult")
    public PostObserver() {

        Observable<Boolean> observable = Observable.create(e -> emitor = e);

        Observable<PostUpdateData> observable1 = Observable.create(e -> likeUnlikeObservableEmitter = e);

        Observable<PostUpdateData> observable2 = Observable.create(e -> savedObservableEmitter = e);

        Observable<PostUpdateData> observable3 = Observable.create(e -> deleteObservableEmitter = e);

        Observable<PostUpdateData> observable4 = Observable.create(e -> updateObservableEmitter = e);

        Observable<PostUpdateData> observable5 = Observable.create(e -> postEditedObservableEmitter = e);

        Observable<Pair<Boolean,String>> observable6 = Observable.create(e -> followObservableEmitter = e);

        Observable<Pair<String,Integer>> observable7 = Observable.create(e -> paidPostObservableEmitter = e);

        Observable<PostUpdateData> observable8 = Observable.create(e -> tagObservableEmitter = e);

        connectableObservable = observable.publish();
        connectableObservable.share();
        connectableObservable.replay();
        connectableObservable.connect();

        likeUnlikeConnectableObservable = observable1.publish();
        likeUnlikeConnectableObservable.share();
        likeUnlikeConnectableObservable.replay();
        likeUnlikeConnectableObservable.connect();

        savedConnectableObservable = observable2.publish();
        savedConnectableObservable.share();
        savedConnectableObservable.replay();
        savedConnectableObservable.connect();

        deleteConnectableObservable = observable3.publish();
        deleteConnectableObservable.share();
        deleteConnectableObservable.replay();
        deleteConnectableObservable.connect();

        updateConnectableObservable = observable4.publish();
        updateConnectableObservable.share();
        updateConnectableObservable.replay();
        updateConnectableObservable.connect();

        postEditedObservable = observable5.publish();
        postEditedObservable.share();
        postEditedObservable.replay();
        postEditedObservable.connect();

        followObservable = observable6.publish();
        followObservable.share();
        followObservable.replay();
        followObservable.connect();

        paidPostObservable = observable7.publish();
        paidPostObservable.share();
        paidPostObservable.replay();
        paidPostObservable.connect();

        tagConnectableObservable = observable8.publish();
        tagConnectableObservable.share();
        tagConnectableObservable.replay();
        tagConnectableObservable.connect();
    }

    public ConnectableObservable<Boolean> getObservable() {
        return connectableObservable;
    }

    public ConnectableObservable<PostUpdateData> getLikeUnlikeObservable() {
        return likeUnlikeConnectableObservable;
    }

    public ConnectableObservable<PostUpdateData> getSavedObservable() {
        return savedConnectableObservable;
    }

    public ConnectableObservable<PostUpdateData> getDeleteObservable() {
        return deleteConnectableObservable;
    }

    public ConnectableObservable<PostUpdateData> getUpdateObservable() {
        return updateConnectableObservable;
    }

    public ConnectableObservable<PostUpdateData> getPostEditedObservable() {
        return postEditedObservable;
    }

    public ConnectableObservable<Pair<Boolean,String>> getFollowObservable() {
        return followObservable;
    }

    public ConnectableObservable<Pair<String,Integer>> getPaidPostsObservable() {
        return paidPostObservable;
    }

    public ConnectableObservable<PostUpdateData> getTagPostsObservable() {
        return tagConnectableObservable;
    }

    public void postData(Boolean flag) {
        if (emitor != null) {
            emitor.onNext(flag);
        }
    }

    public void postLikeUnlikeObject(PostUpdateData data){
        if(likeUnlikeObservableEmitter != null){
            likeUnlikeObservableEmitter.onNext(data);
        }
    }

    public void postSavedUpdate(PostUpdateData data){
        if(savedObservableEmitter != null){
            savedObservableEmitter.onNext(data);
        }
    }

    public void postDeleteUpdate(PostUpdateData data){
        if(deleteObservableEmitter != null){
            deleteObservableEmitter.onNext(data);
        }
    }

    public void postEditUpdate(PostUpdateData data){
        if(updateObservableEmitter != null){
            updateObservableEmitter.onNext(data);
        }
    }

    public void postEditedUpdate(PostUpdateData data){
        if(postEditedObservableEmitter != null){
            postEditedObservableEmitter.onNext(data);
        }
    }

    public void postFollowObservableEmitter(Pair<Boolean,String> pair){
        if(followObservableEmitter != null){
            followObservableEmitter.onNext(pair);
        }
    }

    public void postPaidPostsObservableEmitter(Pair<String,Integer> pair){
        if(paidPostObservableEmitter != null){
            paidPostObservableEmitter.onNext(pair);
        }
    }

    public void tagPostsObservableEmitter(PostUpdateData data){
        if(tagObservableEmitter != null){
            tagObservableEmitter.onNext(data);
        }
    }

}
