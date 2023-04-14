package chat.hola.com.app.send_star_packet;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Utilities.DecimalDigitsInputFilter;
import chat.hola.com.app.manager.session.SessionManager;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.ezcall.android.R;

public class SendStarPacketActivity extends AppCompatActivity {

    private String receiverImage, receiverName, receiverUid, receiverIdentifier;
    @BindView(R.id.eT_amount)
    EditText et_amount;
    @BindView(R.id.ll_mainView)
    LinearLayout ll_mainView;
    private Unbinder unbinder;
    private SessionManager sessionManager;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_star_packet);
        unbinder = ButterKnife.bind(this);
        getIntentData();
        et_amount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(7, 2)});
        sessionManager = new SessionManager(this);
    }

    private void getIntentData() {
        type = getIntent().getStringExtra("type");
        receiverImage = getIntent().getStringExtra("receiverImage");
        receiverName = getIntent().getStringExtra("receiverName");
        receiverUid = getIntent().getStringExtra("receiverUid");
        receiverIdentifier = getIntent().getStringExtra("receiverIdentifier");
    }

    private void openPaynowDialog() {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.paynow_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        TextView tV_payTo, tV_amt, tvBalance;
        RelativeLayout rL_paynow;
        ImageView iV_back, imageView;
        tV_payTo = (TextView) dialog.findViewById(R.id.tV_payTo);
        tV_amt = dialog.findViewById(R.id.tV_amt);
        tvBalance = dialog.findViewById(R.id.tvBalance);
//        rL_paynow = (RelativeLayout) dialog.findViewById(R.id.rL_paynow);
        iV_back = dialog.findViewById(R.id.iV_back);
        imageView = dialog.findViewById(R.id.imageView);

        String payto = getString(R.string.pay_to) + " " + receiverName;
        tV_payTo.setText(payto);

        String balance;
        if (type != null && type.equals("coin"))
            balance = sessionManager.getCoinBalance();
        else
            balance = sessionManager.getCurrencySymbol() + " " + sessionManager.getWalletBalance();
        tvBalance.setText(balance);

        String amt;
        if (type != null && type.equals("coin")) {
            amt = et_amount.getText().toString().trim();
        } else {
            amt = sessionManager.getCurrencySymbol() + " " + et_amount.getText().toString().trim();
        }
        tV_amt.setText(amt);

//        rL_paynow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//                //successView();
//                dialog.dismiss();
//            }
//        });

        iV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @OnClick(R.id.iV_back)
    public void back() {
        onBackPressed();
    }

    @OnClick(R.id.rL_done)
    public void done() {
        if (!et_amount.getText().toString().isEmpty()) {
            openPaynowDialog();
        } else {
            Snackbar.make(ll_mainView, getString(R.string.valid_amount), Snackbar.LENGTH_LONG).show();
        }

    }
}
