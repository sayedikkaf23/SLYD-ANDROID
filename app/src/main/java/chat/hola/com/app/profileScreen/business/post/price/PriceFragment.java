package chat.hola.com.app.profileScreen.business.post.price;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.ezcall.android.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.DecimalDigitsInputFilter;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.models.Currency;
import chat.hola.com.app.post.PostActivity;
import dagger.android.support.DaggerFragment;

/**
 * <h1>PriceFragment</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 05 September 2019
 */
public class PriceFragment extends DaggerFragment implements PriceContract.View, AdapterView.OnItemSelectedListener {

    @Inject
    TypefaceManager typefaceManager;

    @BindView(R.id.spCurrency)
    AppCompatSpinner spCurrency;
    @BindView(R.id.etPrice)
    EditText etPrice;
    @BindView(R.id.tvSave)
    TextView tvSave;
    @BindView(R.id.title)
    TextView title;

    @Inject
    PricePresenter presenter;

    private ArrayAdapter<String> dataAdapter;
    private List<String> currency;
    private List<Currency> currencyData;
    public static String currencyText;
    public static String symbol;
    public static String price;
    private InputMethodManager imm;

    @Inject
    public PriceFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_price, container, false);
        ButterKnife.bind(this, view);

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        price = getArguments().getString("businessPrice");
        currencyText = getArguments().getString("businessCurrency");

        if (price != null && !price.isEmpty())
            etPrice.setText(price);

        etPrice.setTypeface(typefaceManager.getRegularFont());
        tvSave.setTypeface(typefaceManager.getSemiboldFont());
        title.setTypeface(typefaceManager.getSemiboldFont());

        spCurrency.setOnItemSelectedListener(this);

        currency = new ArrayList<>();
        currencyData = new ArrayList<>();
        dataAdapter = new ArrayAdapter<String>(getContext(), R.layout.item_simple, currency);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCurrency.setAdapter(dataAdapter);

        presenter.attacheView(this);
        presenter.currency();

        etPrice.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(7, 2)});
        showKeyboard();
        return view;
    }

    @OnClick(R.id.ibBack)
    public void back() {
        getActivity().onBackPressed();
    }


    @OnClick(R.id.tvSave)
    public void addPrice() {
        Intent intent = new Intent(getContext(), PostActivity.class);
        intent.putExtra("symbol", symbol);
        intent.putExtra("currency", currencyText);
        intent.putExtra("price", etPrice.getText().toString());
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currencyText = currency.get(position);
        symbol = currencyData.get(position).getSymbol();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void currency(List<Currency> currencyList) {
        this.currencyData = currencyList;
        for (Currency currency : currencyList) {
            this.currency.add(currency.getCurrency());
        }
        dataAdapter.notifyDataSetChanged();
        spCurrency.setSelection(dataAdapter.getPosition(currencyText));
    }

    private void showKeyboard() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                etPrice.requestFocus(etPrice.getText().length());
                if (imm != null) {
                    imm.showSoftInput(etPrice, InputMethodManager.SHOW_FORCED);
                }
            }
        }, 200);
    }

    private void hideKeyBoard() {
        if (imm != null)
            imm.hideSoftInputFromWindow(etPrice.getWindowToken(), 0);
    }
}
