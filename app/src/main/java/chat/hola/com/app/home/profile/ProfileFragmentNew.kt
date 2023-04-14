package chat.hola.com.app.home.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import chat.hola.com.app.AppController
import chat.hola.com.app.Utilities.Utilities
import chat.hola.com.app.cameraActivities.manager.CameraOutputModel
import chat.hola.com.app.home.LandingActivity
import chat.hola.com.app.manager.session.SessionManager
import chat.hola.com.app.models.WalletResponse
import chat.hola.com.app.profileScreen.model.Data
import chat.hola.com.app.profileScreen.model.Profile
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.signature.StringSignature
import com.ezcall.android.databinding.ProfileFragmentNewBinding
import dagger.android.support.DaggerFragment
import java.lang.Exception
import java.util.ArrayList
import javax.inject.Inject

class ProfileFragmentNew  @Inject constructor() : DaggerFragment(), ProfileContract.View {
    lateinit var binding: ProfileFragmentNewBinding
    lateinit var mActivity: Activity

    var userId: String? = null
    private var profileData: Data? = null
    private var myid: String? = null
    var userName = ""


    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var presenter: ProfilePresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = ProfileFragmentNewBinding.inflate(inflater, container, false)
        //changeVisibilityOfViews();
        presenter.attachView(this)

        myid = AppController.getInstance().userId

        mActivity = requireActivity()

        binding.iVSettings.setOnClickListener {

        }
        loadData()

        return binding.root
    }

    fun changeVisibilityOfViews() {
        (mActivity as LandingActivity).hideActionBar()
        (mActivity as LandingActivity).removeFullScreenFrame()
        (mActivity as LandingActivity).linearPostTabs.visibility = View.GONE
        //        ((LandingActivity) mActivity).drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        (mActivity as LandingActivity).tvCoins.visibility = View.GONE
        (mActivity as LandingActivity).tvSearch.visibility = View.GONE
        (mActivity as LandingActivity).ivLiveStream.visibility = View.GONE
//        loadData()
    }

    fun loadData() {
        presenter.loadProfileData()

    }

    override fun reload() {
        TODO("Not yet implemented")
    }

    override fun showMessage(msg: String?, msgId: Int) {
        TODO("Not yet implemented")
    }

    override fun sessionExpired() {
        TODO("Not yet implemented")
    }

    override fun isInternetAvailable(flag: Boolean) {
        TODO("Not yet implemented")
    }

    override fun userBlocked() {
        TODO("Not yet implemented")
    }

    override fun applyFont() {
        TODO("Not yet implemented")
    }

    override fun isLoading(flag: Boolean) {
    }

    override fun showProfileData(profile: Profile?) {
        try {
            userId = profile!!.data[0].id
            showProfile(profile)
        } catch (ignored: Exception) {
        }
    }

    fun showProfile(profile: Profile) {
        try {

            profileData = profile.data[0]

            if (profileData != null) {

                val fname = profileData!!.getFirstName()
                val lname = profileData!!.getLastName()

                if (profile.data[0].id == myid) {
                    sessionManager.setUserProfilePic(profile.data[0].profilePic, false)
                    sessionManager.setFirstName(fname)
                    sessionManager.setLastsName(lname)

                    binding.tvProfileName.setText("$fname $lname")
                    binding.tvNumber.setText("(${profile.data[0].countryCode}) "+profile.data[0].number)
                }
            }

            userName = profileData?.getUserName().orEmpty()
            val data = profile.data[0]


            if (data.getProfilePic() != null && !data.getProfilePic().isEmpty()) {
                Glide.with(mActivity).load(data.getProfilePic()).asBitmap()
                        .signature(StringSignature(AppController.getInstance().sessionManager.userProfilePicUpdateTime))
                        .centerCrop().into(object : BitmapImageViewTarget(binding.ivProfile) {
                            override fun setResource(resource: Bitmap) {
                                super.setResource(resource)
                                binding.ivProfile.setImageBitmap(resource)
                            }
                        })
            } else {
                Utilities.setTextDrawable(mActivity, data.getFirstName(), data.getLastName(), binding.ivProfile)
            }


        } catch (ignored: Exception) {
            ignored.printStackTrace();
        }

    }

    override fun isFollowing(flag: Boolean) {
        TODO("Not yet implemented")
    }

    override fun launchCustomCamera() {
        TODO("Not yet implemented")
    }

    override fun checkReadImage() {
        TODO("Not yet implemented")
    }

    override fun launchImagePicker(intent: Intent?) {
        TODO("Not yet implemented")
    }

    override fun launchCropImage(uri: Uri?) {
        TODO("Not yet implemented")
    }

    override fun showSnackMsg(msgId: Int) {
        TODO("Not yet implemented")
    }

    override fun launchPostActivity(model: CameraOutputModel?) {
        TODO("Not yet implemented")
    }

    override fun addToReportList(data: ArrayList<String>?) {
        TODO("Not yet implemented")
    }

    override fun addToBlockList(data: ArrayList<String>?) {
        TODO("Not yet implemented")
    }

    override fun block(block: Boolean) {
        TODO("Not yet implemented")
    }

    override fun unfriend() {
        TODO("Not yet implemented")
    }

    override fun showBalance(data: WalletResponse.Data.Wallet?) {
        TODO("Not yet implemented")
    }

    override fun noProfile(message: String?) {
        TODO("Not yet implemented")
    }

    override fun moveNext(verificationStatus: Int?) {
        TODO("Not yet implemented")
    }

    override fun showCoinBalance(wallet: WalletResponse.Data.Wallet?) {
        TODO("Not yet implemented")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }

    override fun showLoader() {
        TODO("Not yet implemented")
    }

    override fun hideLoader() {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }
}