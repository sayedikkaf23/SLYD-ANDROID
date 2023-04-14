package chat.hola.com.app.Utilities;

import android.content.Context;
import android.util.Base64;

import java.io.IOException;

/**
 * Created by ankit on 1/3/18.
 */

public class CountryCodeUtil {

    public static String readEncodedJsonString(Context context)
            throws IOException {
//        String base64 = context.getResources().getString(R.string.countries_code);
        byte[] data = Base64.decode(CountryCode.ENCODED_COUNTRY_CODE, Base64.DEFAULT);
        return new String(data, "UTF-8");
    }

}
