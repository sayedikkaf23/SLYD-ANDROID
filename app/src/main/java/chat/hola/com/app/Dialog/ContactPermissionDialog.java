package chat.hola.com.app.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ezcall.android.R;

import org.jetbrains.annotations.NotNull;

public class ContactPermissionDialog extends Dialog {

    public ContactPermissionDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        setContentView(R.layout.dialog_contact);

        TextView tvPrivacyPolicy = findViewById(R.id.tvPrivacyPolicy);

        SpannableString ss = new SpannableString(tvPrivacyPolicy.getText().toString());

        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                String url = context.getString(R.string.privacyPolicyUrl);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }

            @Override
            public void updateDrawState(@NotNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(context.getResources().getColor(R.color.blue_button));
                ds.setUnderlineText(true);
                ds.setFakeBoldText(true);
            }
        }, 17, tvPrivacyPolicy.getText().toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvPrivacyPolicy.setText(ss);
        tvPrivacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());

        Button btnNotNow = findViewById(R.id.btnNotNow);
        btnNotNow.setOnClickListener(v -> dismiss());
    }
}
