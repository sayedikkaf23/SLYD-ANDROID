package chat.hola.com.app.models;

import android.content.Context;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.ezcall.android.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 6/4/2018.
 */

public class InternetErrorView extends LinearLayout {

    private ErrorListner errorListner;

    public InternetErrorView(Context context) {
        super(context);
        init();
    }

    public InternetErrorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InternetErrorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public InternetErrorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER);
        setBackgroundColor(getContext().getResources().getColor(R.color.color_line_gray));
        LayoutInflater.from(getContext()).inflate(R.layout.view_error, this);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button_reload)
    public void reloadButton() {
        errorListner.reload();
    }

    public void setErrorListner(ErrorListner errorListner) {
        this.errorListner = errorListner;
    }

    public interface ErrorListner {
        void reload();
    }
}
