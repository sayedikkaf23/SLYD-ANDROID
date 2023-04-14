package chat.hola.com.app.settings;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ezcall.android.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.profileScreen.model.Data;

public class SetSubscriptionDialog extends BottomSheetDialogFragment {

    private SessionManager sessionManager;
    private Data profileData;
    double appPercentage = 0.0;
    double userPercentage = 0.0;
    private double youAmount;
    private double appAmount;
    private double amount;
    ClickListener clickListener;

    public SetSubscriptionDialog(Data profileData, ClickListener clickListener) {
        this.profileData = profileData;
        this.clickListener = clickListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
        sessionManager = new SessionManager(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subscription_amout_dialog, container, false);

        TextView tVYouGet = view.findViewById(R.id.tVYouGet);
        TextView tVAppGet = view.findViewById(R.id.tVAppGet);
        EditText etAmount = view.findViewById(R.id.etAmount);
        Button btnConfirm = view.findViewById(R.id.btn_confirm);

        if (profileData.getSubscriptionDetails() != null) {
            appPercentage = profileData.getAppWillGetInPercentage();
            userPercentage = 100 - appPercentage;

            amount = profileData.getSubscriptionDetails().getAmount();
            etAmount.setText(String.valueOf(amount));
            youAmount = profileData.getSubscriptionDetails().getUserWillGet();
            appAmount = profileData.getSubscriptionDetails().getAppWillGet();
            tVAppGet.setText(appAmount+"("+sessionManager.getCurrencySymbol()+""+appAmount*profileData.getCoinValue()+")");
            tVYouGet.setText(youAmount+"("+sessionManager.getCurrencySymbol()+""+youAmount*profileData.getCoinValue()+")");
        }

        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) {
                    amount = Double.valueOf(s.toString());
                    double appAm = (amount * appPercentage) / 100;
                    double youAm = (amount * userPercentage) / 100;
                    String appTxt =  appAm+ "(" +sessionManager.getCurrencySymbol()+""+appAm*profileData.getCoinValue()+")";
                    String youTxt =  youAm+ "(" +sessionManager.getCurrencySymbol()+""+ youAm*profileData.getCoinValue()+")";
                    tVAppGet.setText(appTxt);
                    tVYouGet.setText(youTxt);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnConfirm.setOnClickListener(v -> {
            clickListener.onConfirmClick(amount, youAmount, appAmount);
        });


        return view;
    }

    public interface ClickListener{
        void onConfirmClick(double amount, double youAmount, double appAmount);
    }
}
