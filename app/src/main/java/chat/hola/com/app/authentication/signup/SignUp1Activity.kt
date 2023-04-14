package chat.hola.com.app.authentication.signup

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import butterknife.OnClick
import chat.hola.com.app.Utilities.Constants
import chat.hola.com.app.models.Login
import chat.hola.com.app.models.Register
import chat.hola.com.app.user_verification.phone.VerifyNumberOTPActivity
import com.ezcall.android.R
import com.ezcall.android.databinding.ActivitySignUp1Binding
import com.ezcall.android.databinding.ActivitySignUp2Binding
import com.google.firebase.auth.FirebaseUser
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_sign_up1.*
import kotlinx.android.synthetic.main.activity_sign_up1.btn_next
import kotlinx.android.synthetic.main.activity_sign_up2.*
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

public class SignUp1Activity : DaggerAppCompatActivity(), SignUpContract.View {

    private var timestamp: Timestamp? = null
    var register: Register = Register()
    lateinit var binding: ActivitySignUp1Binding
    private var countryCodeName: String? = null
    private var countryCode: String? = null
    private var country: String? = null
    private var mobileNumber: String? = null
    private var code: String? = null
    private val loginType = Constants.LoginType.NORMAL

    @Inject
    lateinit var presenter: SignUpContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_up1)


        mobileNumber = intent.getStringExtra("phoneNumber").toString() as String?
        countryCodeName = intent.getStringExtra("countryCodeName").toString() as String?
        countryCode = intent.getStringExtra("countryCode").toString() as String?
        country = intent.getStringExtra("country").toString() as String?
        Log.v("--Register--2",mobileNumber + " " + countryCodeName + " " + countryCode + " " + country)


        ll_dob.setOnClickListener {
            dob()
        }
        ivCalendar.setOnClickListener {
            dob()
        }
        tvDob.setOnClickListener {
            dob()
        }


        btn_next.setOnClickListener {
            onNext()
        }

        binding.ivClose.setOnClickListener{
            onBackPressed()
        }

    }


    fun dob() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(this, { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            val newDate = Calendar.getInstance()
            newDate[year, monthOfYear] = dayOfMonth
            val str_date = dateFormatter.format(newDate.time)
            tvDob.setText(str_date)
            timestamp = Timestamp(newDate.timeInMillis)
            Log.i("t1", timestamp?.getTime().toString())
            tvDobWarning.setVisibility(if (year > 2007) View.VISIBLE else View.INVISIBLE)
        }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH])
        datePickerDialog.show()
    }

    fun onNext() {
        if (!isValidated()) {
            return
        }

        val fullName = etFullName.text.toString().split(" ")
        register.userName = etUserName.text.toString()
        if (fullName.size > 0) {
            register.firstName = fullName.get(0)
        }
        if (fullName.size > 1) {
            register.lastName = fullName.get(1)
        }
        register.email = etEmailid.text.toString()
        register.dateOfBirth = tvDob.text.toString()
        register.gender = spinner.getSelectedItem().toString()
        register.mobileNumber = mobileNumber
        register.countryCode = countryCode
        register.country = country
        register.countryCodeName = countryCodeName
        presenter?.verifyIsUserNameRegistered(etUserName.text.toString())
//        presenter?.validate(register)
        /*val i = Intent(this, SignUp2Activity::class.java);
        i.putExtra("register", register);
        startActivity(i)*/

    }

    fun isValidated(): Boolean {
        if (etUserName.text.toString().isEmpty()) {
            message("Please enter user name")
            return false
        }
        if (etFullName.text.toString().isEmpty()) {
            message("Please enter full name")
            return false
        }

        if (etEmailid.text.toString().isEmpty()) {
            message("Please enter valid email address")
            return false
        }

        if (tvDob.text.toString().isEmpty()) {
            message("Please enter Date of Birth")
            return false
        }

        if (spinner.getSelectedItem().toString().equals("Select your gender")) {
            message("Please enter your gender")
            return false
        }

        if(etUserName.text.toString().length < 4){
            message("Please enter username more than 4 characters")
            return false
        }

        if(etFullName.text.toString().length < 4){
            message("Please enter Name more than 4 characters")
            return false
        }

        return true;
    }

    override fun registered(response: Login.LoginResponse?) {
        TODO("Not yet implemented")
    }

    override fun completed() {
        btn_next.isEnabled = true
    }

    override fun setProfilePic(url: String?) {
        TODO("Not yet implemented")
    }

    override fun enableSave(enable: Boolean) {
        btn_next.isEnabled = enable
    }

    override fun showProgress(show: Boolean) {
        if (progressBar != null) progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun verifyPhone() {
        //if we are doing social logins then call signup api else call verifyOTP screen
        if (loginType == Constants.LoginType.GOOGLE || loginType == Constants.LoginType.FACEBOOK) {
//            callSignupApi()
        } else {
            val intent = Intent(this, VerifyNumberOTPActivity::class.java)
            if (mobileNumber.equals("", ignoreCase = true)) {
                intent.putExtra("countryCode", "")
            } else {
                intent.putExtra("countryCode", countryCode)
            }
            intent.putExtra("countryCodeName", countryCodeName)
            intent.putExtra("country", country)
            intent.putExtra("countryCode", countryCode)
            intent.putExtra("phoneNumber", mobileNumber)
            intent.putExtra("isFromSignUp", true)
//            intent.putExtra("profilePic", profilePic)
            intent.putExtra("register", register)
            startActivity(intent)

        }
    }

    override fun showLoader() {
        progressBar.setVisibility(View.VISIBLE)
    }

    override fun hideLoader() {
        progressBar.visibility = View.GONE
    }

    override fun socialLoginSuccess(response: Login.LoginResponse?) {
        TODO("Not yet implemented")
    }

    override fun socialIdNotRegistered(user: FirebaseUser?, loginType: String?) {
        TODO("Not yet implemented")
    }

    override fun emailalreadyRegistered(email: String?) {
        Toast.makeText(this,"This Email already registerd",Toast.LENGTH_SHORT).show()
    }

    override fun emailNotRegistered(email_number_is_not_registered: String?) {
        presenter?.validate(register)
    }

    override fun userNamealreadyRegistered(userName: String?) {
        Toast.makeText(this,"This username already registerd",Toast.LENGTH_SHORT).show()
    }

    override fun userNameNotRegistered(userName_is_already_in_use: String?) {
        presenter?.verifyIsEmailRegistered(etEmailid.text.toString())
    }

    override fun numberAlreadyRegistered(phoneNumber: String?) {
        Toast.makeText(this,"Number already registered",Toast.LENGTH_SHORT).show()
    }

    override fun numberNotRegistered(mobile_number_is_not_registered: String?) {
    }

    override fun message(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}