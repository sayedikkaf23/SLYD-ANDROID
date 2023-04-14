package chat.hola.com.app.mystories;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.AppController;
import chat.hola.com.app.DublyCamera.deepar.DeeparFiltersTabCameraActivity;
import chat.hola.com.app.home.stories.model.StoryPost;
import chat.hola.com.app.mystories.adapter.MyStoriesAdapter;
import dagger.android.support.DaggerAppCompatActivity;

public class MyStoriesActivity extends DaggerAppCompatActivity implements MyStoriesPresenterImpl.MyStoriesPresenterView, MyStoriesAdapter.DeleteStoryListerner {

    List<StoryPost> myStoriesList = new ArrayList<>();
    MyStoriesAdapter myStoriesAdapter;
    @Inject MyStoriesPresenterImpl.MyStoriesPresent present;

    @BindView(R.id.rV_myStories) RecyclerView rV_myStories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stories);
        ButterKnife.bind(this);

        getIntentData();

        intializeRecycleView();

    }

    private void getIntentData(){
        myStoriesList = (List<StoryPost>) getIntent().getSerializableExtra("myStories");
    }

    private void intializeRecycleView(){
        rV_myStories.setLayoutManager(new LinearLayoutManager(this));
        myStoriesAdapter = new MyStoriesAdapter(this,myStoriesList);
        rV_myStories.setAdapter(myStoriesAdapter);
    }

    @OnClick(R.id.iV_back)
    public void BackButton(){
        onBackPressed();
    }

    @Override
    public void showMessage(String msg, int msgId) {

    }

    @Override
    public void sessionExpired() {

    }

    @Override
    public void isInternetAvailable(boolean flag) {

    }

    @Override
    public void onStoriesDeleteSuccess(int position) {
        myStoriesList.remove(position);
        myStoriesAdapter.notifyDataSetChanged();

        JSONObject obj = new JSONObject();
        try {
            obj.put("eventName", "myStoryUpdate");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AppController.getBus().post(obj);
    }

    @Override
    public void userBlocked() {

    }

    @Override
    public void reload() {

    }

    @Override
    public void onDelete(int position) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.are_you_sure_to_delete_this_story)
                .setPositiveButton(R.string.Delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        present.onToDeleteStory(myStoriesList.get(position).getStoryId(),position);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        builder.create();

        builder.show();




    }

    @OnClick(R.id.fab)
    public void addStories() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            startActivity(new Intent(MyStoriesActivity.this, DeeparFiltersTabCameraActivity.class).putExtra("call", "story"));
                            finish();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
        //imageSourcePicker.show();
    }
}
