package chat.hola.com.app.dagger;

import com.appscrip.myapplication.injection.module.ViewModelModule;

import chat.hola.com.app.Activities.MainActivity;
import chat.hola.com.app.Activities.Terms;
import chat.hola.com.app.AddContact.AddContactActivity;
import chat.hola.com.app.AddContact.AddContactModule;
import chat.hola.com.app.DublyCamera.CameraActivity;
import chat.hola.com.app.DublyCamera.deepar.DeeparFiltersTabCameraActivity;
import chat.hola.com.app.DublyCamera.deepar.DeeparTabCameraModule;
import chat.hola.com.app.DublyCamera.live_stream.CameraStreamModule;
import chat.hola.com.app.DublyCamera.tabs.TabCameraActivity;
import chat.hola.com.app.DublyCamera.tabs.TabCameraModule;
import chat.hola.com.app.NumberVerification.VerifyPhoneModule;
import chat.hola.com.app.NumberVerification.VerifyPhoneNumber;
import chat.hola.com.app.Profile.SaveProfile;
import chat.hola.com.app.Profile.SaveProfileModule;
import chat.hola.com.app.acceptRequest.AcceptRequestActivity;
import chat.hola.com.app.acceptRequest.AcceptRequestModule;
import chat.hola.com.app.activities_user.UserActivitiesActivity;
import chat.hola.com.app.activities_user.UserActivitiesModule;
import chat.hola.com.app.activities_user.UserActivitiesUtilModule;
import chat.hola.com.app.authentication.forgotpassword.ForgotPasswordActivity;
import chat.hola.com.app.authentication.forgotpassword.ForgotPasswordModule;
import chat.hola.com.app.authentication.login.LoginActivity;
import chat.hola.com.app.authentication.login.LoginModule;
import chat.hola.com.app.authentication.newpassword.NewPasswordActivity;
import chat.hola.com.app.authentication.newpassword.NewPasswordModule;
import chat.hola.com.app.authentication.signup.SignUp1Activity;
import chat.hola.com.app.authentication.signup.SignUp1Module;
import chat.hola.com.app.authentication.signup.SignUp2Activity;
import chat.hola.com.app.authentication.signup.SignUp2Module;
import chat.hola.com.app.authentication.signup.SignUpActivity;
import chat.hola.com.app.authentication.signup.SignUpModule;
import chat.hola.com.app.authentication.signup.SignUpUtilModule;
import chat.hola.com.app.authentication.verifyEmail.VerifyEmailActivity;
import chat.hola.com.app.authentication.verifyEmail.VerifyEmailModule;
import chat.hola.com.app.blockUser.BlockUserActivity;
import chat.hola.com.app.blockUser.BlockUserModule;
import chat.hola.com.app.blockUser.BlockUserUtilModule;
import chat.hola.com.app.calling.video.call.CallingActivity;
import chat.hola.com.app.category.CategoryActivity;
import chat.hola.com.app.category.CategoryModule;
import chat.hola.com.app.category.CategoryUtilModule;
import chat.hola.com.app.coin.base.CoinActivity;
import chat.hola.com.app.coin.base.CoinModule;
import chat.hola.com.app.coin.base.CoinUtilModule;
import chat.hola.com.app.collections.add_to_collection.AddToCollectionActivity;
import chat.hola.com.app.collections.add_to_collection.AddToCollectionModule;
import chat.hola.com.app.collections.collection.CollectionActivity;
import chat.hola.com.app.collections.collection.CollectionModule;
import chat.hola.com.app.collections.create_collection.CreateCollectionActivity;
import chat.hola.com.app.collections.create_collection.CreateCollectionModule;
import chat.hola.com.app.collections.edit_collection.EditCollectionActivity;
import chat.hola.com.app.collections.edit_collection.EditCollectionModule;
import chat.hola.com.app.collections.saved.SavedActivity;
import chat.hola.com.app.collections.saved.SavedModule;
import chat.hola.com.app.comment.CommentActivity;
import chat.hola.com.app.comment.CommentModule;
import chat.hola.com.app.comment.CommentUtilModule;
import chat.hola.com.app.dagger.module.AddAddressUseCaseModule;
import chat.hola.com.app.dagger.module.AddAddressViewModelModule;
import chat.hola.com.app.dagger.module.AddToCartUseCaseModule;
import chat.hola.com.app.dagger.module.ApplyPromoCodesUseCaseModule;
import chat.hola.com.app.dagger.module.AttributesBottomSheetDaggerModule;
import chat.hola.com.app.dagger.module.ClearWishListUseCaseModel;
import chat.hola.com.app.dagger.module.DeleteAddressUseCaseModule;
import chat.hola.com.app.dagger.module.DeleteWishListProductUseCaseModule;
import chat.hola.com.app.dagger.module.EcomCartDaggerModule;
import chat.hola.com.app.dagger.module.EcomCartViewModel;
import chat.hola.com.app.dagger.module.EcomProductDetaillsModelModule;
import chat.hola.com.app.dagger.module.EcomTrackViewModelModule;
import chat.hola.com.app.dagger.module.EditAddressUseCaseModule;
import chat.hola.com.app.dagger.module.GetCartUseCaseModule;
import chat.hola.com.app.dagger.module.GetDeliveryFeeUseCaseModule;
import chat.hola.com.app.dagger.module.GetOrderHistoryUseCaseModule;
import chat.hola.com.app.dagger.module.GetPackageDetUseCaseModule;
import chat.hola.com.app.dagger.module.GetSimilarProductsUseCaseModule;
import chat.hola.com.app.dagger.module.GetWishListProductsUseCaseModule;
import chat.hola.com.app.dagger.module.HelpUseCaseModule;
import chat.hola.com.app.dagger.module.HelpViewModelModule;
import chat.hola.com.app.dagger.module.HistoryOrderDetailModelModule;
import chat.hola.com.app.dagger.module.HistoryOrderDetailUseCaseModule;
import chat.hola.com.app.dagger.module.HistoryProductDetailViewModelModule;
import chat.hola.com.app.dagger.module.HistoryViewModelModule;
import chat.hola.com.app.dagger.module.IpAddressToLocationUseCaseModule;
import chat.hola.com.app.dagger.module.MakeAddressDefaultUseCaseModule;
import chat.hola.com.app.dagger.module.NotifyProductUseCaseModule;
import chat.hola.com.app.dagger.module.PaymentMethodModelModule;
import chat.hola.com.app.dagger.module.PlaceOrderUseCaseModule;
import chat.hola.com.app.dagger.module.ReviewProductModelModule;
import chat.hola.com.app.dagger.module.ReviewProductViewModule;
import chat.hola.com.app.dagger.module.SavedAddressListUseCaseModule;
import chat.hola.com.app.dagger.module.SavedAddressListViewModule;
import chat.hola.com.app.dagger.module.SellerBottomSheetDaggerModule;
import chat.hola.com.app.dagger.module.SortBottomSheetModule;
import chat.hola.com.app.dagger.module.TrackOrderUseCaseModule;
import chat.hola.com.app.dagger.module.ViewMoreSellersUseCaseModule;
import chat.hola.com.app.dagger.module.WishListViewModelModule;
import chat.hola.com.app.dubly.DubsActivity;
import chat.hola.com.app.dubly.DubsModule;
import chat.hola.com.app.dubly.DubsUtilModule;
import chat.hola.com.app.dublycategory.DubCategoryActivity;
import chat.hola.com.app.dublycategory.DubCategoryModule;
import chat.hola.com.app.dublycategory.DubCategoryUtilModule;
import chat.hola.com.app.ecom.addresslist.SavedAddressListActivity;
import chat.hola.com.app.ecom.addresslist.manageaddress.AddAddressActivity;
import chat.hola.com.app.ecom.cart.EcomCartActivity;
import chat.hola.com.app.ecom.help.HelpActivity;
import chat.hola.com.app.ecom.help.helpsubcategory.HelpSubCatActivity;
import chat.hola.com.app.ecom.pdp.ProductDetailsActivity;
import chat.hola.com.app.ecom.review.ReviewProductActivity;
import chat.hola.com.app.ecom.wishlist.WishListActivity;
import chat.hola.com.app.friends.FriendsActivity;
import chat.hola.com.app.friends.FriendsModule;
import chat.hola.com.app.friends.FriendsUtilModule;
import chat.hola.com.app.historyproductdetail.HistoryProductDetailActivity;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.home.LandingModule;
import chat.hola.com.app.home.LandingUtilModule;
import chat.hola.com.app.home.activity.youTab.channelrequesters.ChannelRequestersActivity;
import chat.hola.com.app.home.activity.youTab.channelrequesters.ChannelRequestersModule;
import chat.hola.com.app.home.activity.youTab.channelrequesters.ChannelRequestersUtilModule;
import chat.hola.com.app.home.activity.youTab.followrequest.FollowRequestActivity;
import chat.hola.com.app.home.activity.youTab.followrequest.FollowRequestModule;
import chat.hola.com.app.home.activity.youTab.followrequest.FollowRequestUtilModule;
import chat.hola.com.app.live_stream.Home.StreamingMainActivity;
import chat.hola.com.app.live_stream.Home.StreamingMainActivityModule;
import chat.hola.com.app.live_stream.Home.follow_user.FollowUserActivity;
import chat.hola.com.app.live_stream.Home.follow_user.FollowUserModule;
import chat.hola.com.app.live_stream.Home.follow_user.FollowUserUtilModule;
import chat.hola.com.app.live_stream.Home.live_users.LiveUsersActivity;
import chat.hola.com.app.live_stream.Home.live_users.LiveUsersModule;
import chat.hola.com.app.live_stream.Home.stream.StreamActivity;
import chat.hola.com.app.live_stream.Home.stream.StreamModule;
import chat.hola.com.app.live_stream.LiveStream.LiveStreamBroadcaster.RTMPStreamBroadCastModule;
import chat.hola.com.app.live_stream.LiveStream.LiveStreamBroadcaster.RTMPStreamBroadcasterActivity;
import chat.hola.com.app.live_stream.LiveStream.LiveStreamBroadcaster.WebRTCStreamBroadCastModule;
import chat.hola.com.app.live_stream.LiveStream.LiveStreamBroadcaster.WebRTCStreamBroadCastUtilModule;
import chat.hola.com.app.live_stream.LiveStream.LiveStreamBroadcaster.WebRTCStreamBroadcasterActivity;
import chat.hola.com.app.live_stream.LiveStream.LiveStreamViewer.RTMPStreamPlayerActivity;
import chat.hola.com.app.live_stream.LiveStream.LiveStreamViewer.RTMPStreamPlayerModule;
import chat.hola.com.app.live_stream.LiveStream.LiveStreamViewer.WebRTCStreamPlayerActivity;
import chat.hola.com.app.live_stream.LiveStream.LiveStreamViewer.WebRTCStreamPlayerModule;
import chat.hola.com.app.live_stream.LiveStream.LiveStreamViewer.WebRTCStreamPlayerUtilModule;
import chat.hola.com.app.location.LocSearchModule;
import chat.hola.com.app.location.LocSearchUtilModule;
import chat.hola.com.app.location.Location_Search_Activity;
import chat.hola.com.app.music.MusicActivity;
import chat.hola.com.app.music.MusicModule;
import chat.hola.com.app.music.MusicUtilModule;
import chat.hola.com.app.my_qr_code.MyQRCodeActivity;
import chat.hola.com.app.my_qr_code.MyQRCodeModule;
import chat.hola.com.app.mystories.MyStoriesActivity;
import chat.hola.com.app.mystories.MyStoriesDaggerModule;
import chat.hola.com.app.orderdetails.HistoryOrderDetailActivity;
import chat.hola.com.app.orders.historyscreen.HistoryActivity;
import chat.hola.com.app.post.PostActivity;
import chat.hola.com.app.post.PostModule;
import chat.hola.com.app.post.PostUtilModule;
import chat.hola.com.app.post.location.LocationActivity;
import chat.hola.com.app.post.location.LocationModule;
import chat.hola.com.app.post.location.LocationUtilModule;
import chat.hola.com.app.poststatus.PostStatusActivity;
import chat.hola.com.app.poststatus.PostStatusDaggerModule;
import chat.hola.com.app.preview.PreviewActivity;
import chat.hola.com.app.preview.PreviewModule;
import chat.hola.com.app.preview.PreviewUtilModule;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.profileScreen.ProfileModule;
import chat.hola.com.app.profileScreen.ProfileUtilModule;
import chat.hola.com.app.profileScreen.addChannel.AddChannelActivity;
import chat.hola.com.app.profileScreen.addChannel.AddChannelModule;
import chat.hola.com.app.profileScreen.addChannel.AddChannelUtilModule;
import chat.hola.com.app.profileScreen.business.StartBusinessProfileActivity;
import chat.hola.com.app.profileScreen.business.address.BusinessAddressActivity;
import chat.hola.com.app.profileScreen.business.category.BusinessCategoryActivity;
import chat.hola.com.app.profileScreen.business.category.BusinessCategoryModule;
import chat.hola.com.app.profileScreen.business.category.BusinessCategoryUtilModule;
import chat.hola.com.app.profileScreen.business.configuration.BusinessConfigurationActivity;
import chat.hola.com.app.profileScreen.business.configuration.BusinessConfigureModule;
import chat.hola.com.app.profileScreen.business.form.BusinessProfileFormActivity;
import chat.hola.com.app.profileScreen.business.form.BusinessProfileFormModule;
import chat.hola.com.app.profileScreen.business.form.verify.BusinessFormVerifyActivity;
import chat.hola.com.app.profileScreen.business.form.verify.BusinessFormVerifyModule;
import chat.hola.com.app.profileScreen.business.post.BusinessPostActivity;
import chat.hola.com.app.profileScreen.business.post.BusinessPostModule;
import chat.hola.com.app.profileScreen.business.post.BusinessPostUtilModule;
import chat.hola.com.app.profileScreen.discover.DiscoverActivity;
import chat.hola.com.app.profileScreen.discover.DiscoverModule;
import chat.hola.com.app.profileScreen.discover.DiscoverUtilModule;
import chat.hola.com.app.profileScreen.editProfile.BusinessBioActivity;
import chat.hola.com.app.profileScreen.editProfile.EditKnownAsActivity;
import chat.hola.com.app.profileScreen.editProfile.EditNameActivity;
import chat.hola.com.app.profileScreen.editProfile.EditProfileActivity;
import chat.hola.com.app.profileScreen.editProfile.EditProfileModule;
import chat.hola.com.app.profileScreen.editProfile.EditProfileUtilModule;
import chat.hola.com.app.profileScreen.editProfile.EditStatusActivity;
import chat.hola.com.app.profileScreen.editProfile.EditUserNameActivity;
import chat.hola.com.app.profileScreen.editProfile.changeEmail.ChangeEmail;
import chat.hola.com.app.profileScreen.editProfile.changeEmail.ChangeEmailModule;
import chat.hola.com.app.profileScreen.editProfile.editDetail.EditDetailActivity;
import chat.hola.com.app.profileScreen.editProfile.editDetail.EditDetailModule;
import chat.hola.com.app.profileScreen.followers.FollowersActivity;
import chat.hola.com.app.profileScreen.followers.FollowersModule;
import chat.hola.com.app.profileScreen.followers.FollowersUtilModule;
import chat.hola.com.app.request_star_profile.request_star.RequestStarModule;
import chat.hola.com.app.request_star_profile.request_star.RequestStarProfileActivity;
import chat.hola.com.app.request_star_profile.request_star.RequestStarUtilModule;
import chat.hola.com.app.request_star_profile.star_category.StarCatModule;
import chat.hola.com.app.request_star_profile.star_category.StarCategoryActivity;
import chat.hola.com.app.search.SearchActivity;
import chat.hola.com.app.search.SearchModule;
import chat.hola.com.app.search_user.SearchUserActivity;
import chat.hola.com.app.search_user.SearchUserModule;
import chat.hola.com.app.search_user.SearchUserUtilModule;
import chat.hola.com.app.settings.SettingsActivity;
import chat.hola.com.app.settings.SettingsModule;
import chat.hola.com.app.socialDetail.SocialDetailActivity;
import chat.hola.com.app.socialDetail.SocialDetailModule;
import chat.hola.com.app.socialDetail.SocialDetailUtilModule;
import chat.hola.com.app.star_configuration.StarConfigModule;
import chat.hola.com.app.star_configuration.StarConfigurationActivity;
import chat.hola.com.app.stars.StarActivity;
import chat.hola.com.app.stars.StarModule;
import chat.hola.com.app.stars.StarUtilModule;
import chat.hola.com.app.subscription.SubscriptionActivity;
import chat.hola.com.app.subscription.SubscriptionModule;
import chat.hola.com.app.tracking.EcomTrackingActivity;
import chat.hola.com.app.transfer_to_friend.TransferModule;
import chat.hola.com.app.transfer_to_friend.TransferReceivedDetail;
import chat.hola.com.app.transfer_to_friend.TransferSentDetail;
import chat.hola.com.app.transfer_to_friend.TransferToFriendActivity;
import chat.hola.com.app.trendingDetail.TrendingDetail;
import chat.hola.com.app.trendingDetail.TrendingDtlModule;
import chat.hola.com.app.trendingDetail.TrendingDtlUtilModule;
import chat.hola.com.app.ui.cards.CardActivity;
import chat.hola.com.app.ui.cards.CardModule;
import chat.hola.com.app.ui.cards.CardUtilModule;
import chat.hola.com.app.ui.dashboard.WalletDashboardActivity;
import chat.hola.com.app.ui.dashboard.WalletDashboardModule;
import chat.hola.com.app.ui.dashboard.WalletDashboardUtilModule;
import chat.hola.com.app.ui.kyc.KycActivity;
import chat.hola.com.app.ui.kyc.KycModule;
import chat.hola.com.app.ui.kyc.KycUtilModule;
import chat.hola.com.app.ui.password.RechargePasswordActivity;
import chat.hola.com.app.ui.password.RechargePasswordModule;
import chat.hola.com.app.ui.password.RechargePasswordUtilModule;
import chat.hola.com.app.ui.paymentgateway.PaymentGatewaysActivity;
import chat.hola.com.app.ui.paymentgateway.PaymentGatewaysModule;
import chat.hola.com.app.ui.paymentgateway.PaymentGatewaysUtilModule;
import chat.hola.com.app.ui.qrcode.WalletQrCodeActivity;
import chat.hola.com.app.ui.qrcode.WalletQrCodeModule;
import chat.hola.com.app.ui.qrcode.WalletQrCodeUtilModule;
import chat.hola.com.app.ui.recharge.RechargeActivity;
import chat.hola.com.app.ui.recharge.RechargeModule;
import chat.hola.com.app.ui.recharge.RechargeUtilModule;
import chat.hola.com.app.ui.stripe.AddStripeActivity;
import chat.hola.com.app.ui.stripe.AddStripeModule;
import chat.hola.com.app.ui.stripe.AddStripeUtilModule;
import chat.hola.com.app.ui.transfer.TransferSuccessActivity;
import chat.hola.com.app.ui.validate.ValidateActivity;
import chat.hola.com.app.ui.withdraw.addbankaccount.AddBankAccountActivity;
import chat.hola.com.app.ui.withdraw.addbankaccount.AddBankAccountModule;
import chat.hola.com.app.ui.withdraw.addbankaccount.AddBankAccountUtilModule;
import chat.hola.com.app.ui.withdraw.bankaccount.BankAccountActivity;
import chat.hola.com.app.ui.withdraw.bankaccount.BankAccountModule;
import chat.hola.com.app.ui.withdraw.bankaccount.BankAccountUtilModule;
import chat.hola.com.app.ui.withdraw.bankdetail.BankDetailActivity;
import chat.hola.com.app.ui.withdraw.bankdetail.BankDetailModule;
import chat.hola.com.app.ui.withdraw.bankdetail.BankDetailUtilModule;
import chat.hola.com.app.ui.withdraw.detail.WithdawDetailActivity;
import chat.hola.com.app.ui.withdraw.detail.WithdrawDetailModule;
import chat.hola.com.app.ui.withdraw.detail.WithdrawDetailUtilModule;
import chat.hola.com.app.ui.withdraw.method.WithdrawMethodActivity;
import chat.hola.com.app.ui.withdraw.method.WithdrawMethodModule;
import chat.hola.com.app.ui.withdraw.method.WithdrawMethodUtilModule;
import chat.hola.com.app.ui.withdraw.withdrawallog.WithdrawalLogActivity;
import chat.hola.com.app.ui.withdraw.withdrawallog.WithdrawalLogModule;
import chat.hola.com.app.ui.withdraw.withdrawallog.WithdrawalLogUtilModule;
import chat.hola.com.app.user_verification.VerifyEmailOTPActivity;
import chat.hola.com.app.user_verification.phone.VerifyNumberModule;
import chat.hola.com.app.user_verification.phone.VerifyNumberOTPActivity;
import chat.hola.com.app.wallet.transaction.TransactionActivity;
import chat.hola.com.app.wallet.transaction.TransactionModule;
import chat.hola.com.app.wallet.transaction.TransactionUtilModule;
import chat.hola.com.app.wallet.wallet_detail.WalletActivity;
import chat.hola.com.app.wallet.wallet_detail.WalletModule;
import chat.hola.com.app.webScreen.WebActivity;
import chat.hola.com.app.welcomeScreen.WelcomeActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

//import chat.hola.com.app.DublyCamera.banuba.BanubaFiltersTabCameraActivity;
//import chat.hola.com.app.DublyCamera.banuba.BanubaTabCameraModule;

/**
 * Created by ankit on 20/2/18.
 */

@Module
public interface ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector()
    BusinessBioActivity businessBioActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = BusinessConfigureModule.class)
    BusinessConfigurationActivity businessConfigurationActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {BusinessPostModule.class, BusinessPostUtilModule.class})
    BusinessPostActivity businessPostActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    BusinessAddressActivity businessAddressActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = LoginModule.class)
    LoginActivity loginActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = ForgotPasswordModule.class)
    ForgotPasswordActivity forgotPasswordActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = VerifyEmailModule.class)
    VerifyEmailActivity verifyEmailActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = NewPasswordModule.class)
    NewPasswordActivity newPasswordActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {SignUpModule.class, SignUpUtilModule.class, UserModule.class})
    SignUpActivity signUpActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {SignUp2Module.class, SignUpUtilModule.class, UserModule.class})
    SignUp2Activity signUp2Activity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {SignUp1Module.class, SignUpUtilModule.class, UserModule.class})
    SignUp1Activity signUp1Activity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {EcomTrackViewModelModule.class,
            TrackOrderUseCaseModule.class})
    abstract EcomTrackingActivity ecomTrackingActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {StarModule.class, StarUtilModule.class})
    StarActivity starActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AcceptRequestModule.class})
    AcceptRequestActivity acceptRequestActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AddContactModule.class})
    AddContactActivity addContactActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {LocSearchModule.class, LocSearchUtilModule.class})
    Location_Search_Activity locationSearchActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {CategoryModule.class, CategoryUtilModule.class})
    CategoryActivity categoryActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {FollowersModule.class, FollowersUtilModule.class})
    FollowersActivity followerActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = ChangeEmailModule.class)
    ChangeEmail changeEmail();

    @ActivityScoped
    @ContributesAndroidInjector()
    WebActivity webActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {DiscoverModule.class, DiscoverUtilModule.class})
    DiscoverActivity discoverActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = SearchModule.class)
    SearchActivity searchActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {PostModule.class, PostUtilModule.class})
    PostActivity postActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {EditProfileModule.class, EditProfileUtilModule.class})
    EditProfileActivity editProfileActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AddChannelModule.class, AddChannelUtilModule.class})
    AddChannelActivity addChannelActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {ProfileModule.class, ProfileUtilModule.class})
    ProfileActivity profileActivity();

    @ActivityScoped
    @ContributesAndroidInjector()
    WelcomeActivity welcomeActivity();

    @ActivityScoped
    @ContributesAndroidInjector()
    Terms terms();

    @ActivityScoped
    @ContributesAndroidInjector()
    MainActivity mainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {LandingModule.class, LandingUtilModule.class, UserModule.class})
    LandingActivity contactSyncLandingPage();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {UserActivitiesModule.class, UserActivitiesUtilModule.class})
    UserActivitiesActivity userActivitiesActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {TrendingDtlModule.class, TrendingDtlUtilModule.class})
    TrendingDetail trendingDetail();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {SocialDetailModule.class, SocialDetailUtilModule.class})
    SocialDetailActivity socialDetailPresenterImpl();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {VerifyPhoneModule.class})
    VerifyPhoneNumber verifyNumber();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {SettingsModule.class})
    SettingsActivity settingsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {SaveProfileModule.class})
    SaveProfile saveProfile();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {CommentModule.class, CommentUtilModule.class})
    CommentActivity commentActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {SearchUserModule.class, SearchUserUtilModule.class})
    SearchUserActivity searchUserActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {FriendsModule.class, FriendsUtilModule.class})
    FriendsActivity friendsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {MusicModule.class, MusicUtilModule.class})
    MusicActivity musicActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {BlockUserModule.class, BlockUserUtilModule.class})
    BlockUserActivity blockUserActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {DubsModule.class, DubsUtilModule.class})
    DubsActivity dubsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {DubCategoryModule.class, DubCategoryUtilModule.class})
    DubCategoryActivity dubCategoryActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {PreviewModule.class, PreviewUtilModule.class})
    PreviewActivity previewActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {ChannelRequestersModule.class, ChannelRequestersUtilModule.class})
    ChannelRequestersActivity channelRequestersActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {FollowRequestModule.class, FollowRequestUtilModule.class})
    FollowRequestActivity followRequestActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = MyStoriesDaggerModule.class)
    MyStoriesActivity provideMyStories();

    @ActivityScoped
    @ContributesAndroidInjector(modules = PostStatusDaggerModule.class)
    PostStatusActivity providePostStatus();

    @ActivityScoped
    @ContributesAndroidInjector(modules = WalletModule.class)
    WalletActivity provideWallet();

    @ActivityScoped
    @ContributesAndroidInjector(modules = MyQRCodeModule.class)
    MyQRCodeActivity proMyQrCode();

    @ActivityScoped
    @ContributesAndroidInjector(modules = StarCatModule.class)
    StarCategoryActivity provideStarCategory();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {RequestStarModule.class, RequestStarUtilModule.class})
    RequestStarProfileActivity provideRequestStarProfileActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = StarConfigModule.class)
    StarConfigurationActivity provideStarConfigurationActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {TransactionModule.class, TransactionUtilModule.class})
    TransactionActivity provideTransactionActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = TransferModule.class)
    TransferToFriendActivity provideTransferToFriendActivity();

    @ActivityScoped
    @ContributesAndroidInjector()
    TransferSentDetail provideTransferSentDetail();

    @ActivityScoped
    @ContributesAndroidInjector()
    TransferReceivedDetail provideTransferReceivedDetail();

//    @ActivityScoped
//    @ContributesAndroidInjector()
//    VerifyNumberOTPActivity provideVerifyNumberOTPActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = StreamingMainActivityModule.class)
    StreamingMainActivity streamingActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = RTMPStreamBroadCastModule.class)
    RTMPStreamBroadcasterActivity rtmpBroadcasterActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {WebRTCStreamBroadCastModule.class, WebRTCStreamBroadCastUtilModule.class})
    WebRTCStreamBroadcasterActivity webrtcBroadcasterActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = RTMPStreamPlayerModule.class)
    RTMPStreamPlayerActivity rtmpStreamPlayerActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {WebRTCStreamPlayerModule.class, WebRTCStreamPlayerUtilModule.class})
    WebRTCStreamPlayerActivity webrtcStreamPlayerActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = StreamModule.class)
    StreamActivity streamActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = LiveUsersModule.class)
    LiveUsersActivity liveUsersActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = CameraStreamModule.class)
    CameraActivity cameraActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {LocationModule.class, LocationUtilModule.class})
    LocationActivity locationActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = VerifyNumberModule.class)
    VerifyNumberOTPActivity verifyNumberOTPActivity();

    //business profile
    @ActivityScoped
    @ContributesAndroidInjector()
    StartBusinessProfileActivity startBusinessProfileActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = BusinessProfileFormModule.class)
    BusinessProfileFormActivity businessProfileActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {BusinessCategoryModule.class, BusinessCategoryUtilModule.class})
    BusinessCategoryActivity businessCategoryActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = SavedModule.class)
    SavedActivity savedActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = CreateCollectionModule.class)
    CreateCollectionActivity createCollectionActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = CollectionModule.class)
    CollectionActivity collectionActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = AddToCollectionModule.class)
    AddToCollectionActivity addToCollectionActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = EditCollectionModule.class)
    EditCollectionActivity editCollectionActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {CallingModule.class, ViewModelModule.class, UserModule.class})
    CallingActivity calling();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {FollowUserModule.class, FollowUserUtilModule.class})
    FollowUserActivity followUserActivity();


    @ActivityScoped
    @ContributesAndroidInjector(modules = {KycModule.class, KycUtilModule.class})
    KycActivity kycActivity();

    @ActivityScoped
    @ContributesAndroidInjector()
    chat.hola.com.app.ui.wallet.WebActivity webActivity1();

    @ActivityScoped
    @ContributesAndroidInjector()
    ValidateActivity validateActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {WalletDashboardModule.class, WalletDashboardUtilModule.class})
    WalletDashboardActivity walletDashboard();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {RechargeModule.class, RechargeUtilModule.class})
    RechargeActivity rechargeActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {PaymentGatewaysModule.class, PaymentGatewaysUtilModule.class})
    PaymentGatewaysActivity paymentGatewaysActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {WalletQrCodeModule.class, WalletQrCodeUtilModule.class})
    WalletQrCodeActivity walletQrCodeActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {RechargePasswordModule.class, RechargePasswordUtilModule.class})
    RechargePasswordActivity rechargePasswordActivity();

    @ActivityScoped
    @ContributesAndroidInjector()
    TransferSuccessActivity transferSuccessActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {WithdrawalLogModule.class, WithdrawalLogUtilModule.class})
    WithdrawalLogActivity withdrawalLogActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {BankAccountModule.class, BankAccountUtilModule.class})
    BankAccountActivity bankAccountActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AddBankAccountModule.class, AddBankAccountUtilModule.class})
    AddBankAccountActivity addBankAccountActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {CardModule.class, CardUtilModule.class})
    CardActivity cardActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {WithdrawMethodModule.class, WithdrawMethodUtilModule.class})
    WithdrawMethodActivity withdrawMethodActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AddStripeModule.class, AddStripeUtilModule.class})
    AddStripeActivity addStripeActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {BankDetailModule.class, BankDetailUtilModule.class})
    BankDetailActivity bankDetailActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {WithdrawDetailModule.class, WithdrawDetailUtilModule.class})
    WithdawDetailActivity withdawDetailActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = TabCameraModule.class)
    TabCameraActivity tabCameraActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = DeeparTabCameraModule.class)
    DeeparFiltersTabCameraActivity deeparFiltersTabCameraActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {HelpViewModelModule.class, HelpUseCaseModule.class})
    HelpActivity helpAct();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {GetOrderHistoryUseCaseModule.class, HistoryViewModelModule.class})
    HistoryActivity historyActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {
            HistoryOrderDetailUseCaseModule.class, HistoryOrderDetailModelModule.class})
    HistoryOrderDetailActivity historydetaialsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {HistoryProductDetailViewModelModule.class,
            GetPackageDetUseCaseModule.class,
            HistoryOrderDetailUseCaseModule.class})
    HistoryProductDetailActivity historyDetailsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {})
    HelpSubCatActivity helpSubCat();

    @ActivityScoped
    @ContributesAndroidInjector(
            modules = {SavedAddressListViewModule.class, DeleteAddressUseCaseModule.class, AddToCartUseCaseModule.class,
                    SavedAddressListUseCaseModule.class, MakeAddressDefaultUseCaseModule.class,
                    GetDeliveryFeeUseCaseModule.class})
    SavedAddressListActivity savedAddressListActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {AddAddressUseCaseModule.class,
            AddAddressViewModelModule.class, EditAddressUseCaseModule.class})
    abstract AddAddressActivity addAddressDetailsActivity();
//    @ActivityScoped
//    @ContributesAndroidInjector(modules = BanubaTabCameraModule.class)
//    BanubaFiltersTabCameraActivity banubaFiltersTabCameraActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {CoinModule.class, CoinUtilModule.class})
    CoinActivity coinActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {EcomProductDetaillsModelModule.class,
            AttributesBottomSheetDaggerModule.class, DeleteWishListProductUseCaseModule.class,
            SellerBottomSheetDaggerModule.class,
            /*ReplaceBottomSheetModule.class,*/
            NotifyProductUseCaseModule.class, AddToCartUseCaseModule.class, GetSimilarProductsUseCaseModule.class,
            ViewMoreSellersUseCaseModule.class, UserModule.class})
    ProductDetailsActivity productDetailsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(
            modules = {
                    EcomCartDaggerModule.class, EcomCartViewModel.class, GetCartUseCaseModule.class,
                    IpAddressToLocationUseCaseModule.class, AddToCartUseCaseModule.class,
                    UserModule.class, PlaceOrderUseCaseModule.class, ApplyPromoCodesUseCaseModule.class})
    EcomCartActivity ecomCartActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {PaymentMethodModelModule.class,
        /*WalletAddAmountUseCaseModule.class,
        TransactionEstimateUseCaseModule.class*/})
    chat.hola.com.app.ecom.payment.PaymentMethodActivity paymentMethodActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {ReviewProductModelModule.class,
            ReviewProductViewModule.class/*, UploadImageUseCaseModule.class*/})
    ReviewProductActivity reviewProductActivity();

    @ActivityScoped
    @ContributesAndroidInjector(
            modules = {GetWishListProductsUseCaseModule.class, ClearWishListUseCaseModel.class,
                    SortBottomSheetModule.class,
                    WishListViewModelModule.class, DeleteWishListProductUseCaseModule.class, UserModule.class})
    WishListActivity wishListActivity();

    /**
     * Group streaming
     */
//    @ActivityScoped
//    @ContributesAndroidInjector(modules = {
//            MultiLiveModule.class
//    })
//    MultiLiveSelectMembersActivity multiLiveSelectMembersActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = {SubscriptionModule.class})
    SubscriptionActivity subscriptionActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = EditDetailModule.class)
    EditDetailActivity editDetailActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = BusinessFormVerifyModule.class)
    BusinessFormVerifyActivity businessFormVerifyActivity();

    @ActivityScoped
    @ContributesAndroidInjector()
    EditNameActivity editNameActivity();

    @ActivityScoped
    @ContributesAndroidInjector()
    EditStatusActivity editStatusActivity();

    @ActivityScoped
    @ContributesAndroidInjector()
    EditUserNameActivity editUserNameActivity();

    @ActivityScoped
    @ContributesAndroidInjector()
    EditKnownAsActivity editKnownAsActivity();

    @ActivityScoped
    @ContributesAndroidInjector()
    VerifyEmailOTPActivity verifyEmailOtpActivity();
}
