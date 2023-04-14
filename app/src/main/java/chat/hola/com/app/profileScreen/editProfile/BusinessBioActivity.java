package chat.hola.com.app.profileScreen.editProfile;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ezcall.android.R;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.TypefaceManager;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>BusinessBioActivity</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 20 September 2019
 */
public class BusinessBioActivity extends DaggerAppCompatActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvSave)
    TextView tvSave;
    @BindView(R.id.etBio)
    EditText etBio;

    @Inject
    TypefaceManager typefaceManager;
    private InputMethodManager imm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_businessbio);
        ButterKnife.bind(this);

        String bio = getIntent().getStringExtra("bio");

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        title.setTypeface(typefaceManager.getSemiboldFont());
        tvSave.setTypeface(typefaceManager.getSemiboldFont());
        etBio.setTypeface(typefaceManager.getRegularFont());

        if (bio != null)
            etBio.setText(bio);
        showKeyboard();
    }

    @OnClick(R.id.tvSave)
    public void save() {
        hideKeyBoard();
        Intent intent = new Intent();
        intent.putExtra("bio", etBio.getText().toString().trim());
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.ibBack)
    public void back(){
        hideKeyBoard();
        super.onBackPressed();
    }

    private void showKeyboard() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            etBio.requestFocus(etBio.getText().length());
            if (imm != null) {
                imm.showSoftInput(etBio, InputMethodManager.SHOW_FORCED);
            }
        }, 200);
    }

    private void hideKeyBoard() {
        if (imm != null)
            imm.hideSoftInputFromWindow(etBio.getWindowToken(), 0);
    }
}
