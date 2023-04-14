package chat.hola.com.app.Doodle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.ezcall.android.R;


/**
 * Created by embed on 4/11/16.
 */
public class DoodlePop extends PopupWindow

{

    private View rootView;
    private Context mContext;
    private Boolean isOpened = false;
    private int keyBoardHeight = 0;
    private Boolean pendingOpen = false;
    private DrawView drawView;
    private DoodleAction doddleAction;
    private RelativeLayout mainLinearLayout, colourId;
    private ImageView redButton, blackButton, greenButton, blueButton;


    public DoodlePop(View rootView, Context mContext, DoodleAction doodleAction) {
        super(mContext);
        this.mContext = mContext;
        this.rootView = rootView;
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        this.doddleAction = doodleAction;
        View customView = createCustomView();
        setContentView(customView);


        //default size
        setSize((int) mContext.getResources().getDimension(R.dimen.keyboard_height), WindowManager.LayoutParams.MATCH_PARENT);
    }


    private View createCustomView() {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.doddle_layout, null, false);
        mainLinearLayout = (RelativeLayout) view.findViewById(R.id.doodleLayout);
        colourId = (RelativeLayout) view.findViewById(R.id.coloursLayout);
        drawView = new DrawView(mContext);
        mainLinearLayout.addView(drawView);


        redButton = (ImageView) view.findViewById(R.id.redColour);
        blackButton = (ImageView) view.findViewById(R.id.blackColour);
        greenButton = (ImageView) view.findViewById(R.id.greenColour);
        blueButton = (ImageView) view.findViewById(R.id.blueColour);

        blackButton.setSelected(true);
        redButton.setSelected(false);
        greenButton.setSelected(false);
        blueButton.setSelected(false);
        redButton.setClickable(true);
        blackButton.setClickable(true);
        greenButton.setClickable(true);
        blueButton.setClickable(true);
        blackButton.setSelected(true);

        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                blackButton.setSelected(false);
                redButton.setSelected(true);
                greenButton.setSelected(false);
                blueButton.setSelected(false);
                drawView.mPaint.setColor(ContextCompat.getColor(mContext, R.color.doodle_color_red));
            }
        });


        blackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blackButton.setSelected(true);
                redButton.setSelected(false);
                greenButton.setSelected(false);
                blueButton.setSelected(false);
                drawView.mPaint.setColor(ContextCompat.getColor(mContext, R.color.doodle_color_black));
            }
        });


        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blackButton.setSelected(false);
                redButton.setSelected(false);
                greenButton.setSelected(false);
                blueButton.setSelected(true);
                drawView.mPaint.setColor(ContextCompat.getColor(mContext, R.color.doodle_color_blue));
            }
        });

        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blackButton.setSelected(false);
                redButton.setSelected(false);
                greenButton.setSelected(true);
                blueButton.setSelected(false);
                drawView.mPaint.setColor(ContextCompat.getColor(mContext, R.color.doodle_color_green));
            }
        });

        final ImageView deletebutton = (ImageView) view.findViewById(R.id.deleteDoddle);

        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainLinearLayout.removeAllViews();
                drawView = new DrawView(mContext);
                mainLinearLayout.addView(drawView);
                mainLinearLayout.addView(colourId);
                redButton.setClickable(true);
                blackButton.setClickable(true);
                greenButton.setClickable(true);
                blueButton.setClickable(true);
                blackButton.setSelected(true);
                redButton.setSelected(false);
                greenButton.setSelected(false);
                blueButton.setSelected(false);
                drawView.mPaint.setColor(Color.parseColor("#000000"));

            }
        });


        final ImageView colorSelected = (ImageView) view.findViewById(R.id.sendDoddle);

        colorSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Bitmap imageOnView = ConvertToBitmap();


                doddleAction.doodleBitmap(imageOnView);
                mainLinearLayout.removeAllViews();
                mainLinearLayout.invalidate();

                mainLinearLayout.addView(drawView);
                mainLinearLayout.addView(colourId);
                redButton.setClickable(true);
                blackButton.setClickable(true);
                greenButton.setClickable(true);
                blueButton.setClickable(true);
                blackButton.setSelected(true);
                redButton.setSelected(false);
                greenButton.setSelected(false);
                blueButton.setSelected(false);
                drawView.mPaint.setColor(Color.parseColor("#000000"));

            }
        });

        return view;
    }

    protected Bitmap ConvertToBitmap() {
        return drawView.getmBitmap();
    }

    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    public void showAtBottom() {
        showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * Use this function when the soft keyboard has not been opened yet. This
     * will show the emoji popup after the keyboard is up next time.
     * Generally, you will be calling InputMethodManager.showSoftInput function after
     * calling this function.
     */
    public void showAtBottomPending() {
        if (isKeyBoardOpen())
            showAtBottom();
        else
            pendingOpen = true;
    }

    public Boolean isKeyBoardOpen() {
        return isOpened;
    }

    public void setSizeForSoftKeyboard() {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);

                int screenHeight = getUsableScreenHeight();
                int heightDifference = screenHeight
                        - (r.bottom - r.top);
                int resourceId = mContext.getResources()
                        .getIdentifier("status_bar_height",
                                "dimen", "android");
                if (resourceId > 0) {
                    heightDifference -= mContext.getResources()
                            .getDimensionPixelSize(resourceId);
                }
                if (heightDifference > 100) {
                    keyBoardHeight = heightDifference;
                    setSize(WindowManager.LayoutParams.MATCH_PARENT, keyBoardHeight);
                    if (!isOpened) {
                        /*if(onSoftKeyboardOpenCloseListener!=null)
                            onSoftKeyboardOpenCloseListener.onKeyboardOpen(keyBoardHeight);*/

                        doddleAction.keyboardOpen();
                    }
                    isOpened = true;
                    if (pendingOpen) {
                        showAtBottom();
                        pendingOpen = false;
                    }
                } else {
                    isOpened = false;

                    doddleAction.KeyboardClose();
                    mainLinearLayout.removeAllViews();
                    mainLinearLayout.invalidate();

                    drawView = new DrawView(mContext);
                    mainLinearLayout.addView(drawView);
                    mainLinearLayout.addView(colourId);
                    redButton.setClickable(true);
                    blackButton.setClickable(true);
                    greenButton.setClickable(true);
                    blueButton.setClickable(true);

                }
            }
        });
    }

    private int getUsableScreenHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();

            WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);

            return metrics.heightPixels;

        } else {
            return rootView.getRootView().getHeight();
        }
    }

}
