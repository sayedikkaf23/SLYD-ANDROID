package chat.hola.com.app.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import chat.hola.com.app.AppController
import chat.hola.com.app.authentication.login.LoginActivity
import chat.hola.com.app.authentication.signup.SignUp2Activity
import com.ezcall.android.R
import kotlinx.android.synthetic.main.activity_select_login.*

class SelectLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_login)


        AppController.getInstance().sharedPreferences.edit().putBoolean("isOnBoardingDone", true).apply()

        view_signup.setOnClickListener {

            //directly navigating to signup screen insted of webview activity
            val intent = Intent(this, SignUp2Activity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            intent.putExtra("countryCode", tvRegion.getText().toString())
//            intent.putExtra("countryCodeName", countryCodeName)
//            if (!country.equals("", ignoreCase = true)) {
//                intent.putExtra("country", country)
//            } else {
//                intent.putExtra("country", "IN")
//            }
//            intent.putExtra("phoneNumber", etPhone.getText().toString())
//            intent.putExtra("country", country)
            startActivity(intent)
//            supportFinishAfterTransition()
//            finish()

        }

        btn_login.setOnClickListener {
            val intent2 = Intent(this, LoginActivity::class.java)
            startActivity(intent2)
        }

    }
}