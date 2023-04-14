package chat.hola.com.app.live_stream.utility;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by moda on 12/21/2018.
 */
public class Utility
{
    public static void hideKeyboard(Activity mcontext) {
        try{
            InputMethodManager inputManager = (InputMethodManager) mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(mcontext.getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }catch (NullPointerException e)
        {

        }

    }
    public static void showKeyBoard(Activity mContext)
    {
        try {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        }catch (Exception e)
        {

        }
    }









}
