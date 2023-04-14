package chat.hola.com.app.Utilities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import chat.hola.com.app.AppController;
import chat.hola.com.app.music.MusicActivity;
import chat.hola.com.app.profileScreen.ProfileActivity;

/**
 * <h1>TagSpannable</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 5/16/2018.
 */

public class TagSpannable extends ClickableSpan {
    private String tag;
    private Context context;
    private String call = "";
    private int color = -1;

    public TagSpannable(Context context, String tag) {
        this.context = context;
        this.tag = tag;
    }

    public TagSpannable(Context context, String tag, int color) {
        this.context = context;
        this.tag = tag;
        this.color = color;
    }

    public TagSpannable(Context context, String tag, String call) {
        this.context = context;
        this.tag = tag;
        this.call = call;
    }

    @Override
    public void onClick(View view) {
        if (AppController.getInstance().isGuest()) {
            AppController.getInstance().openSignInDialog(context);
        } else {
            context.startActivity(tag.contains("#")
                ? new Intent(context, MusicActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("call", "hashtag").putExtra("hashtag", tag).putExtra("from","node")
                : new Intent(context, ProfileActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra(Constants.SocialFragment.USERID, tag.replace("@", "")));
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        // ds.setColor(context.getResources().getColor(call.isEmpty() ? tag.contains("#") ? R.color.colorPrimary : R.color.blue : R.color.color_black));
        if (color != -1)
            ds.setColor(context.getResources().getColor(color));
        ds.setUnderlineText(false);
        Typeface    myTypeface =
            Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Bold.ttf");

        ds.setTypeface(myTypeface);
        //  ds.setFakeBoldText(true);
    }
}
