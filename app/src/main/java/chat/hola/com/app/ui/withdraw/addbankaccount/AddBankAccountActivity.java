package chat.hola.com.app.ui.withdraw.addbankaccount;

import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.ezcall.android.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.base.BaseActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.ui.withdraw.bankaccount.BankAccountActivity;
import chat.hola.com.app.ui.withdraw.model.bankfield.Field;

/**
 * <h1>{@link AddBankAccountActivity}</h1>
 * <p>{@link AddBankAccountActivity shows the fileds dynamically from api,
 * which is require for add bank account and call api through presenter to add bank account} </p>
 *
 * @author : Hardik Karkar
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 12 March 2020
 */

public class AddBankAccountActivity extends BaseActivity implements AddBankAccountContract.View {

    @BindView(R.id.root)
    CoordinatorLayout root;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    Map<String, TextInputEditText> editTextMap = new HashMap<>();

    @Inject
    TypefaceManager typefaceManager;
    @Inject
    Loader loader;
    @Inject
    AddBankAccountContract.Presenter presenter;
    @Inject
    SessionManager sessionManager;
    private String paymentGateWayId = "5e3156a4d8bb6d8f119cc6e6";
    private List<Field> fields = new ArrayList<>();
    private static final String TAG = AddBankAccountActivity.class.getSimpleName();
    private String email, account_number, routing_number, account_holder_type, account_holder_name, country, currency;
    @BindView(R.id.etAccountNumber)
    EditText etAccountNumber;
    @BindView(R.id.etRoutingNumber)
    EditText etRoutingNumber;
    @BindView(R.id.etAccountHolderName)
    EditText etAccountHolderName;
    @BindView(R.id.btnSave)
    Button btnSave;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_bank_account;
    }

    @Override
    public void initView() {
        super.initView();
        presenter.attachView(this);
//        presenter.getBankFields(paymentGateWayId);
    }

    @OnClick(R.id.btnSave)
    public void save() {
        if (validate()) {
            Map<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("account_number", account_number);
            params.put("routing_number", routing_number);
            params.put("account_holder_type", account_holder_type);
            params.put("account_holder_name", account_holder_name);
            params.put("country", "US");
            params.put("currency", "USD");
            presenter.addBank(params);
        }
    }

    private boolean validate() {
        account_holder_type = "individual";
        email = sessionManager.getEmail();
        currency = sessionManager.getCurrency();
        country = sessionManager.getCountry();

        account_number = etAccountNumber.getText().toString();
        routing_number = etRoutingNumber.getText().toString();
        account_holder_name = etAccountHolderName.getText().toString();
        if (account_number == null || account_number.isEmpty()) {
            showMessage("Enter account number", -1);
            return false;
        }

        if (routing_number == null || routing_number.isEmpty()) {
            showMessage("Enter routing number", -1);
            return false;
        }

        if (account_holder_name.isEmpty()) {
            showMessage("Enter account holder name", -1);
            return false;
        }

        return true;
    }

    @Override
    public void setTypeface() {
        super.setTypeface();
        tvTitle.setTypeface(typefaceManager.getSemiboldFont());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @OnClick(R.id.ibBack)
    public void back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, BankAccountActivity.class));
        finish();
    }

    @Override
    public void showMessage(String msg, int msgId) {
        Snackbar snackbar = Snackbar.make(root, msg, Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP;
        view.setLayoutParams(params);
        snackbar.show();
    }

    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(getApplicationContext());
    }

    @Override
    public void isInternetAvailable(boolean flag) {

    }

    @Override
    public void userBlocked() {

    }

    @Override
    public void reload() {

    }

    @Override
    public void showLoader() {
        loader.show();
    }

    @Override
    public void hideLoader() {
        if (loader.isShowing())
            loader.dismiss();
    }

    @Override
    public void showFields(List<Field> fieldList) {
        this.fields.clear();
        this.fields = fieldList;

        for (Field f : fields) {

            View view = LayoutInflater.from(this).inflate(R.layout.bank_input_field_item, linearLayout, false);

            TextInputLayout textInputLayout = view.findViewById(R.id.textInputLayout);
            TextInputEditText editText = view.findViewById(R.id.editText);
            textInputLayout.setHint(f.getName());
            editText.setTypeface(typefaceManager.getRegularFont());
            switch (f.getType()) {
                case "string":
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;
                case "password":
                    editText.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                    break;
                default:
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                    break;

            }
            editTextMap.put(f.getId(), editText);

            linearLayout.addView(view);

        }

        if (!fields.isEmpty()) {
            View view = LayoutInflater.from(this).inflate(R.layout.button_save, linearLayout, false);
            Button btnSave = view.findViewById(R.id.btnSave);
            btnSave.setTypeface(typefaceManager.getMediumFont());

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isValidate(fieldList)) {
                        presenter.addBankAccount(fieldList, country);
                    }
                }
            });

            linearLayout.addView(view);
        }
    }

    @Override
    public void bankAccountAdded() {
        finish();
    }

    private boolean isValidate(List<Field> fieldList) {
        for (Field f : fieldList) {
            f.setValue(editTextMap.get(f.getId()).getText().toString().trim());

            if (f.getValue().isEmpty()) {
                showMessage("Please fill " + f.getName(), 0);
                return false;
            }

            /*This is just for confirm field*/
            if (f.getRef() != null) {
                try {
                    if (f.getValue().equals(editTextMap.get(f.getRef()).getText().toString().trim())) {
                        Log.d(TAG, "confirm true");
                    } else {
                        Log.d(TAG, "confirm false");
                        showMessage(f.getName() + " not matched!", 0);
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Log.d(TAG, "saved fields=" + f.getId() + " " + f.getName() + " " + f.getValue());
        }
        return true;
    }
}
