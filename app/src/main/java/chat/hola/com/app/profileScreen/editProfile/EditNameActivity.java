package chat.hola.com.app.profileScreen.editProfile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.R;
import com.kotlintestgradle.CallDisconnectType;

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
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.profileScreen.editProfile.editDetail.EditDetailActivity;
import chat.hola.com.app.profileScreen.editProfile.model.EditProfileResponse;
import dagger.android.support.DaggerAppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditNameActivity extends DaggerAppCompatActivity {

    private static final String TAG = EditNameActivity.class.getSimpleName();
    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastname)
    EditText etLastname;
    @BindView(R.id.tv_changename)
    TextView tv_changename;
    @BindView(R.id.ivBack)
    ImageView ivBack;

    private String name,firstName,lastName;

    @Inject
    HowdooService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);
        ButterKnife.bind(this);
        getData();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_changename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etFirstName.getText().toString().equalsIgnoreCase("")) {
                    String editName = etFirstName.getText().toString() + " " + etLastname.getText().toString();
                    Intent intent = new Intent();
                    intent.putExtra("Name", editName);
                    intent.putExtra("FirstName", etFirstName.getText().toString());
                    intent.putExtra("LastName", etLastname.getText().toString());
                    setResult(RESULT_OK, intent);
                    save();
                } else {
                    Toast.makeText(getApplicationContext(), "Firstname can't be empty", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void save() {
        Map<String, Object> map = new HashMap<>();
        map.put("firstName", etFirstName.getText().toString());
        map.put("lastName", etLastname.getText().toString());
        service.editProfile(AppController.getInstance().getApiToken(), "en", map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<EditProfileResponse>>() {
                    @Override
                    public void onNext(Response<EditProfileResponse> response) {
                        if (response.code() == 200) {
                            Toast.makeText(EditNameActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
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

    //getting first name and lastname from editProfileActivty
    private void getData() {
        name = getIntent().getStringExtra("name");
        firstName = getIntent().getStringExtra("FirstName");
        lastName = getIntent().getStringExtra("LastName");
        etLastname.setText(lastName);
        etFirstName.setText(firstName);
        etFirstName.requestFocus();
        etFirstName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        etFirstName.setSelection(etFirstName.getText().length());
        etFirstName.setSelection(etFirstName.getText().length());
        /*if (name != null && name.length() > 2) {
            String[] fullName = name.split("\\s+");
            etFirstName.setText(fullName[0]);
            String lastName = fullName.length > 1 ? fullName[1].trim() : "";
            etLastname.setText(lastName);
            etFirstName.requestFocus();
            etFirstName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            etFirstName.setSelection(etFirstName.getText().length());
            etFirstName.setSelection(etFirstName.getText().length());
        }*/

    }
}
