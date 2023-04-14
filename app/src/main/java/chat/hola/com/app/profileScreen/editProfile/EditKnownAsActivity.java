package chat.hola.com.app.profileScreen.editProfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.R;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.profileScreen.editProfile.model.EditProfileResponse;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class EditKnownAsActivity extends DaggerAppCompatActivity {

    @BindView(R.id.etBio)
    EditText etBio;
    @BindView(R.id.tvChangeBio)
    TextView tvChangeBio;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvCenterCategoryName)
    TextView tvHeaderTitle;

    @Inject
    HowdooService service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_known_as);

        ButterKnife.bind(this);
        tvHeaderTitle.setText(getString(R.string.known_as));
        getData();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvChangeBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editedBio = etBio.getText().toString();
                Intent intent = new Intent();
                intent.putExtra(Constants.KNOWN_AS, editedBio);
                setResult(RESULT_OK, intent);
                save();
            }
        });
    }

    //getting bio from editProfileActivty
    private void getData() {
        String bio = getIntent().getStringExtra(Constants.KNOWN_AS);
        etBio.setText(bio);
        etBio.setSelection(etBio.length());
    }

    private void save() {
        Map<String, Object> map = new HashMap<>();
        map.put("starUserKnownBy", etBio.getText().toString().trim());

        service.editProfile(AppController.getInstance().getApiToken(), "en", map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<EditProfileResponse>>() {
                    @Override
                    public void onNext(Response<EditProfileResponse> response) {
                        if (response.code() == 200) {
                            Toast.makeText(EditKnownAsActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}

