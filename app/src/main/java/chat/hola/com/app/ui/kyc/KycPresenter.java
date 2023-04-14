package chat.hola.com.app.ui.kyc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.ezcall.android.R;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.android.policy.TimeWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.ImageFilePath;
import chat.hola.com.app.Utilities.UriUtil;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Documents;
import chat.hola.com.app.models.Error;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * <h1>IdentificationPresenter</h1>
 *
 * <p>This is implemented presenter of {@link KycActivity},
 * it will call apis and handles apis responses and
 * communicate between models and {@link  KycActivity}</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 05 Dec 2019
 */
public class KycPresenter implements KycContract.Presenter {

    private KycContract.View view;

    @Inject
    Context context;
    @Inject
    HowdooService service;
    @Inject
    SessionManager sessionManager;

    private String picturePath = null;
    private String frontImage = null;
    private String backImage = null;
    private Uri imageUri;

    @Inject
    public KycPresenter() {

    }

    @Override
    public void attach(KycContract.View view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
    }

    @Override
    public void updateUserDetail(Map<String, Object> params) {
        if (view != null) view.showLoader();
        params.put("documentFrontImage", frontImage);
        params.put("documentBackImage", backImage);
        service.userDetail(AppController.getInstance().getApiToken(), Constants.LANGUAGE, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        if (response.code() == 200) {
                            view.moveNext();
                        } else {
                            if (view != null)
                                view.hideLoader();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null)
                            view.hideLoader();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void amazonUploadDoc(String image) {
        Log.i("cloudinar1", image);
        MediaManager.get()
                .upload(image)
                .option("folder", "kyc")
                .option(Constants.Post.PUBLIC_ID, sessionManager.getUserId() + "_FRONT")
                .option(Constants.Post.RESOURCE_TYPE, Constants.Post.IMAGE)
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        frontImage = String.valueOf(resultData.get("url"));
                        Log.i("cloudinary2", frontImage);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Log.e("cloudinary3", error.getDescription());
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }
                })
                .constrain(TimeWindow.immediate())
                .dispatch();
    }

    private void amazonUploadBackDoc(String image) {
        MediaManager.get()
                .upload(image)
                .option("folder", "kyc")
                .option(Constants.Post.PUBLIC_ID, sessionManager.getUserId() + "_BACK")
                .option(Constants.Post.RESOURCE_TYPE, Constants.Post.IMAGE)
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        backImage = String.valueOf(resultData.get("url"));
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {

                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }
                })
                .constrain(TimeWindow.immediate())
                .dispatch();
    }

    @Override
    public void typesOfDocuments() {
        //TODO remove below 3 lines when API will work
        List<String> list = new ArrayList<>();
        if (view != null)
            view.typesOfDocuments(list);

        service.documentType(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Documents>>() {
                    @Override
                    public void onNext(Response<Documents> response) {
                        if (response.code() == 200) {
                            if (view != null)
                                view.typesOfDocuments(response.body().getData());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void parseSelectedImage(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            try {
                Uri uri = data.getData();
                if (uri == null) return;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    picturePath = ImageFilePath.getPathAboveN(context, uri);
                } else {

                    picturePath = ImageFilePath.getPath(context, uri);
                }

                if (picturePath != null) {
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(picturePath, options);

                    if (options.outWidth > 0 && options.outHeight > 0) {
                        //launch crop image
                        if (view != null)
                            view.launchCropImage(data.getData());
                    } else {
                        //image can't be selected try another
                        if (view != null)
                            view.message(R.string.string_31);
                    }
                } else {
                    //image can't be selected try another
                    if (view != null)
                        view.message(R.string.string_31);
                }
            } catch (OutOfMemoryError e) {
                //out of mem try again
                if (view != null)
                    view.message(R.string.string_15);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //image selection canceled.
            if (view != null)
                view.message(R.string.string_16);
        } else {
            if (view != null)
                //failed to select image.
                view.message(R.string.string_113);
        }
    }

    @Override
    public void parseCapturedImage(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            try {

                picturePath = data.getStringExtra("imagePath");
                imageUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider",
                        new File(picturePath));

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(picturePath, options);
                if (options.outWidth > 0 && options.outHeight > 0) {
                    //launch crop image
                    //here
                    if (view != null)
                        view.launchCropImage(imageUri);
                } else {
                    //failed to capture image.
                    picturePath = null;
                    if (view != null)
                        view.message(R.string.string_17);
                }
            } catch (OutOfMemoryError e) {
                //out of mem try again
                picturePath = null;
                if (view != null)
                    view.message(R.string.string_15);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            picturePath = null;
            //img capture canceled.
            if (view != null)
                view.message(R.string.string_18);
        } else {
            //sorry failed to capture
            picturePath = null;
            if (view != null)
                view.message(R.string.string_17);
        }
    }

    @Override
    public void parseCropedImage(int requestCode, int resultCode, Intent data, boolean isFront) {
        try {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Bitmap bitmapToUpload;
                if (isFront) {
                    String profilePath1 = UriUtil.getPath(context, result.getUri());
                    if (profilePath1 != null) {

                        bitmapToUpload = BitmapFactory.decodeFile(profilePath1);
                        Bitmap bitmap = bitmapToUpload;
                        if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
                            if (view != null)
                                view.setFrontImage(bitmap, profilePath1);
                            frontImage = profilePath1;
                            amazonUploadDoc(frontImage);
                        } else {
                            if (view != null)
                                view.message(R.string.string_19);
                        }
                    } else {
                        if (view != null)
                            view.message(R.string.string_19);
                    }
                } else {
                    backImage = UriUtil.getPath(context, result.getUri());
                    if (backImage != null) {
                        bitmapToUpload = BitmapFactory.decodeFile(backImage);
                        if (bitmapToUpload != null && bitmapToUpload.getWidth() > 0 && bitmapToUpload.getHeight() > 0) {
                            if (view != null)
                                view.setBackImage(bitmapToUpload, backImage);
                            amazonUploadBackDoc(backImage);
                        } else {
                            if (view != null)
                                view.message(R.string.string_19);
                        }
                    } else {
                        if (view != null)
                            view.message(R.string.string_19);
                    }
                }
            }
        } catch (OutOfMemoryError e) {
            //out of mem try again
            if (isFront) {
                frontImage = null;
            } else {
                backImage = null;
            }
            if (view != null)
                view.message(R.string.string_15);
        }
    }

    private void approve() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 1);
        service.approveVerification(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(Response<Error> response) {
                        if (response.code() == 200) {
                            if (view != null)
                                view.moveNext();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
