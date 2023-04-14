package chat.hola.com.app.profileScreen.business.post.actionbutton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ezcall.android.R;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.models.ActionButtonResponse;
import chat.hola.com.app.post.PostActivity;
import dagger.android.support.DaggerFragment;


/**
 * <h1>ActionButtonFragment</h1>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 05 September 2019
 */
public class ActionButtonFragment extends DaggerFragment implements ActionButtonAdapter.ClickListner, ActionButtonContract.View {

    @BindView(R.id.rvActionButton)
    RecyclerView rvActionButton;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvSave)
    TextView tvSave;

    private LinearLayoutManager layoutManager;

    @Inject
    ActionButtonAdapter adapter;
    @Inject
    ActionButtonPresenter presenter;
    @Inject
    TypefaceManager typefaceManager;
    public static String buttonText = "";
    public static String buttonColor = "";

    @Inject
    public ActionButtonFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_action_button, container, false);
        ButterKnife.bind(this, view);
        presenter.attacheView(this);

        buttonText = getArguments().getString("businessButtonText");

        tvSave.setTypeface(typefaceManager.getSemiboldFont());
        title.setTypeface(typefaceManager.getSemiboldFont());

        layoutManager = new LinearLayoutManager(getContext());
        rvActionButton.setLayoutManager(layoutManager);
        adapter.setClickListner(this);
        rvActionButton.addItemDecoration(new DividerItemDecoration(rvActionButton.getContext(), layoutManager.getOrientation()));
        rvActionButton.setAdapter(adapter);
        presenter.buttonText();
        return view;
    }

    @OnClick(R.id.ibBack)
    public void back() {
        getActivity().onBackPressed();
    }


    @OnClick(R.id.tvSave)
    public void addPrice() {
        Intent intent = new Intent(getContext(), PostActivity.class);
        intent.putExtra("button", buttonText);
        intent.putExtra("color", buttonColor);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onItemSelectListner(ActionButtonResponse.BizButton bizButton) {
        buttonText = bizButton.getButtonText();
        buttonColor = bizButton.getButtonColor();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void defaultItemSelected(ActionButtonResponse.BizButton bizButton) {
        buttonText = bizButton.getButtonText();
        buttonColor = bizButton.getButtonColor();
    }
}
