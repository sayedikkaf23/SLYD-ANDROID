package chat.hola.com.app.Utilities;

import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.ezcall.android.R;

import chat.hola.com.app.profileScreen.ProfileActivity;

/**
 * <h1>UserSpannable</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 5/16/2018.
 */

public class UserSpannable extends ClickableSpan {
    private String tag;
    private Context context;

    public UserSpannable(Context context, String tag) {
        this.context = context;
        this.tag = tag;
    }

    @Override
    public void onClick(View view) {
        context.startActivity(new Intent(context, ProfileActivity.class).putExtra(Constants.SocialFragment.USERID, tag.replace("@", "")));
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(context.getResources().getColor(R.color.colorPrimary));
        ds.setUnderlineText(false);
    }
}
