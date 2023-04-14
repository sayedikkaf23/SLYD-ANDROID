package chat.hola.com.app.profileScreen.bottomProfileMenu;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ezcall.android.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Dialog.ImageSourcePicker;
import chat.hola.com.app.DublyCamera.deepar.DeeparFiltersTabCameraActivity;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.profileScreen.ProfilePresenter;
import chat.hola.com.app.profileScreen.addChannel.AddChannelActivity;

/**
 * <h>ProfileMenuFrag</h>
 *
 * @author 3Embed.
 * @since 27-10-2017.
 */

public class ProfileMenuFrag extends BottomSheetDialogFragment {

    private LayoutInflater inflater;
    private View view;

    @Inject
    TypefaceManager typefaceManager;

    @Inject
    ProfileActivity activity;

    @Inject
    ProfilePresenter profilePresenter;

    @Inject
    ImageSourcePicker imageSourcePicker;

    @BindView(R.id.fabCross)
    FloatingActionButton fabCross;

    @BindView(R.id.fabCamera)
    FloatingActionButton fabCamera;

    @BindView(R.id.fabCreateChannel)
    FloatingActionButton fabCreateChannel;

    @BindView(R.id.tvCreateChannel)
    TextView tvCreateChannel;
    @BindView(R.id.tvPost)
    TextView tvPost;


    private Unbinder unbinder;

    @Inject
    public ProfileMenuFrag() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            view = inflater.inflate(R.layout.profile_bottom_menu, null);
            unbinder = ButterKnife.bind(this, view);
            tvCreateChannel.setTypeface(typefaceManager.getSemiboldFont());
            tvPost.setTypeface(typefaceManager.getSemiboldFont());
        }
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        dialog.setContentView(view);
        FrameLayout bottomSheet = (FrameLayout) dialog.getWindow().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundColor(Color.TRANSPARENT);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @OnClick(R.id.fabCreateChannel)
    public void createChannel() {
        startActivity(new Intent(getActivity(), AddChannelActivity.class));
        getDialog().dismiss();
    }

    @OnClick(R.id.fabCamera)
    public void camera() {
        //camera change
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            startActivity(new Intent(getContext(), DeeparFiltersTabCameraActivity.class).putExtra("call", "post"));
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
//        ImageSourcePicker.OnSelectImageSource onSelectImageSourceCallback  = new ImageSourcePicker.OnSelectImageSource() {
//            @Override
//            public void onCamera() {
//                profilePresenter.launchCustomCamera();
//                dismiss();
//            }
//            @Override
//            public void onGallary() {
//                profilePresenter.launchGallery();
//                dismiss();
//            }
//
//            @Override
//            public void onCancel() {
//                dismiss();
//            }
//        };
//        imageSourcePicker.setOnSelectImageSource(onSelectImageSourceCallback);
//        imageSourcePicker.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.fabCross)
    public void stop() {
        getDialog().dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }


}