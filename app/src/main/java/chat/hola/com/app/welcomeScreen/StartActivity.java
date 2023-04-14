package chat.hola.com.app.welcomeScreen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.ezcall.android.R;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.authentication.login.LoginActivity;


public class StartActivity extends AppCompatActivity {

    @BindView(R.id.tvUrl)
    TextView tvUrl;
    @BindView(R.id.btnAgree)
    Button btnAgree;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);

        SpannableString ss = new SpannableString(tvUrl.getText().toString());

        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String url = getString(R.string.privacyPolicyUrl);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }

            @Override
            public void updateDrawState(@NotNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.whitist_grey));
                ds.setUnderlineText(true);
                ds.setFakeBoldText(true);
            }
        }, 9, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String url = getString(R.string.termsUrl);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }

            @Override
            public void updateDrawState(@NotNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.whitist_grey));
                ds.setUnderlineText(true);
                ds.setFakeBoldText(true);
            }
        }, 62, tvUrl.getText().toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvUrl.setText(ss);
        tvUrl.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @OnClick(R.id.btnAgree)
    public void agree() {
        Intent intent2 = new Intent(this, LoginActivity.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent2);
        finish();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
