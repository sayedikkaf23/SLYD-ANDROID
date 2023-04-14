package chat.hola.com.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ezcall.android.R;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.ConfirmDialog;
import chat.hola.com.app.ui.validate.ValidateActivity;

public class AttachPagerAdapter extends PagerAdapter {

    Context mContext;
    SetOnAttachMenuItemClickListner setOnAttachMenuItemClickListner;
    private boolean isOpponentTransferAvailable, walletAvailable;

    public AttachPagerAdapter(Context mContext, boolean walletAvailable, boolean isTransferAvailable) {
        this.mContext = mContext;
        this.isOpponentTransferAvailable = isTransferAvailable;
        this.walletAvailable = walletAvailable;
        setOnAttachMenuItemClickListner = (SetOnAttachMenuItemClickListner) mContext;
    }

    /*
     * Bug Title: Chat->Remove transfer coins and transfer amount option from chat.
     * Bug Id: DUBAND073
     * Fix Desc: change size of adapter
     * Fix Dev: Hardik
     * Fix Date: 15/4/21
     * */

    @Override
    public int getCount() {
        return (mContext instanceof ChatMessageScreen) ? 1 : 1;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ViewGroup view;
        if (position == 0) {
            view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.attachment_options_menu_3, container, false);

            LinearLayout layoutAudio1, layoutGallery, layoutPhoto, layoutVideo, layoutContacts, layoutAudio, layoutLocation, layoutGiphy, layoutDocument;
            TextView tvAudio1, tvGallery, tvPhoto, tvVideo, tvContacts, tvAudio, tvLocation, tvGiphy, tvDocument;
            tvAudio1 = (TextView) view.findViewById(R.id.tvAudio1);
            tvGallery = (TextView) view.findViewById(R.id.tvGallery);
            tvPhoto = (TextView) view.findViewById(R.id.tvPhoto);
            tvVideo = (TextView) view.findViewById(R.id.tvVideo);
            tvDocument = (TextView) view.findViewById(R.id.tvFile);

            tvContacts = (TextView) view.findViewById(R.id.tvContact);
            tvLocation = (TextView) view.findViewById(R.id.tvLocation);
            tvGiphy = (TextView) view.findViewById(R.id.tvGiphy);
            TextView tvTransfer = (TextView) view.findViewById(R.id.tvTransfer);


            Typeface face = AppController.getInstance().getRegularFont();
            tvGallery.setTypeface(face, Typeface.NORMAL);
            tvPhoto.setTypeface(face, Typeface.NORMAL);
            tvVideo.setTypeface(face, Typeface.NORMAL);
            tvContacts.setTypeface(face, Typeface.NORMAL);
            tvLocation.setTypeface(face, Typeface.NORMAL);
            tvTransfer.setTypeface(face, Typeface.NORMAL);
            tvGiphy.setTypeface(face, Typeface.NORMAL);
            tvDocument.setTypeface(face, Typeface.NORMAL);
            tvAudio1.setTypeface(face, Typeface.NORMAL);


            layoutAudio1 = (LinearLayout) view.findViewById(R.id.layoutAudio1);
            layoutDocument = (LinearLayout) view.findViewById(R.id.layoutDocument);
            layoutPhoto = (LinearLayout) view.findViewById(R.id.layoutPhoto);
            layoutVideo = (LinearLayout) view.findViewById(R.id.layoutVideo);
            layoutContacts = (LinearLayout) view.findViewById(R.id.layoutContact);
            layoutLocation = (LinearLayout) view.findViewById(R.id.layoutLocation);
            layoutGiphy = (LinearLayout) view.findViewById(R.id.layoutGiphy);
            layoutGallery = (LinearLayout) view.findViewById(R.id.layoutGallery);
            LinearLayout layoutTransfer = (LinearLayout) view.findViewById(R.id.layoutTransfer);


            /*
             * Bug Title: Chat->Remove transfer coins and transfer amount option from chat.
             * Bug Id: DUBAND073
             * Fix Desc: hide
             * Fix Dev: Hardik
             * Fix Date: 15/4/21
             * */
//            LinearLayout layoutTransferCoin = (LinearLayout) view.findViewById(R.id.layoutCoin);
//            layoutTransferCoin.setOnClickListener(v -> setOnAttachMenuItemClickListner.onCoinTransferClick());


            if (mContext instanceof ChatMessageScreen) {
//                layoutTransfer.setVisibility(View.VISIBLE);
            } else {
                layoutAudio1.setVisibility(View.VISIBLE);
            }


            layoutPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOnAttachMenuItemClickListner.onAttachCameraClick();
                }
            });

            layoutVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOnAttachMenuItemClickListner.onAttachVideoClick();
                }
            });

            layoutContacts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOnAttachMenuItemClickListner.onAttachContactClick();
                }
            });


            layoutLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOnAttachMenuItemClickListner.onAttachLocationClick();
                }
            });

            layoutGiphy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOnAttachMenuItemClickListner.onAttachGIFClick();
                }
            });

            layoutGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOnAttachMenuItemClickListner.onAttachAlbumClick();
                }
            });

            layoutDocument.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOnAttachMenuItemClickListner.onAttachFileClick();
                }
            });


            layoutTransfer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!walletAvailable) {
                        ConfirmDialog dialog = new ConfirmDialog(mContext, R.drawable.ic_wallet, mContext.getString(R.string.message_wallet_not_available), mContext.getString(R.string.create), mContext.getString(R.string.cancel));
                        dialog.show();

                        Button btnYes = dialog.findViewById(R.id.btnYes);
                        btnYes.setOnClickListener(v1 -> {
                            mContext.startActivity(new Intent(mContext, ValidateActivity.class)
                                    .putExtra("image", R.drawable.ic_validate)
                                    .putExtra("title", mContext.getString(R.string.kyc_not_verified))
                                    .putExtra("message", mContext.getString(R.string.kyc_not_verified_message)));
                            dialog.dismiss();
                        });

                        dialog.show();
                    } else if (!isOpponentTransferAvailable) {
                        ConfirmDialog dialog = new ConfirmDialog(mContext, R.drawable.ic_wallet, mContext.getString(R.string.message_wallet_ask_to_create), mContext.getString(R.string.ok), null);
                        Button btnYes = dialog.findViewById(R.id.btnYes);
                        btnYes.setOnClickListener(v1 -> {
                            dialog.dismiss();
                        });
                        dialog.show();
                    } else if (walletAvailable && isOpponentTransferAvailable) {
                        setOnAttachMenuItemClickListner.onAttachTransferClick();
                    }
                }
            });

            layoutAudio1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    setOnAttachMenuItemClickListner.onAttachAudioClick();


                }
            });

        } else {
            view = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.attachment_options_menu_2, container, false);

            LinearLayout layoutStarPacket, layoutTransfer, layoutVideo, layoutContacts, layoutAudio, layoutLocation, layoutGiphy, layoutDocument;
            TextView tvStarPacket, tvTransfer, tvVideo, tvContacts, tvAudio, tvLocation, tvGiphy, tvPhoto;
            //tvStarPacket = (TextView) view.findViewById(R.id.tvStarPacket);
            //tvTransfer = (TextView) view.findViewById(R.id.tvTransfer);
            tvVideo = (TextView) view.findViewById(R.id.tvVideo);
            tvContacts = (TextView) view.findViewById(R.id.tvContact);
            tvLocation = (TextView) view.findViewById(R.id.tvLocation);
            tvGiphy = (TextView) view.findViewById(R.id.tvGiphy);
            tvPhoto = (TextView) view.findViewById(R.id.tvPhoto);
            tvAudio = (TextView) view.findViewById(R.id.tvAudio);

            Typeface face = AppController.getInstance().getRegularFont();

//            tvStarPacket.setTypeface(face, Typeface.NORMAL);
//            tvTransfer.setTypeface(face, Typeface.NORMAL);
            tvVideo.setTypeface(face, Typeface.NORMAL);
            tvContacts.setTypeface(face, Typeface.NORMAL);
            tvLocation.setTypeface(face, Typeface.NORMAL);
            tvGiphy.setTypeface(face, Typeface.NORMAL);
            tvPhoto.setTypeface(face, Typeface.NORMAL);
            tvAudio.setTypeface(face, Typeface.NORMAL);

            layoutDocument = (LinearLayout) view.findViewById(R.id.layoutDocument);
            //layoutTransfer = (LinearLayout) view.findViewById(R.id.layoutTransfer);
            layoutVideo = (LinearLayout) view.findViewById(R.id.layoutVideo);
            layoutContacts = (LinearLayout) view.findViewById(R.id.layoutContact);
            layoutAudio = (LinearLayout) view.findViewById(R.id.layoutAudio);
            layoutLocation = (LinearLayout) view.findViewById(R.id.layoutLocation);
            layoutGiphy = (LinearLayout) view.findViewById(R.id.layoutGiphy);
            //layoutStarPacket = (LinearLayout) view.findViewById(R.id.layoutStarPacket);

            layoutAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    setOnAttachMenuItemClickListner.onAttachAudioClick();


                }
            });


        }
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public interface SetOnAttachMenuItemClickListner {
        public void onAttachFileClick();

        public void onAttachAlbumClick();

        public void onAttachCameraClick();

        public void onAttachGIFClick();

        public void onAttachAudioClick();

        public void onAttachContactClick();

        public void onAttachVideoClick();

        public void onAttachLocationClick();

        public void onAttachTransferClick();

        public void onAttachStarPacketClick();

        void onCoinTransferClick();
    }
}
