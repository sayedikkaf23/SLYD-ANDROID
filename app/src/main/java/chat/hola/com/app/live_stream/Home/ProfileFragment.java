package chat.hola.com.app.live_stream.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ezcall.android.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Activities.MainActivity;
import chat.hola.com.app.live_stream.Home.dummy.DummyContent;
import chat.hola.com.app.live_stream.pubsub.MQTTManager;
import chat.hola.com.app.manager.session.SessionManager;
import dagger.android.support.DaggerFragment;


public class ProfileFragment extends DaggerFragment implements ProfilePresenterContract.ProfileView {


    @Inject
    public ProfileFragment() {
        // Required empty public constructor
    }


    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvEmailPlaceHolder)
    TextView tvEmailPlaceHolder;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.rlSignout)
    RelativeLayout rlSignout;

    @Inject
    SessionManager manager;
    @Inject
    ProfilePresenterContract.ProfilePresenter presenter;
    @Inject
    MQTTManager mQttManager;

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        presenter.attachView(this);

        showUserProfile();
        rlSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }


    public void showUserProfile() {
        tvName.setText(manager.getFirstName());

        if (manager.getEmail() != null) {

            tvEmailPlaceHolder.setText(getString(R.string.email));
            tvEmail.setText(manager.getEmail());
        } else {
            tvEmailPlaceHolder.setText(getString(R.string.phone_number));
            tvEmail.setText(manager.getMobileNumber());
        }


    }


    @Override
    public void showFailedSignoutAlert(String message) {
        //Failed to signout
        try {
            Toast.makeText(getActivity(), getString(R.string.signout_failed), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void signOutResponse() {

        manager.clear();
        mQttManager.disconnect();
        Intent intent = new Intent(getActivity(), MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        if (getActivity() != null) {
            getActivity().supportFinishAfterTransition();
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(DummyContent.DummyItem item);
    }


}
