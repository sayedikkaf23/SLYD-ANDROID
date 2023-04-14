package chat.hola.com.app.authentication.signup

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import chat.hola.com.app.Dialog.ContactPermissionDialog
import com.ezcall.android.R
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_privacy_settings.*

class PrivacySettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_settings)

        btn_allow.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this@PrivacySettingsActivity,
                    Manifest.permission.READ_CONTACTS
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                val permissionDialog = ContactPermissionDialog(this)
                permissionDialog.show()
                val btnAccept = permissionDialog.findViewById<Button>(R.id.btnContinue)
                btnAccept.setOnClickListener { v: View? ->
                    permissionDialog.dismiss()
                    permission()
                    startActivity(Intent(this, CongratulationsActivity::class.java))
                }
            } else {
                // permission already granted
                permission()
                startActivity(Intent(this, CongratulationsActivity::class.java))
            }
        }
    }

    fun permission() {
        fun permission() {
            Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_CONTACTS)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        // permission is granted, open the camera
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied) {
                            gotoSettings()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()
        }

    }

    fun gotoSettings() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("We need permission to access your contact list, please grant")
        builder.setPositiveButton("Setting") { dialogInterface: DialogInterface?, i: Int ->
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        builder.show()
    }
}