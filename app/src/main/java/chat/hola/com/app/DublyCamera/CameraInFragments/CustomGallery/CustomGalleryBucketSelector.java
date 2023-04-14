package chat.hola.com.app.DublyCamera.CameraInFragments.CustomGallery;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.ezcall.android.R;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;


/**
 * Created by moda on 09/05/17.
 */

public class CustomGalleryBucketSelector {
    private static CustomGalleryBucketSelector gallery_picker_aleret = null;
    private PopupWindow gallery_list_poup = null;

    /**
     * Making it single tone
     */
    private CustomGalleryBucketSelector() {
    }

    public static CustomGalleryBucketSelector getInstance() {
        if (gallery_picker_aleret == null) {
            return gallery_picker_aleret = new CustomGalleryBucketSelector();
        } else {
            return gallery_picker_aleret;
        }
    }

    /**
     * <h2>show_popup_block_user</h2>
     * <p>
     * <p>
     * </P>
     */
    public PopupWindow showBucketPicker(final View view, final Activity activity, CustomGalleryBucketAdapter adapter, final CustomGalleryBucketItemListener listener) {
        LayoutInflater inflater = activity.getLayoutInflater();
        if (gallery_list_poup == null) {
            gallery_list_poup = new PopupWindow(activity);
        } else {
            gallery_list_poup.dismiss();
        }
        int popupWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        int popupHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
        @SuppressLint("InflateParams") View popupView = inflater.inflate(R.layout.gallery_alert_picker, null);
        RecyclerView bukket_item_name = (RecyclerView) popupView.findViewById(R.id.popup_list);
        bukket_item_name.setLayoutManager(new LinearLayoutManager(activity));
        bukket_item_name.setAdapter(adapter);
        bukket_item_name.addOnItemTouchListener(new RecyclerItemClickListener(activity,bukket_item_name ,new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (listener != null && gallery_list_poup != null) {
                    gallery_list_poup.dismiss();
                    listener.onCLick(position);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
        gallery_list_poup.setFocusable(true);
        gallery_list_poup.setContentView(popupView);
        gallery_list_poup.setWidth(popupWidth);
        gallery_list_poup.setHeight(popupHeight);
        gallery_list_poup.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        view.post(new Runnable() {
            public void run() {
                gallery_list_poup.showAsDropDown(view, 20, 0);
            }
        });
        return gallery_list_poup;
    }
}
