package chat.hola.com.app.profileScreen.business.post.link;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.ezcall.android.R;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.post.PostActivity;
import chat.hola.com.app.profileScreen.business.post.price.PricePresenter;
import dagger.android.support.DaggerFragment;

/**
 * <h1>PriceFragment</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 05 September 2019
 */
public class LinkFragment extends DaggerFragment {

    @Inject
    TypefaceManager typefaceManager;

    @BindView(R.id.etLink)
    EditText etLink;
    @BindView(R.id.tvSave)
    TextView tvSave;
    @BindView(R.id.title)
    TextView title;

    @Inject
    PricePresenter presenter;

    private String url = "";
    private InputMethodManager imm;

    @Inject
    public LinkFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_link, container, false);
        ButterKnife.bind(this, view);

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        url = getArguments().getString("businessUrl");
        etLink.setText(url);

        etLink.setTypeface(typefaceManager.getRegularFont());
        tvSave.setTypeface(typefaceManager.getSemiboldFont());
        title.setTypeface(typefaceManager.getSemiboldFont());

        showKeyboard();

        return view;
    }

    @OnClick(R.id.ibBack)
    public void back() {
        hideKeyBoard();
        getActivity().onBackPressed();
    }


    @OnClick(R.id.tvSave)
    public void addLink() {
        hideKeyBoard();
        url = etLink.getText().toString().trim();
        Intent intent = new Intent(getContext(), PostActivity.class);
        intent.putExtra("url", url);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    private void showKeyboard() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                etLink.requestFocus(etLink.getText().length());
                if (imm != null) {
                    imm.showSoftInput(etLink, InputMethodManager.SHOW_FORCED);
                }
            }
        }, 200);
    }

    private void hideKeyBoard() {
        if (imm != null)
            imm.hideSoftInputFromWindow(etLink.getWindowToken(), 0);
    }
}
