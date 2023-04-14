package chat.hola.com.app.Share.socialmedia;

import android.content.pm.PackageManager;

public class SupportedSocialMediaAppsConfig {

  private static final String FACEBOOK_PACKAGE_NAME = "com.facebook.katana";
  private static final String FACEBOOK_LITE_PACKAGE_NAME = "com.facebook.lite";

  private static final String FACEBOOK_MESSENGER_PACKAGE_NAME = "com.facebook.mlite";
  private static final String TWITTER_PACKAGE_NAME = "com.twitter.android";
  //private static final String TWITTER_LITE_PACKAGE_NAME = "com.twitter.android.lite";
  private static final String INSTAGRAM_PACKAGE_NAME = "com.instagram.android";
  private static final String INSTAGRAM_LITE_PACKAGE_NAME = "com.instagram.lite";

  private static final String PINTEREST_PACKAGE_NAME = "com.pinterest";
  //private static final String PINTEREST_LITE_PACKAGE_NAME = "com.pinterest.twa";

  //To support both whatsapp and whatsapp for business
  private static final String WHATS_PACKAGE_NAME = "com.whatsapp";
  //private static final String WHATS_BUSINESS_PACKAGE_NAME = "com.whatsapp.w4b";
  private static final String SKYPE_PACKAGE_NAME = "com.skype.raider";
  private static final String SNAPCHAT_PACKAGE_NAME = "com.snapchat.android";
  private static final String REDDIT_PACKAGE_NAME = "com.reddit.frontpage";
  private static final String LINKEDIN_PACKAGE_NAME = "com.linkedin.android";
  //private static final String LINKEDIN_LITE_PACKAGE_NAME = "com.linkedin.android.lite";
  private static final String QUORA_PACKAGE_NAME = "com.quora.android";

  public boolean isSupportedAppInstalled(String packageName) {

    return (packageName.equals(FACEBOOK_MESSENGER_PACKAGE_NAME)
        || packageName.contains(TWITTER_PACKAGE_NAME)
        || packageName.equals(INSTAGRAM_PACKAGE_NAME)
        || packageName.equals(INSTAGRAM_LITE_PACKAGE_NAME)
        || packageName.contains(PINTEREST_PACKAGE_NAME)
        || packageName.contains(WHATS_PACKAGE_NAME)
        || packageName.equals(SKYPE_PACKAGE_NAME)
        || packageName.equals(SNAPCHAT_PACKAGE_NAME)
        || packageName.equals(REDDIT_PACKAGE_NAME)
        || packageName.contains(LINKEDIN_PACKAGE_NAME)
        || packageName.equals(QUORA_PACKAGE_NAME));
  }

  public boolean isFacebookInstalled(PackageManager packageManager) {

    try {
      packageManager.getPackageInfo(FACEBOOK_PACKAGE_NAME, 0);
      return true;
    } catch (PackageManager.NameNotFoundException e) {
      try {
        packageManager.getPackageInfo(FACEBOOK_LITE_PACKAGE_NAME, 0);
        return true;
      } catch (PackageManager.NameNotFoundException ei) {
        return false;
      }
    }
  }
}
