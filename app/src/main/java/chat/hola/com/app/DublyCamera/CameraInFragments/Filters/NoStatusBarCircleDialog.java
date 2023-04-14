package chat.hola.com.app.DublyCamera.CameraInFragments.Filters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ezcall.android.R;

/**
 * <h2>NoStatusBarCircleDialog</h2>
 * <P>
 *     No status bar circle progress dialog in android.
 * </P>
 * @since  1/23/2018.
 * @author 3Embed.
 */
public class NoStatusBarCircleDialog
{
    private static NoStatusBarCircleDialog CIRCLE_ALERET=new NoStatusBarCircleDialog();
    private Dialog progress_bar=null;
    private TextView progress_bar_title=null;
    private Activity activity;
    LayoutInflater inflater = null;
    private NoStatusBarCircleDialog()
    {}

    public static NoStatusBarCircleDialog getInstance()
    {
        if(CIRCLE_ALERET==null)
        {
            CIRCLE_ALERET=new NoStatusBarCircleDialog();
            return CIRCLE_ALERET;
        }else
        {
            return CIRCLE_ALERET;
        }
    }

    /**
     * <h2>get_Circle_Progress_bar</h2>
     * <P>
     *
     * </P>*/
    public Dialog get_Circle_Progress_bar(Activity mactivity)
    {
        this.activity=mactivity;
        inflater = activity.getLayoutInflater();
        if(progress_bar!=null)
        {
            if(progress_bar.isShowing())
            {
                progress_bar.dismiss();
            }
        }
        progress_bar = new Dialog(mactivity, R.style.NoStatusBarDialog);
        Window window=progress_bar.getWindow();
        assert window != null;
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        @SuppressLint("InflateParams")
        View dialogView = inflater.inflate(R.layout.circle_progress_bar_layout, null);
        progress_bar_title= dialogView.findViewById(R.id.progress_title);
        progress_bar.setContentView(dialogView);
        progress_bar.setCancelable(true);
        progress_bar.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            public void onCancel(DialogInterface dialog)
            {
                //Canceled..
            }
        });
        return progress_bar;
    }


    public void set_Progress_title(final String text)
    {
        if(progress_bar_title!=null&&text!=null)
        {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress_bar_title.setText(text);
                }
            });

        }

    }

    public void set_Title_Color(final int color_code)
    {
        if(progress_bar_title!=null)
        {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run()
                {
                    progress_bar_title.setTextColor(color_code);
                }
            });

        }
    }


    public void set_Title_Text_Style(final Typeface title_text_style)
    {
        if(progress_bar_title!=null)
        {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress_bar_title.setTypeface(title_text_style);
                }
            });

        }
    }

}
