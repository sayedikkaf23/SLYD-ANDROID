package chat.hola.com.app.referral_code;

/**
 * <h1>{@link chat.hola.com.app.referral_code.ReferralCodeActivity}</h1>
 * <p>This is an activity class used to show user referral code.</p>
 */

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.ezcall.android.R;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.profileScreen.model.Data;

public class ReferralCodeActivity extends AppCompatActivity {

    @BindView(R.id.tV_code)
    TextView tV_code;
    @BindView(R.id.tV_copy)
    TextView tV_copy;
    @BindView(R.id.rL_share)
    RelativeLayout rL_share;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_code);
        unbinder = ButterKnife.bind(this);

        /**
         * <P>User profile data receiving from setting activity</P>
         * {@link chat.hola.com.app.settings.SettingsActivity}*/

        Data profileData = (Data) getIntent().getSerializableExtra("profileData");

        tV_code.setText(profileData.getReferralCode());

    }

    @OnClick(R.id.rL_share)
    public void share() {
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.inviteMsg1) + " " + tV_code.getText().toString() + " " + getString(R.string.inviteMsg2) + " " + getString(R.string.howdooPlayStore));
//        intent.setType("text/plain");
//        Intent chooser = Intent.createChooser(intent, getString(R.string.selectApp));
//        startActivity(chooser);
        new Thread(() -> {
            String url = "https://ezcallapp.page.link/refer/" + tV_code.getText().toString();
           FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLongLink(Uri.parse("https://ezcallapp.page.link?link=" + url + "&apn=com.star.chat"))
                    .buildShortDynamicLink()
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Uri shortLink = task.getResult().getShortLink();
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                            intent.setType("text/plain");
                            Intent chooser = Intent.createChooser(intent, getString(R.string.selectApp));
                            startActivity(chooser);
                        }
//                        hideProgressDialog();
                    });
        }).start();
    }

    @OnClick(R.id.tV_copy)
    public void copy() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("copied", tV_code.getText().toString().trim());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.iV_back)
    public void back() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (unbinder != null)
            unbinder.unbind();
        super.onDestroy();
    }
}
