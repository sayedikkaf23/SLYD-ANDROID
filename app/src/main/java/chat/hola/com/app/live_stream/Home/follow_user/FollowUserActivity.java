package chat.hola.com.app.live_stream.Home.follow_user;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.base.BaseActivity;
import chat.hola.com.app.live_stream.ResponcePojo.AllStreamsData;

public class FollowUserActivity extends BaseActivity implements FollowUserContract.View {

    private AllStreamsData data;
    @BindView(R.id.ivThumbnail)
    ImageView ivThumbnail;
    @BindView(R.id.ivProfilePic)
    ImageView ivProfilePic;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.btnFollow)
    ToggleButton btnFollow;

    @Inject
    Loader loader;
    @Inject
    FollowUserPresenter presenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_follow_user;
    }

    @Override
    public void initView() {
        super.initView();
        data = (AllStreamsData) getIntent().getSerializableExtra("data");
        presenter.attach(this);
        if (data != null) {
            tvName.setText(data.getUserName());

            if (data.isFollowing()) {
                btnFollow.setTextOn(data.getFollowStatus() == 2 ? "Requested" : "Following");
            } else if (data.isPrivate()) {
                btnFollow.setTextOn("Requested");
            }
            btnFollow.setChecked(data.isFollowing());


            Glide.with(this).load(data.getThumbnail()).asBitmap().into(ivThumbnail);

            Glide.with(this).load(data.getUserImage()).asBitmap().centerCrop().signature(new StringSignature(String.valueOf(System.currentTimeMillis()))).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(new BitmapImageViewTarget(ivProfilePic) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ivProfilePic.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }

        btnFollow.setOnCheckedChangeListener((compoundButton, b) -> {
            if (btnFollow.isChecked()) {
                presenter.follow(data.getUserId());
            } else {
                presenter.unFollow(data.getUserId());
            }
        });
    }

    @Override
    protected void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }

    @OnClick(R.id.btnClose)
    public void close() {
        super.onBackPressed();
    }

    @Override
    public void message(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void message(int message) {
        message(getString(message));
    }

    @Override
    public void showLoader() {
        loader.show();
    }

    @Override
    public void hideLoader() {
        if (loader != null && loader.isShowing())
            loader.dismiss();
    }
}
