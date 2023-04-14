package chat.hola.com.app.Dialog;

import android.content.Context;
import android.content.DialogInterface;

import com.ezcall.android.R;

import javax.inject.Inject;

/**
 * Created by ankit on 17/3/18.
 */

public class ImageSourcePicker {

    private Context context;
    private androidx.appcompat.app.AlertDialog.Builder builder;
    private OnSelectImageSource callback = null;
    private OnSelectImage callbackOnSelectImage = null;
    private boolean isCamera = true;

    public interface OnSelectImageSource {

        void onCamera();

        void onGallary();

        void onCancel();
    }

    public interface OnSelectImage extends OnSelectImageSource {
//        void onRemove();
    }


    public void setOnSelectImageSource(OnSelectImageSource callback) {
        this.callback = callback;
    }

    public void setOnSelectImage(OnSelectImage callback) {
        this.callbackOnSelectImage = callback;
    }

    @Inject
    public ImageSourcePicker(Context context, boolean isCamera, boolean remove) {
        this.context = context;
        this.isCamera = isCamera;
        if (remove)
        initWithRemove();
        else
        init();
    }

    private void initWithRemove() {
        builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle(R.string.string_255);
        // builder.setIcon(R.drawable.orca_attach_camera_pressed);
        builder.setItems((isCamera) ? new CharSequence[]{context.getString(R.string.string_1021),
                        context.getString(R.string.string_1022), context.getString(R.string.cancel)}
                        : new CharSequence[]{context.getString(R.string.string_1021),
                        context.getString(R.string.cancel)},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                if (callbackOnSelectImage != null)
                                    callbackOnSelectImage.onGallary();
                                //checkReadImage();
                                dialog.dismiss();
                                break;
                            case 1:
                                //checkCameraPermissionImage();
                                if (isCamera) {
                                    if (callbackOnSelectImage != null)
                                        callbackOnSelectImage.onCamera();
                                } else {
                                    dialog.dismiss();
                                }
                                break;
                            case 2:
                                /* Do Nothing here */
                                if (callbackOnSelectImage != null)
                                    callbackOnSelectImage.onCancel();
                                dialog.dismiss();
                                break;
                        }
                    }
                });
    }

    private void init() {
        builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle(R.string.string_255);
        // builder.setIcon(R.drawable.orca_attach_camera_pressed);
        builder.setItems((isCamera) ? new CharSequence[]{context.getString(R.string.string_1021),
                        context.getString(R.string.string_1022), context.getString(R.string.cancel)}
                        : new CharSequence[]{context.getString(R.string.string_1021),
                        context.getString(R.string.cancel)},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                if (callback != null)
                                    callback.onGallary();
                                //checkReadImage();
                                dialog.dismiss();
                                break;
                            case 1:
                                //checkCameraPermissionImage();
                                if (isCamera) {
                                    if (callback != null)
                                        callback.onCamera();
                                } else {
                                    dialog.dismiss();
                                }
                                break;
                            case 2:
                                /* Do Nothing here */
                                if (callback != null)
                                    callback.onCancel();
                                dialog.dismiss();
                                break;
                        }
                    }
                });

    }

    public void show() {
        if (builder != null) {
            androidx.appcompat.app.AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
