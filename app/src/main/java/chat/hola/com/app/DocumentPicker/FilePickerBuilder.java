package chat.hola.com.app.DocumentPicker;

/**
 * Created by moda on 22/08/17.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.DrawableRes;
import androidx.fragment.app.Fragment;



import java.util.ArrayList;

import chat.hola.com.app.DocumentPicker.Models.FileType;
import chat.hola.com.app.DocumentPicker.Utils.Orientation;


public class FilePickerBuilder {

    private final Bundle mPickerOptionsBundle;

    public FilePickerBuilder()
    {
        mPickerOptionsBundle = new Bundle();
    }

    public static FilePickerBuilder getInstance()
    {
        return new FilePickerBuilder();
    }

    public FilePickerBuilder setMaxCount(int maxCount)
    {
        PickerManager.getInstance().setMaxCount(maxCount);
        return this;
    }

    public FilePickerBuilder setActivityTheme(int theme)
    {
        PickerManager.getInstance().setTheme(theme);
        return this;
    }

    public FilePickerBuilder setSelectedFiles(ArrayList<String> selectedPhotos)
    {
        mPickerOptionsBundle.putStringArrayList(FilePickerConst.KEY_SELECTED_MEDIA, selectedPhotos);
        return this;
    }

    public FilePickerBuilder enableVideoPicker(boolean status)
    {
        PickerManager.getInstance().setShowVideos(status);
        return this;
    }

    public FilePickerBuilder enableImagePicker(boolean status)
    {
        PickerManager.getInstance().setShowImages(status);
        return this;
    }

    public FilePickerBuilder setCameraPlaceholder(@DrawableRes int drawable)
    {
        PickerManager.getInstance().setCameraDrawable(drawable);
        return this;
    }

    public FilePickerBuilder showGifs(boolean status)
    {
        PickerManager.getInstance().setShowGif(status);
        return this;
    }

    public FilePickerBuilder showFolderView(boolean status)
    {
        PickerManager.getInstance().setShowFolderView(status);
        return this;
    }

    public FilePickerBuilder enableDocSupport(boolean status)
    {
        PickerManager.getInstance().setDocSupport(status);
        return this;
    }

    public FilePickerBuilder enableCameraSupport(boolean status)
    {
        PickerManager.getInstance().setEnableCamera(status);
        return this;
    }

    public FilePickerBuilder withOrientation(Orientation orientation)
    {
        PickerManager.getInstance().setOrientation(orientation);
        return this;
    }

    public FilePickerBuilder addFileSupport(String title, String[] extensions, @DrawableRes int drawable)
    {
        PickerManager.getInstance().addFileType(new FileType(title,extensions,drawable));
        return this;
    }

    public FilePickerBuilder addFileSupport(String title, String[] extensions)
    {
        PickerManager.getInstance().addFileType(new FileType(title,extensions,0));
        return this;
    }

    public void pickPhoto(Activity context)
    {
        mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE,FilePickerConst.MEDIA_PICKER);
        start(context,FilePickerConst.MEDIA_PICKER);
    }

    public void pickPhoto(Fragment context)
    {
        mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE,FilePickerConst.MEDIA_PICKER);
        start(context,FilePickerConst.MEDIA_PICKER);
    }

    public void pickFile(Activity context)
    {
        mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE,FilePickerConst.DOC_PICKER);
        start(context,FilePickerConst.DOC_PICKER);
    }

    public void pickFile(Fragment context)
    {
        mPickerOptionsBundle.putInt(FilePickerConst.EXTRA_PICKER_TYPE,FilePickerConst.DOC_PICKER);
        start(context,FilePickerConst.DOC_PICKER);
    }

    private void start(Activity context, int pickerType)
    {
        PickerManager.getInstance().setProviderAuthorities(context.getApplicationContext().getPackageName() + ".droidninja.filepicker.provider");

        Intent intent = new Intent(context, FilePickerActivity.class);
        intent.putExtras(mPickerOptionsBundle);

        if(pickerType==FilePickerConst.MEDIA_PICKER)
            context.startActivityForResult(intent,FilePickerConst.REQUEST_CODE_PHOTO);
        else
            context.startActivityForResult(intent,FilePickerConst.REQUEST_CODE_DOC);
    }

    private void start(Fragment fragment, int pickerType)
    {
        PickerManager.getInstance().setProviderAuthorities(fragment.getContext().getApplicationContext().getPackageName() + ".droidninja.filepicker.provider");

        Intent intent = new Intent(fragment.getActivity(), FilePickerActivity.class);
        intent.putExtras(mPickerOptionsBundle);
        if(pickerType==FilePickerConst.MEDIA_PICKER)
            fragment.startActivityForResult(intent,FilePickerConst.REQUEST_CODE_PHOTO);
        else
            fragment.startActivityForResult(intent,FilePickerConst.REQUEST_CODE_DOC);
    }

}