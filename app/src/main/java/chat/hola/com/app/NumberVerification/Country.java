package chat.hola.com.app.NumberVerification;

/*
 * Created by moda on 30/12/16.
 */


/*
*
*
* POJO class for the list of countries*/

import android.util.Log;

import com.ezcall.android.R;

import java.lang.reflect.Field;
import java.util.Locale;

public class Country {

    private int flag;
    private String code;
    private String name;
    private String dialCode;
    private int maxDigits;

    public String getDialCode() {
        return dialCode;
    }

    public void setDialCode(String dialCode) {
        this.dialCode = dialCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getFlag() {
        String drawableName = "flag_"
                + code.toLowerCase(Locale.ENGLISH);
        return getResId(drawableName);
    }

    private int getResId(String drawableName) {

        try {
            Field field = (R.drawable.class).getField(drawableName);
            return field.getInt(null);
        } catch (Exception e) {
            Log.e("CountryCodePicker", "Failure to get drawable id.", e);
        }
        return -1;
    }

    public int getMaxDigits() {
        return maxDigits;
    }

    public void setMaxDigits(int maxDigits) {
        this.maxDigits = maxDigits;
    }
}