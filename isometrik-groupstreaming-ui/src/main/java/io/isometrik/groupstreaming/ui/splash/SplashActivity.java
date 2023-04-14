package io.isometrik.groupstreaming.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.streams.grid.StreamsActivity;
import io.isometrik.groupstreaming.ui.users.UsersActivity;

/**
 * The type Splash activity.
 */

public class SplashActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent;
    if (IsometrikUiSdk.getInstance().getUserSession().getUserId() == null) {

      intent = new Intent(SplashActivity.this, UsersActivity.class);
    } else {
      intent = new Intent(SplashActivity.this, StreamsActivity.class);
    }
    startActivity(intent);
    finish();
  }
}
