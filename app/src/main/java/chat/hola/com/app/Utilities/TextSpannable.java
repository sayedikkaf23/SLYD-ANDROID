package chat.hola.com.app.Utilities;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;

import androidx.annotation.NonNull;

import chat.hola.com.app.Database.View;

/**
 * <h1></h1>
 * <p></p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 23 Dec 2019
 */
public class TextSpannable extends ClickableSpan {

    private boolean isUnderline = false;

    /**
     * Constructor
     */
    public TextSpannable(boolean isUnderline) {
        this.isUnderline = isUnderline;
    }

    @Override
    public void onClick(@NonNull android.view.View widget) {

    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(isUnderline);
        ds.setColor(Color.parseColor("#0066cc"));

    }

}
