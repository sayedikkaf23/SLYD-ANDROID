package chat.hola.com.app.home.social;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.ezcall.android.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.coin.base.CoinActivity;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.manager.session.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SendTipDialog#} factory method to
 * create an instance of this fragment.
 */
public class SendTipDialog extends BottomSheetDialogFragment {

    private EditText etAmount;
    private Button btnConfirm;
    private RadioGroup rgAmountTag;
    private Data data;
    private ClickListener clickListener;
    private SessionManager sessionManager;
    private TextView tvCoinBalance;
    private ImageView back;
    private TextView tvGetMore;

    public SendTipDialog(Data data, ClickListener clickListener) {
        this.data = data;
        this.clickListener = clickListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
        sessionManager = new SessionManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_tip_dialog, container, false);
        etAmount = view.findViewById(R.id.etAmount);
        tvGetMore = view.findViewById(R.id.tvGetMore);
        tvCoinBalance = view.findViewById(R.id.tvCoinBalance);
        btnConfirm = view.findViewById(R.id.btn_confirm);
        rgAmountTag = view.findViewById(R.id.rgAmountTag);
        back = view.findViewById(R.id.back);
        tvCoinBalance.setText(sessionManager.getCoinBalance());
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("50");
        arrayList.add("100");
        arrayList.add("200");
        arrayList.add("500");
        setSuggestedAmount("",arrayList);
        btnConfirm.setOnClickListener(v -> {
            clickListener.onConfirmSendTipClick(data,etAmount.getText().toString().trim(),"");
            dismiss();
        });
        back.setOnClickListener(v -> dismiss());
        tvGetMore.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), CoinActivity.class));
        });

         /*
         * Bug Title: can you change the color of the button when it’s deactivated, in the tip page for example I have not typed any
                        tip so confirm button should be deactivated
         *  Bug Id: DUBAND140
         * Fix Desc: add selector on etAmount
         * Fix Dev: Hardik
         * Fix Date: 7/5/21
         * */
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnConfirm.setEnabled(s.length()>0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    @SuppressLint("SetTextI18n")
    public void setSuggestedAmount(String currency_symbol, List<String> amounts) {
        for (int i = 0; i < amounts.size(); i++) {
            String amount = amounts.get(i);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(currency_symbol + " " + amount);
            radioButton.setBackground(getActivity().getDrawable(R.drawable.amount_selector));
            radioButton.setPadding(30, 30, 30, 30);
            radioButton.setButtonDrawable(null);
            radioButton.setButtonDrawable(null);
            radioButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_coin_small, 0, 0, 0);
            radioButton.setTextSize(15);
            params.setMargins(10, 10, 10, 10);
            radioButton.setLayoutParams(params);
            radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    etAmount.setText(amount);
                }
            });
            rgAmountTag.addView(radioButton);
        }
    }

    public interface ClickListener{
        void onConfirmSendTipClick(Data data, String coin, String desc);
    }
}