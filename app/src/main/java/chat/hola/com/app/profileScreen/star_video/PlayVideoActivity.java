package chat.hola.com.app.profileScreen.star_video;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.ezcall.android.R;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PlayVideoActivity extends AppCompatActivity {

    @BindView(R.id.videoView)
    VideoView videoView;
    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        unbinder = ButterKnife.bind(this);

        String vidAddress = "https://res.cloudinary.com/dafszph29/video/upload/q_auto/v1552031624/default/videoplayback.mp4";
        Uri vidUri = Uri.parse(vidAddress);
        videoView.setVideoURI(vidUri);
        videoView.start();

        MediaController vidControl = new MediaController(this);
        vidControl.setAnchorView(videoView);
        videoView.setMediaController(vidControl);
    }

    @OnClick(R.id.close)
    public void close() {
        finish();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
