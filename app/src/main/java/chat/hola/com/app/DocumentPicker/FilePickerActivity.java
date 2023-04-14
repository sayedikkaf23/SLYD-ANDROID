package chat.hola.com.app.DocumentPicker;

/**
 * Created by moda on 22/08/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;



import java.util.ArrayList;

import chat.hola.com.app.DocumentPicker.Fragments.DocFragment;
import chat.hola.com.app.DocumentPicker.Fragments.DocPickerFragment;
import chat.hola.com.app.DocumentPicker.Fragments.MediaPickerFragment;
import chat.hola.com.app.DocumentPicker.Fragments.PhotoPickerFragmentListener;
import chat.hola.com.app.DocumentPicker.Utils.FragmentUtil;
import com.ezcall.android.R;

public class FilePickerActivity extends BaseFilePickerActivity implements
        PhotoPickerFragmentListener,
        DocFragment.DocFragmentListener,
        DocPickerFragment.DocPickerFragmentListener,
        MediaPickerFragment.MediaPickerFragmentListener {

    private static final String TAG = FilePickerActivity.class.getSimpleName();
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_file_picker);
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            ArrayList<String> selectedPaths = intent.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);
            type = intent.getIntExtra(FilePickerConst.EXTRA_PICKER_TYPE, FilePickerConst.MEDIA_PICKER);

            if (selectedPaths != null) {

                if (PickerManager.getInstance().getMaxCount() == 1) {
                    selectedPaths.clear();
                }

                if (type == FilePickerConst.MEDIA_PICKER) {
                    PickerManager.getInstance().add(selectedPaths, FilePickerConst.FILE_TYPE_MEDIA);
                } else {


                    PickerManager.getInstance().addDocumentWithMimeType(selectedPaths, FilePickerConst.FILE_TYPE_DOCUMENT, null);
                    //PickerManager.getInstance().add(selectedPaths, FilePickerConst.FILE_TYPE_DOCUMENT);
                }
            } else
                selectedPaths = new ArrayList<>();

//            setToolbarTitle(PickerManager.getInstance().getCurrentCount());
            setToolbarTitle();
            openSpecificFragment(type, selectedPaths);
        }
    }

    private void openSpecificFragment(int type, @Nullable ArrayList<String> selectedPaths) {
        if (type == FilePickerConst.MEDIA_PICKER) {
            MediaPickerFragment photoFragment = MediaPickerFragment.newInstance();
            try{
            FragmentUtil.addFragment(this, R.id.container, photoFragment);}catch(IllegalStateException e){e.printStackTrace();}
        } else {
            if (PickerManager.getInstance().isDocSupport())
                PickerManager.getInstance().addDocTypes();

            DocPickerFragment photoFragment = DocPickerFragment.newInstance(selectedPaths);
            try{
            FragmentUtil.addFragment(this, R.id.container, photoFragment);}catch(IllegalStateException e){e.printStackTrace();}
        }
    }

    //    private void setToolbarTitle(int count) {
    private void setToolbarTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setTitle("");

            actionBar.setBackgroundDrawable(getDrawable(R.color.base_color));

            if (type == FilePickerConst.MEDIA_PICKER)
                actionBar.setTitle(R.string.select_photo_text);
            else
                actionBar.setTitle(R.string.select_doc_text);
        }
//            if (PickerManager.getInstance().getMaxCount() > 1) {
//                actionBar.setTitle(String.format(getString(R.string.attachments_title_text), count, PickerManager.getInstance().getMaxCount()));
//            } else {
//                if (type == FilePickerConst.MEDIA_PICKER)
//                    actionBar.setTitle(R.string.select_photo_text);
//                else
//                    actionBar.setTitle(R.string.select_doc_text);
//            }
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.picker_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_done) {
            if (type == FilePickerConst.MEDIA_PICKER)
                returnData(PickerManager.getInstance().getSelectedPhotos(), null);
            else
                returnData(PickerManager.getInstance().getSelectedFiles(), PickerManager.getInstance().getSelectedMimeTypes());

            return true;
        } else if (i == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_MEDIA_DETAIL:
                if (resultCode == Activity.RESULT_OK) {
                    if (type == FilePickerConst.MEDIA_PICKER)
                        returnData(PickerManager.getInstance().getSelectedPhotos(), null);
                    else
                        returnData(PickerManager.getInstance().getSelectedFiles(), PickerManager.getInstance().getSelectedMimeTypes());
                }


//                else {
//                    setToolbarTitle(PickerManager.getInstance().getCurrentCount());
//                }
                break;
        }
    }

    private void returnData(ArrayList<String> paths, ArrayList<String> mimeTypes) {
        Intent intent = new Intent();
        if (type == FilePickerConst.MEDIA_PICKER)
            intent.putStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA, paths);
        else {
            intent.putStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS, paths);

            intent.putStringArrayListExtra(FilePickerConst.SELECTED_DOCS_MIME_TYPES, mimeTypes);


        }
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onItemSelected() {
        //  setToolbarTitle(PickerManager.getInstance().getCurrentCount());

        if (PickerManager.getInstance().getMaxCount() == 1) {

            if (type == FilePickerConst.MEDIA_PICKER) {


                returnData(PickerManager.getInstance().getSelectedPhotos(),null);
            } else {

                returnData(PickerManager.getInstance().getSelectedFiles(),PickerManager.getInstance().getSelectedMimeTypes());
            }

        }

    }
}