package chat.hola.com.app.collections.edit_collection;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ezcall.android.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.collections.add_to_collection.AddToCollectionActivity;
import chat.hola.com.app.collections.collection.CollectionActivity;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>{@link EditCollectionActivity}</h1>
 * <p1>Used to Edit a Collection</p1>
 * @author : 3Embed
 * @since : 22/8/19
 */

public class EditCollectionActivity extends DaggerAppCompatActivity implements EditCollectionContract.View {

    @BindView(R.id.iVcoverImage)
    ImageView iVcoverImage;
    @BindView(R.id.iV_done)
    ImageView iV_done;
    @BindView(R.id.eT_name)
    EditText eT_name;
    @BindView(R.id.tV_delete)
    TextView tV_delete;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private Unbinder unbinder;
    private String collectionId = "", collectionName = "", coverImage = "";

    @Inject
    EditCollectionContract.Presenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_collection);
        unbinder = ButterKnife.bind(this);
        initData();
        initViews();
    }

    /**
     * Initialize data from intent.
     */
    private void initData() {
        collectionId = getIntent().getStringExtra("collectionId");
        collectionName = getIntent().getStringExtra("collectionName");
        coverImage = getIntent().getStringExtra("coverImage");
    }

    /**
     * Initialize views
     */
    public void initViews() {
        if (coverImage != null) {
            Glide.with(this)
                    .load(coverImage)
                    .centerCrop()
                    .placeholder(R.color.colorBonJour)
                    .into(iVcoverImage);
        }

        if (collectionName != null)
            eT_name.setText(collectionName);
    }

    @OnClick(R.id.iV_done)
    public void done() {

        if (!eT_name.getText().toString().trim().isEmpty()) {
            presenter.editCollection(collectionId, eT_name.getText().toString().trim(), coverImage);
        }else {
            Toast.makeText(this,getString(R.string.signUpEnterValidName),Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick({R.id.iV_back})
    public void back() {
        onBackPressed();
    }

    @OnClick(R.id.tV_delete)
    public void delete() {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_collection_que)
                .setMessage(R.string.delete_collection_msg)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        presenter.deleteCollection(collectionId);
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

    @OnClick(R.id.ll_changeImage)
    public void changeCoverClick() {
        Intent intent = new Intent(this, CollectionActivity.class);
        intent.putExtra("isAllPost", false);
        intent.putExtra("isImageSelection", true);
        intent.putExtra("collectionId", collectionId);
        intent.putExtra("collectionName", collectionName);
        intent.putExtra("coverImage", coverImage);
        startActivityForResult(intent, 2001);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 2001 && data != null) {
            coverImage = data.getStringExtra("coverImage");
            if (coverImage != null) {
                Glide.with(this)
                        .load(coverImage)
                        .centerCrop()
                        .placeholder(R.color.colorBonJour)
                        .into(iVcoverImage);
            }
        }
    }

    @Override
    public void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void collectionEdited() {
        back();
    }

    @Override
    public void deletedCollection() {
        back();
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
    public void userBlocked() {

    }

    @Override
    public void reload() {

    }
}
