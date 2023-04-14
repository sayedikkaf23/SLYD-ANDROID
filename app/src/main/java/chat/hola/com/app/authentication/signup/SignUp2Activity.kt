package chat.hola.com.app.authentication.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import chat.hola.com.app.NumberVerification.ChooseCountry
import chat.hola.com.app.Utilities.Constants
import chat.hola.com.app.Utilities.TypefaceManager
import chat.hola.com.app.models.Login
import chat.hola.com.app.models.Register
import chat.hola.com.app.user_verification.phone.VerifyNumberOTPActivity
import com.ezcall.android.R
import com.ezcall.android.databinding.ActivitySignUp2Binding
import com.google.firebase.auth.FirebaseUser
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_sign_up2.*
import javax.inject.Inject

public class SignUp2Activity : DaggerAppCompatActivity(), SignUpContract.View {

    lateinit var binding : ActivitySignUp2Binding

    @Inject
    lateinit var presenter: SignUpContract.Presenter

    private val loginType = Constants.LoginType.NORMAL

    private var countryCodeName: String? = null
    private var countryCode: String? = null
    private var country: String? = null
    private var code: String? = null
    private val max_digits = 15
    private val CODE_NATIONALITY = 2
    private val CODE_COUNTRY = 3

    private var register: Register? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up2)

//        register = intent.getSerializableExtra("register") as Register?

        btn_next.setOnClickListener {
            if (!isValidated()) {
                return@setOnClickListener
            }
            register = Register()

            register?.countryCode = countryCode
            register?.country = country
            register?.mobileNumber = etPhone.text.toString()
            register?.countryCodeName = countryCodeName
     /*       register?.mobileNumber = etPhone.text.toString()
            register?.countryCode = countryCode
            register?.country = country
            register?.countryCodeName = countryCodeName*/

            presenter?.verifyIsMobileRegistered(etPhone.text.toString(),countryCode)
//            presenter?.validate(register)
            btn_next.isEnabled = false

        }
        binding.ivClose.setOnClickListener {
            onBackPressed()
        }

//        btn_next.setOnClickListener {
//
//            if(!isValidated()){
//                return@setOnClickListener
//            }
//
//            val intent = Intent(this, VerifyNumberOTPActivity::class.java)
//            if (etPhone.text.toString().equals("", ignoreCase = true)) {
//                intent.putExtra("countryCode", "")
//            } else {
//                intent.putExtra("countryCode", countryCode)
//            }
//            intent.putExtra("countryCodeName", countryCodeName)
//            intent.putExtra("country", country)
//            intent.putExtra("countryCode", countryCode)
//            intent.putExtra("phoneNumber", etPhone.text.toString())
//            intent.putExtra("isFromSignUp", true)
////            intent.putExtra("profilePic", profilePic)
//            intent.putExtra("register", register)
//            startActivity(intent)
//
//        }

        tvRegion.setOnClickListener {
            selectRegion()
        }
        ll_select_nationality.setOnClickListener {
            selectNationality()
        }
        rel_select_country.setOnClickListener {
            selectCountry()
        }
    }

    fun selectRegion() {
        val intent = Intent(this, ChooseCountry::class.java)
        startActivityForResult(intent, 0)
    }

    fun selectNationality() {
        val intent = Intent(this, ChooseCountry::class.java)
        startActivityForResult(intent, CODE_NATIONALITY)
    }

    fun selectCountry() {
        val intent = Intent(this, ChooseCountry::class.java)
        startActivityForResult(intent, CODE_COUNTRY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK) {
            countryCodeName = data?.getStringExtra("CODE_NAME")
            country = data?.getStringExtra("MESSAGE")
            code = data?.getStringExtra("CODE")
            countryCode = "+" + code!!.substring(1)
            etPhone.setText(getString(R.string.double_inverted_comma))
            //            ic_send.setVisibility(View.GONE);
            etPhone.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(max_digits))
            tvRegion.setText(countryCode)
        } else if (requestCode == CODE_COUNTRY && resultCode == RESULT_OK) {
            countryCodeName = data?.getStringExtra("CODE_NAME")
            country = data?.getStringExtra("MESSAGE")
            code = data?.getStringExtra("CODE")
//            text_country.text = data?.getStringExtra("MESSAGE")
            text_country.text =  "($code)$countryCodeName"
            countryCode = "+" + code!!.substring(1)
        } else if (requestCode == CODE_NATIONALITY && resultCode == RESULT_OK) {
            text_nationality.text = data?.getStringExtra("MESSAGE")
        }
    }

    fun isValidated(): Boolean {
        if (text_country.text.toString().isEmpty()) {
            Toast.makeText(this, "Please select country code", Toast.LENGTH_SHORT).show()

            return false
        }

        if (etPhone.text.toString().isEmpty()) {
            Toast.makeText(this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show()

            return false
        }

        return true
    }

    override fun registered(response: Login.LoginResponse?) {
        TODO("Not yet implemented")
    }

    override fun message(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }

    override fun completed() {
        btn_next.isEnabled = true
    }

    override fun setProfilePic(url: String?) {
    }

    override fun enableSave(enable: Boolean) {
        btn_next.isEnabled = enable
    }

    override fun showProgress(show: Boolean) {
        if (progress_bar != null) progress_bar.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun verifyPhone() {
        //if we are doing social logins then call signup api else call verifyOTP screen
        if (loginType == Constants.LoginType.GOOGLE || loginType == Constants.LoginType.FACEBOOK) {
//            callSignupApi()
        } else {
            val intent = Intent(this, VerifyNumberOTPActivity::class.java)
            if (etPhone.text.toString().equals("", ignoreCase = true)) {
                intent.putExtra("countryCode", "")
            } else {
                intent.putExtra("countryCode", countryCode)
            }
            intent.putExtra("countryCodeName", countryCodeName)
            intent.putExtra("country", country)
            intent.putExtra("countryCode", countryCode)
            intent.putExtra("phoneNumber", etPhone.text.toString())
            intent.putExtra("isFromSignUp", true)
//            intent.putExtra("profilePic", profilePic)
            intent.putExtra("register", register)
            startActivity(intent)

        }
    }

    override fun showLoader() {
        progress_bar.setVisibility(View.VISIBLE)

    }

    override fun hideLoader() {
        progress_bar.visibility = View.GONE

    }

    override fun socialLoginSuccess(response: Login.LoginResponse?) {
        TODO("Not yet implemented")
    }

    override fun socialIdNotRegistered(user: FirebaseUser?, loginType: String?) {
        TODO("Not yet implemented")
    }

    override fun emailalreadyRegistered(email: String?) {
        TODO("Not yet implemented")
    }

    override fun emailNotRegistered(email_number_is_not_registered: String?) {
        TODO("Not yet implemented")
    }

    override fun userNamealreadyRegistered(userName: String?) {
        TODO("Not yet implemented")
    }

    override fun userNameNotRegistered(userName_is_already_in_use: String?) {
        TODO("Not yet implemented")
    }

    override fun numberAlreadyRegistered(phoneNumber: String?) {
        Toast.makeText(this,"This number already Registered",Toast.LENGTH_SHORT).show()
    }

    override fun numberNotRegistered(mobile_number_is_not_registered: String?) {
        val i = Intent(this, SignUp1Activity::class.java);
        i.putExtra("register", register);
        i.putExtra("countryCodeName", countryCodeName)
        i.putExtra("country", country)
        i.putExtra("countryCode", countryCode)
        i.putExtra("phoneNumber", etPhone.text.toString())
        Log.v("--Register--",country + " " + countryCodeName + " " + countryCode + " " + etPhone.text.toString())
        startActivity(i)
    }

    override fun onResume() {
        super.onResume()
        btn_next.isEnabled = true
    }

}