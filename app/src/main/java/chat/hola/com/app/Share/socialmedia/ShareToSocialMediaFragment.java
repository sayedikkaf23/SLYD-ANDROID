/**
 * This piece of code was intentionally discarded,as sharing to facebook required an activity to be
 * active, but it works fine for sharing to other apps or dynamic links.
 */

//package chat.hola.com.app.Share.socialmedia;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.ComponentName;
//import android.content.Intent;
//import android.content.pm.ResolveInfo;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Parcelable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.RelativeLayout;
//import android.widget.Toast;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.widget.AppCompatTextView;
//import androidx.core.content.FileProvider;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import chat.hola.com.app.AppController;
//import chat.hola.com.app.postshare.WatermarkConfig;
//import com.ezcall.android.R;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.share.Sharer;
//import com.facebook.share.model.SharePhoto;
//import com.facebook.share.model.SharePhotoContent;
//import com.facebook.share.model.ShareVideo;
//import com.facebook.share.model.ShareVideoContent;
//import com.facebook.share.widget.ShareDialog;
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
//import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
//import io.isometrik.groupstreaming.ui.utils.AlertProgress;
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ShareToSocialMediaFragment extends BottomSheetDialogFragment
//    implements DownloadMediaToShareResult {
//
//  public static final String TAG = "ShareToSocialMediaFragment";
//
//  @BindView(R.id.rlFacebook)
//  RelativeLayout rlFacebook;
//
//  private View view;
//
//  private String mediaUrl, postId;
//  private boolean isImage;
//  private Activity activity;
//  private AlertProgress alertProgress;
//
//  private AlertDialog alertDialog;
//
//  private ShareDialog shareDialog;
//  private CallbackManager callbackManager;
//  private AlertDialog alert;
//  private boolean shareOnFacebook;
//  private SupportedSocialMediaAppsConfig supportedSocialMediaAppsConfig;
//
//  public ShareToSocialMediaFragment() {
//    // Required empty public constructor
//  }
//
//  @Nullable
//  @Override
//  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
//      @Nullable Bundle savedInstanceState) {
//
//    if (view == null) {
//
//      view = inflater.inflate(R.layout.bottomsheet_share_to_social_media, container, false);
//    } else {
//
//      if (view.getParent() != null) ((ViewGroup) view.getParent()).removeView(view);
//    }
//    ButterKnife.bind(this, view);
//    alertProgress = new AlertProgress();
//
//    //FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));
//    //FacebookSdk.sdkInitialize(activity);
//    callbackManager = CallbackManager.Factory.create();
//    shareDialog = new ShareDialog(this);
//    shareDialog.registerCallback(callbackManager, shareCallback);
//    supportedSocialMediaAppsConfig = new SupportedSocialMediaAppsConfig();
//
//    rlFacebook.setVisibility(
//        supportedSocialMediaAppsConfig.isFacebookInstalled(activity.getPackageManager())
//            ? View.VISIBLE : View.GONE);
//
//    return view;
//  }
//
//  @OnClick(R.id.rlFacebook)
//  public void shareToFacebook() {
//    shareOnFacebook = true;
//    downloadMediaToShare();
//    dismiss();
//  }
//
//  @OnClick(R.id.rlDynamicLink)
//  public void shareDynamicLink() {
//    prepareDynamicLink();
//    dismiss();
//  }
//
//  @OnClick(R.id.rlOthers)
//  public void shareOnOtherApps() {
//    shareOnFacebook = false;
//    downloadMediaToShare();
//    dismiss();
//  }
//
//  public void prepareDynamicLink() {
//    if (activity != null) {
//      showProgressDialog(getResources().getString(R.string.please_wait));
//      new Thread(() -> {
//        String url = AppController.getInstance().getString(R.string.dynamic_link_base_url) + postId;
//        FirebaseDynamicLinks.getInstance()
//            .createDynamicLink()
//            .setLongLink(Uri.parse(
//                AppController.getInstance().getString(R.string.dynamic_long_link_url)
//                    + url
//                    + AppController.getInstance().getString(R.string.dynamic_query_param)))
//            .buildShortDynamicLink()
//            .addOnCompleteListener(activity, task -> {
//              if (task.isSuccessful()) {
//                Uri shortLink = task.getResult().getShortLink();
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
//                intent.putExtra(Intent.EXTRA_SUBJECT,
//                    AppController.getInstance().getString(R.string.app_name));
//                intent.setType("text/plain");
//                Intent chooser = Intent.createChooser(intent,
//                    AppController.getInstance().getString(R.string.share_post));
//                activity.startActivity(chooser);
//              }
//              hideProgressDialog();
//            })
//            .addOnFailureListener(Throwable::printStackTrace);
//      }).start();
//    }
//  }
//
//  @Override
//  public void onCreate(@Nullable Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    activity = getActivity();
//  }
//
//  @Override
//  public void onDestroy() {
//    super.onDestroy();
//    //activity = null;
//  }
//
//  private void showProgressDialog(String message) {
//
//    if (activity != null) {
//      alertDialog = alertProgress.getProgressDialog(activity, message);
//      if (!activity.isFinishing()) alertDialog.show();
//    }
//  }
//
//  private void hideProgressDialog() {
//
//    if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
//  }
//
//  private final FacebookCallback<Sharer.Result> shareCallback =
//      new FacebookCallback<Sharer.Result>() {
//        @Override
//        public void onCancel() {
//          Toast.makeText(activity,
//              AppController.getInstance().getString(R.string.facebook_share_canceled),
//              Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onError(FacebookException error) {
//          Toast.makeText(activity, String.format("Error: %s", error.toString()), Toast.LENGTH_SHORT)
//              .show();
//        }
//
//        @Override
//        public void onSuccess(Sharer.Result result) {
//          Toast.makeText(activity,
//              AppController.getInstance().getString(R.string.facebook_share_successfull),
//              Toast.LENGTH_SHORT).show();
//        }
//      };
//
//  private void shareOnFacebook(String mediaPath) {
//    if (activity != null) {
//      File media = new File(mediaPath);
//      Uri uri;
//      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        uri = FileProvider.getUriForFile(activity,
//            activity.getApplicationContext().getPackageName() + ".provider", media);
//      } else {
//        uri = Uri.fromFile(media);
//      }
//      if (isImage) {
//        SharePhoto sharePhoto = new SharePhoto.Builder().setImageUrl(uri).build();
//
//        SharePhotoContent photoContent =
//            new SharePhotoContent.Builder().addPhoto(sharePhoto).build();
//
//        if (ShareDialog.canShow(SharePhotoContent.class)) {
//          shareDialog.show(photoContent);
//          //shareDialog.show(photoContent,ShareDialog.Mode.WEB);
//        } else {
//
//          activity.runOnUiThread(() -> Toast.makeText(activity,
//              AppController.getInstance().getString(R.string.facebook_share_not_available),
//              Toast.LENGTH_SHORT).show());
//        }
//      } else {
//        // Create the URI from the media
//
//        ShareVideo shareVideo = new ShareVideo.Builder().setLocalUrl(uri).build();
//        ShareVideoContent videoContent = new ShareVideoContent.Builder().setVideo(shareVideo)
//            .setContentTitle(AppController.getInstance().getString(R.string.share_post_title))
//            .setContentDescription(
//                AppController.getInstance().getString(R.string.share_post_description))
//            //.setPreviewPhoto()
//            .build();
//        if (ShareDialog.canShow(ShareVideoContent.class)) {
//          shareDialog.show(videoContent);
//        } else {
//
//          activity.
//              runOnUiThread(() -> Toast.makeText(activity,
//                  AppController.getInstance().getString(R.string.facebook_share_not_available),
//                  Toast.LENGTH_SHORT).show());
//        }
//      }
//    }
//  }
//
//  private void shareOnOtherSocialMediaApps(String mediaPath) {
//    if (activity != null) {
//
//      String type;
//      if (isImage) {
//        type = "image/*";
//      } else {
//        type = "video/*";
//      }
//
//      List<Intent> targetShareIntents = new ArrayList<Intent>();
//      Intent shareIntent = new Intent();
//      shareIntent.setAction(Intent.ACTION_SEND);
//      shareIntent.setType(type);
//      List<ResolveInfo> queryIntentActivities =
//          activity.getPackageManager().queryIntentActivities(shareIntent, 0);
//      if (!queryIntentActivities.isEmpty()) {
//
//        for (ResolveInfo resolveInfo : queryIntentActivities) {
//          String packageName = resolveInfo.activityInfo.packageName;
//
//          if (supportedSocialMediaAppsConfig.isSupportedAppInstalled(packageName)) {
//            Intent intent = new Intent();
//            intent.setComponent(new ComponentName(packageName, resolveInfo.activityInfo.name));
//            intent.setAction(Intent.ACTION_SEND);
//            intent.setType(type);
//
//            // Create the URI from the media
//            File media = new File(mediaPath);
//            Uri uri;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//              uri = FileProvider.getUriForFile(activity,
//                  activity.getApplicationContext().getPackageName() + ".provider", media);
//            } else {
//              uri = Uri.fromFile(media);
//            }
//            intent.putExtra(Intent.EXTRA_STREAM, uri);
//            intent.putExtra(Intent.EXTRA_SUBJECT,
//                AppController.getInstance().getString(R.string.share_post_description));
//            intent.setPackage(packageName);
//            targetShareIntents.add(intent);
//          }
//        }
//        if (!targetShareIntents.isEmpty()) {
//
//          Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0),
//              AppController.getInstance().getString(R.string.choose_app));
//          chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
//              targetShareIntents.toArray(new Parcelable[] {}));
//          activity.startActivity(chooserIntent);
//        } else {
//          Toast.makeText(activity,
//              AppController.getInstance().getString(R.string.no_apps_installed), Toast.LENGTH_SHORT)
//              .show();
//        }
//      }
//    }
//  }
//
//  private void downloadMediaToShare() {
//    AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
//    @SuppressLint("InflateParams")
//    final View vDownload = getLayoutInflater().inflate(R.layout.share_confirmation, null);
//
//    AppCompatTextView tvShare = vDownload.findViewById(R.id.tvShare);
//    tvShare.setEnabled(true);
//    tvShare.setText(AppController.getInstance().getString(R.string.share));
//    tvShare.setOnClickListener(v -> {
//      tvShare.setEnabled(false);
//      tvShare.setText(AppController.getInstance().getString(R.string.share_preparing));
//      String fileName;
//      if (isImage) {
//        fileName = postId + ".jpg";
//      } else {
//        fileName = postId + ".mp4";
//      }
//
//      DownloadMedia.startMediaDownload(vDownload.findViewById(R.id.pbDownload), mediaUrl, this,
//          fileName);
//    });
//
//    alertDialog.setView(vDownload);
//
//    alert = alertDialog.create();
//    //alert.setCancelable(false);
//    if (activity != null) {
//      if (!activity.isFinishing()) alert.show();
//    }
//  }
//
//  @Override
//  public void downloadResult(String result, String filePath) {
//    if (alert != null && alert.isShowing()) {
//      try {
//        alert.dismiss();
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
//    }
//    if (result == null) {
//      if (shareOnFacebook) {
//        shareOnFacebook(filePath);
//      } else {
//        shareOnOtherSocialMediaApps(filePath);
//      }
//    } else {
//      if (activity != null) {
//        activity.runOnUiThread(() -> Toast.makeText(activity, result, Toast.LENGTH_SHORT).show());
//      }
//    }
//  }
//
//  @Override
//  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//    super.onActivityResult(requestCode, resultCode, data);
//    callbackManager.onActivityResult(requestCode, resultCode, data);
//  }
//
//  public void updateParameters(String mediaUrl, boolean isImage, String postId, Activity activity) {
//    this.mediaUrl = mediaUrl.replace("upload/", "upload/" + WatermarkConfig.WATERMARK_CONFIG);
//    this.isImage = isImage;
//    this.postId = postId;
//    this.activity = activity;
//  }
//}
