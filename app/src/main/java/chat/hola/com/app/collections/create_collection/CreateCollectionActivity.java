package chat.hola.com.app.collections.create_collection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ezcall.android.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.collections.add_to_collection.AddToCollectionActivity;
import chat.hola.com.app.collections.model.CreateCollectionResponse;
import dagger.android.support.DaggerAppCompatActivity;
/**
 * <h1>{@link CreateCollectionActivity}</h1>
 * <p1>Used to Create a New collection</p1>
 * @author : 3Embed
 * @since : 20/8/19
 */
public class CreateCollectionActivity extends DaggerAppCompatActivity implements CreateCollectionContract.View{

    @BindView(R.id.eT_name)
    EditText eT_name;
    private Unbinder unbinder;
    @Inject
    CreateCollectionContract.Presenter presenter;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_collection);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unbinder!=null)
            unbinder.unbind();
    }

    @OnClick(R.id.iV_back)
    public void back(){
        onBackPressed();
    }

    @OnClick(R.id.tV_next)
    public void next(){
        if(!eT_name.getText().toString().trim().isEmpty()){

            // here pass to addTOCollection activity for add post in this collection

            Intent intent = new Intent(CreateCollectionActivity.this, AddToCollectionActivity.class);
            intent.putExtra("isNew", true);
            intent.putExtra("collectionName", eT_name.getText().toString().trim());
            startActivityForResult(intent, 101);

            //presenter.createCollection(eT_name.getText().toString().trim());
        }else {
            Toast.makeText(this,getString(R.string.signUpEnterValidName),Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public void createdSuccess(CreateCollectionResponse body) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 101)
            onBackPressed();
    }

    @Override
    public void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
