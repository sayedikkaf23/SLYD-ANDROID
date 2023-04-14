package chat.hola.com.app.authentication.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import chat.hola.com.app.home.LandingActivity
import chat.hola.com.app.profileScreen.discover.DiscoverActivity
import com.ezcall.android.R
import kotlinx.android.synthetic.main.activity_congratulations.*

class CongratulationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_congratulations)

        btn_go_home.setOnClickListener {
            val i = Intent(this, DiscoverActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.putExtra("caller", "SaveProfile")
            startActivity(i)
            finish()
        }
    }
}