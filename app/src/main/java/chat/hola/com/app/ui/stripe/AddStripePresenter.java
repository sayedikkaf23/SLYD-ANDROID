package chat.hola.com.app.ui.stripe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.android.policy.TimeWindow;
import com.ezcall.android.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.ImageFilePath;
import chat.hola.com.app.Utilities.UriUtil;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.CountryResponse;
import chat.hola.com.app.models.Error;
import chat.hola.com.app.ui.kyc.KycActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
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
public class AddStripePresenter implements AddStripeContract.Presenter {

    private AddStripeContract.View view;

    @Inject
    Context context;
    @Inject
    HowdooService service;
    @Inject
    SessionManager sessionManager;

    private String picturePath = null;
    private String documentImage = null;
    private Uri imageUri;

    @Inject
    public AddStripePresenter() {

    }

    @Override
    public void attach(AddStripeContract.View view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
    }


    private void amazonUploadDoc(String url) {
        view.showLoader();
        MediaManager.get()
                .upload(url)
                .option("folder", "stripe")
                .option(Constants.Post.PUBLIC_ID, sessionManager.getUserId())
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
                        documentImage = String.valueOf(resultData.get("url"));
                        view.hideLoader();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        view.hideLoader();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }
                })
                .constrain(TimeWindow.immediate())
                .dispatch();
    }

    @Override
    public void addAccount(Map<String, Object> params) {
//        params.put("document", "https://superadmin.shoppd.net/theme/icon/delivxlogoflexy.png");
        if (documentImage == null) {
            view.message("Please upload document");
            return;
        }
        params.put("document", documentImage);
        service.addStripAccount(AppController.getInstance().getApiToken(), Constants.LANGUAGE, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(Response<Error> response) {
                        view.hideLoader();
                        Gson gson = new GsonBuilder().create();
                        Error error = new Error();
                        try {
                            if (response.errorBody() != null) {
                                error = gson.fromJson(response.errorBody().string(), Error.class);
                                if (view != null && error != null)
                                    view.message(error.getMessage());
                            }
                        } catch (IOException ignored) {
                        }
                        if (response.code() == 200) {
                            view.success();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoader();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


//        File file = new File(documentImage);
//        if (file.exists())
//            amazonUploadDoc(amazonS3, document, file, params);

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
                        view.launchCropImage(data.getData());
                    } else {
                        //image can't be selected try another
                        view.message(R.string.string_31);
                    }
                } else {
                    //image can't be selected try another
                    view.message(R.string.string_31);
                }
            } catch (OutOfMemoryError e) {
                //out of mem try again
                view.message(R.string.string_15);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //image selection canceled.
            view.message(R.string.string_16);
        } else {
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
                    view.launchCropImage(imageUri);
                } else {
                    //failed to capture image.
                    picturePath = null;
                    view.message(R.string.string_17);
                }
            } catch (OutOfMemoryError e) {
                //out of mem try again
                picturePath = null;
                view.message(R.string.string_15);
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            picturePath = null;
            //img capture canceled.
            view.message(R.string.string_18);
        } else {
            //sorry failed to capture
            picturePath = null;
            view.message(R.string.string_17);
        }
    }

    @Override
    public void parseCropedImage(int requestCode, int resultCode, Intent data) {
        try {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Bitmap bitmapToUpload;
                String docPath = UriUtil.getPath(context, result.getUri());
                if (docPath != null) {

                    bitmapToUpload = BitmapFactory.decodeFile(docPath);
                    Bitmap bitmap = bitmapToUpload;
                    if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
                        view.setDocumentImage(bitmap, docPath);
//                        documentImage = docPath;
                        amazonUploadDoc(docPath);
                    } else {
                        view.message(R.string.string_19);
                    }
                } else {
                    view.message(R.string.string_19);
                }
            }
        } catch (OutOfMemoryError e) {
            //out of mem try again
            documentImage = null;
            view.message(R.string.string_15);
        }
    }

    @Override
    public void getCountry() {
//        view.showLoader();
        service.getStripeCountry(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<CountryResponse>>() {
                    @Override
                    public void onNext(Response<CountryResponse> response) {
                        view.hideLoader();
                        if (response.code() == 200) {
                            view.setCountries(response.body().getCountries());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoader();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
