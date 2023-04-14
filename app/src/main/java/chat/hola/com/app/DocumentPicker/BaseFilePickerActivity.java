package chat.hola.com.app.DocumentPicker;

/**
 * Created by moda on 22/08/17.
 */

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import chat.hola.com.app.DocumentPicker.Utils.Orientation;


public abstract class BaseFilePickerActivity extends AppCompatActivity {

    protected void onCreate(@Nullable Bundle savedInstanceState, @LayoutRes int layout) {
        super.onCreate(savedInstanceState);
        setTheme(PickerManager.getInstance().getTheme());
        setContentView(layout);

        //set orientation
        Orientation orientation = PickerManager.getInstance().getOrientation();
        if(orientation==Orientation.PORTRAIT_ONLY)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        else if(orientation==Orientation.LANDSCAPE_ONLY)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        initView();
    }

    protected abstract void initView();
}