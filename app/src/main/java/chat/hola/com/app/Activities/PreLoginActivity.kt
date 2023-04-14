package chat.hola.com.app.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import chat.hola.com.app.authentication.login.LoginActivity
import com.ezcall.android.R
import kotlinx.android.synthetic.main.activity_pre_login.*

class PreLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_login)


        btn_sign_in.setOnClickListener {
            val intent2 = Intent(this, LoginActivity::class.java)
            startActivity(intent2)
            finish()
        }
    }
}