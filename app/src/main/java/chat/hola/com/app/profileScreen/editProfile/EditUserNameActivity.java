package chat.hola.com.app.profileScreen.editProfile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.R;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.CallApiServiceGenerator;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.profileScreen.editProfile.model.EditProfileResponse;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUserNameActivity extends DaggerAppCompatActivity {

    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.tvChangeUserName)
    TextView tvChangeUserName;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvCenterCategoryName)
    TextView tvHeaderTitle;
    @BindView(R.id.tiUserName)
    TextInputLayout tiUserName;

    String userName;

    @Inject
    HowdooService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_name);
        ButterKnife.bind(this);
        tvHeaderTitle.setText(getString(R.string.changeUserName));
        getData();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvChangeUserName.setVisibility(View.GONE);

        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userName = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!userName.isEmpty() && userName.length() > 3)
                    checkUserName(userName);
            }
        });

        tvChangeUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tvChangeUserName.getText().toString().equalsIgnoreCase("")) {
                    String editUserName = etUserName.getText().toString();
                    Intent intent = new Intent();
                    intent.putExtra(Constants.PROFILE_USERNAME, editUserName);
                    setResult(RESULT_OK, intent);
                    save();
                } else {
                    Toast.makeText(EditUserNameActivity.this, getString(R.string.userNameEmpty), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //getting first Username from editProfileActivty
    private void getData() {
        userName = getIntent().getStringExtra(Constants.PROFILE_USERNAME);
        etUserName.setText(userName);
        etUserName.setSelection(userName.length());
    }

    private void save() {
        Map<String, Object> map = new HashMap<>();
        map.put("userName", etUserName.getText().toString().trim());

        service.editProfile(AppController.getInstance().getApiToken(), "en", map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<EditProfileResponse>>() {
                    @Override
                    public void onNext(Response<EditProfileResponse> response) {
                        if (response.code() == 200) {
                            Toast.makeText(EditUserNameActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                            AppController.getInstance().setUserName(etUserName.getText().toString().trim());
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

    private void checkUserName(String userName) {
        service.verifyIsUserNameRegistered(Utilities.getThings(),
                etUserName.getText().toString().trim(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        if (response.code() == 200) {
                            tiUserName.setError("Username is already in use");
                        } else if (response.code() == 204) {
                            tvChangeUserName.setVisibility(View.VISIBLE);
                            tiUserName.setError(null);
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
