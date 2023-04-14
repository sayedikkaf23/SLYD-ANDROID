package chat.hola.com.app.Activities;

import androidx.appcompat.app.AppCompatActivity;
import chat.hola.com.app.AppController;

import android.os.Bundle;

import com.ezcall.android.R;

public class CallFinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_finish);
        AppController.getInstance().setApplicationKilled(true);
        supportFinishAfterTransition();
    }
}
