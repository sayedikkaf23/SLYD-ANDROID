package chat.hola.com.app.preview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Utilities.TypefaceManager;

/**
 * <h>ProfileMenuFrag</h>
 *
 * @author 3Embed.
 * @since 27-10-2017.
 */

public class ViewerFragment extends BottomSheetDialogFragment {

    private LayoutInflater inflater;
    private View view;

    @Inject
    TypefaceManager typefaceManager;

    @BindView(R.id.listViewer)
    RecyclerView listViewer;

    private Unbinder unbinder;

    @Inject
    public ViewerFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            view = inflater.inflate(R.layout.viewer_bottom_sheet, null);
        }
        unbinder = ButterKnife.bind(this, view);
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