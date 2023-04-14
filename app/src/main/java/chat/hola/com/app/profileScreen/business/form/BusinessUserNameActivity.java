package chat.hola.com.app.profileScreen.business.form;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.R;

import butterknife.BindView;
import chat.hola.com.app.Utilities.Constants;


public class BusinessUserNameActivity extends AppCompatActivity {
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.tvChangeUserName)
    TextView tvChangeUserName;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvCenterCategoryName)
    TextView tvHeaderTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_user_name);
        tvHeaderTitle.setText(getString(R.string.changeUserName));
        getData();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvChangeUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tvChangeUserName.getText().toString().equalsIgnoreCase("")) {
                    String editUserName = etUserName.getText().toString();
                    Intent intent = new Intent();
                    intent.putExtra(Constants.BUSINESS_USERNAME, editUserName);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(BusinessUserNameActivity.this, getString(R.string.userNameEmpty), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //getting first Username from editProfileActivty
    private void getData() {
        String userName = getIntent().getStringExtra(Constants.BUSINESS_USERNAME);
        etUserName.setText(userName);
        etUserName.setSelection(userName.length());
    }
    }