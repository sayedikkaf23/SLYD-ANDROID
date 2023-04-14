package chat.hola.com.app.home.social.model;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ezcall.android.R;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.TypefaceManager;

/**
 * <h1>ViewHolder</h1>
 *
 * @author 3Embed
 * @since 24/2/2018.
 */

public class AdViewHolder extends RecyclerView.ViewHolder {
    // Create native UI using the ad metadata.
    @BindView(R.id.ad_choices_container)
    LinearLayout adChoicesContainer;
    @BindView(R.id.native_ad_title)
    TextView nativeAdTitle;
    @BindView(R.id.native_ad_social_context)
    TextView nativeAdSocialContext ;
    @BindView(R.id.native_ad_body)
    TextView nativeAdBody;
    @BindView(R.id.native_ad_sponsored_label)
    TextView sponsoredLabel;
    @BindView(R.id.native_ad_call_to_action)
    Button nativeAdCallToAction ;

    public AdViewHolder(View itemView, TypefaceManager typefaceManager) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
    }
}