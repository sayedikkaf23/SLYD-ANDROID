package chat.hola.com.app.Utilities;

/**
 * Created by moda on 04/05/17.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.Formatter;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatDrawableManager;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import chat.hola.com.app.AppController;
import chat.hola.com.app.coin.base.CoinActivity;
import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import io.card.payment.CardType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Credentials;
import org.json.JSONArray;

import static android.content.Context.WIFI_SERVICE;
import static chat.hola.com.app.Utilities.Constants.FOUR;
import static chat.hola.com.app.Utilities.Constants.GET_IP_ADDRESS;
import static chat.hola.com.app.Utilities.Constants.ZERO;

public class Utilities {
    private static Handler sUiThreadHandler;
    public static DecimalFormat df = new DecimalFormat("#.##");
    public static int PAYMENT_TYPE_VALUE = 0;
    public static String CARD_BRAND = "";
    public static String SELECTED_CARD_ID = "";
    public static String SELECTED_CARD_LAST_FOUR_NO = "";

    /**
     * Run the {@code Runnable} on the UI main thread.
     *
     * @param runnable the runnable
     */
    public static void runOnUiThread(Runnable runnable) {
        if (sUiThreadHandler == null) {
            sUiThreadHandler = new Handler(Looper.getMainLooper());
        }
        sUiThreadHandler.post(runnable);
    }

    public static String tsInGmt() {

        Date localTime =
                new Date(System.currentTimeMillis() - AppController.getInstance().getTimeDelta());

        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmssSSS z", Locale.US);
        formater.setTimeZone(TimeZone.getTimeZone("GMT"));

        String s = formater.format(localTime);

        return s;
    }

    //converting time to localtime zone from gmt time

    public static String tsFromGmt(String tsingmt) {

        Date d = null;
        String s = null;

        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmssSSS z", Locale.US);

        try {
            d = formater.parse(tsingmt);
            TimeZone tz = TimeZone.getDefault();
            formater.setTimeZone(tz);
            assert d != null;
            s = formater.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return s;
    }

    /**
     * return date format
     *
     * @param time time stamp
     * @return return time string formate
     */
    public static String getDateForHistoryDet(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000L);
        return DateFormat.format("dd MMMM yyyy hh:mm", cal).toString();
    }

    public static String getThings() {
        return Credentials.basic(BuildConfig.abba, BuildConfig.dabba);
    }

    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String getModifiedImageLink(String url) {
        if (url != null && url.contains("cloudinary")) {
            String extension = url.substring(url.lastIndexOf("."));
            url = url.replace(extension, Constants.IMAGE_EXT);
            return url;
        } else {
            return url;
        }
    }

    public static String getModifiedThumbnailLink(String url) {
        if (url.contains("cloudinary")) {

            if (!url.contains("upload/t_media_lib_thumb")) {

                url = url.replace("upload", "upload/t_media_lib_thumb");
            }

            return url.replace(".jpg", ".webp").replace(".jpeg", ".webp").replace(".png", ".webp");
        } else {
            return url;
        }
    }

    public static String getModifiedVideoLink(String url) {
        return url.replace("upload/", "upload/vc_auto/");
    }

    public static int getDrawableByString(Context mContext, String name) {
        return mContext.getResources().getIdentifier(name, "drawable", mContext.getPackageName());
    }

    public static void openInsufficientBalanceDialog(Context context) {
        BottomSheetDialog dialog = new BottomSheetDialog(context);
        dialog.setContentView(R.layout.dialog_insufficient_balance);
        Button btnAdd = dialog.findViewById(R.id.btn_add_coin);
        btnAdd.setOnClickListener(v -> {
            context.startActivity(new Intent(context, CoinActivity.class));
            dialog.dismiss();
        });
        dialog.show();
    }

    public static String getDateDDMMYYYY(String tsingmt) {
        try {
            Date d = null;
            String s = null;
            long epoch = 0;

            SimpleDateFormat formater = new SimpleDateFormat("dd MMM yyyy");
            formater.setTimeZone(TimeZone.getDefault());
            epoch = Long.parseLong(tsingmt);

            d = new Date(epoch);
            s = formater.format(d);

            return s;
        } catch (Exception e) {
            return tsingmt;
        }
    }

    public static boolean isValidWebsite(String newValue) {
        String URL_REGEX = "(www.)?"
                + "[a-zA-Z0-9@:%._\\+~#?&//=]{2,256}\\.[a-z]"
                + "{2,6}\\b([-a-zA-Z0-9@:%._\\+~#?&//=]*)";
        Pattern p = Pattern.compile(URL_REGEX);
        Matcher m = p.matcher(newValue);
        return m.find();
//        return Patterns.WEB_URL.matcher(newValue).matches();
    }

    public String gmtToEpoch(String tsingmt) {

        Date d = null;

        long epoch = 0;

        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmssSSS z", Locale.US);

        try {
            d = formater.parse(tsingmt);

            epoch = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return String.valueOf(epoch);
    }

    public static long Daybetween(String date1, String date2, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);

        sdf.setTimeZone(TimeZone.getDefault());

        Date startDate = null, endDate = null;
        try {
            startDate = sdf.parse(date1);
            endDate = sdf.parse(date2);
        } catch (Exception e) {
            e.printStackTrace();

            sdf = new SimpleDateFormat(pattern.substring(0, 19), Locale.US);
            try {
                startDate = sdf.parse(date1);
                endDate = sdf.parse(date2);
            } catch (ParseException ef) {
                ef.printStackTrace();
            }
        }

        Calendar sDate = getDatePart(startDate);
        Calendar eDate = getDatePart(endDate);

        long daysBetween = 0;
        while (sDate.before(eDate)) {
            sDate.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    public static Calendar getDatePart(Date date) {
        Calendar cal = Calendar.getInstance();       // get calendar instance
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
        cal.set(Calendar.MINUTE, 0);                 // set minute in hour
        cal.set(Calendar.SECOND, 0);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);            // set millisecond in second

        return cal;                                  // return the date part
    }

    public static String formatDate(String ts) {

        String s = null;
        Date d = null;

        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmssSSS z", Locale.US);

        try {
            d = formater.parse(ts);

            formater = new SimpleDateFormat("HH:mm:ss EEE dd/MMM/yyyy z", Locale.US);
            s = formater.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return s;
    }

    public static String epochtoGmt(String tsingmt) {

        Date d = null;
        String s = null;
        long epoch = 0;

        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmssSSS z", Locale.US);
        formater.setTimeZone(TimeZone.getTimeZone("GMT"));
        epoch = Long.parseLong(tsingmt);

        d = new Date(epoch);
        s = formater.format(d);

        return s;
    }

    public static String changeStatusDateFromGMTToLocal(String ts) {

        String s = null;
        Date d;

        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmssSSS z", Locale.US);

        try {
            d = formater.parse(ts);

            TimeZone tz = TimeZone.getDefault();
            formater.setTimeZone(tz);

            s = formater.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return s;
    }

    /**
     * To get the epoch time of the current time in gmt
     */

    public static long getGmtEpoch() {

        Date localTime =
                new Date(System.currentTimeMillis() - AppController.getInstance().getTimeDelta());

        Date d;

        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmssSSS z", Locale.US);
        formater.setTimeZone(TimeZone.getTimeZone("GMT"));

        String tsingmt = formater.format(localTime);

        long epoch = 0;

        try {
            d = formater.parse(tsingmt);

            epoch = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return epoch;
    }

    /*
     * Get calls time based on the timezone
     */
    public static String tsFromGmtToLocalTimeZone(String tsingmt) {

        String s = null;
        Date d = null;

        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmmssSSS z", Locale.US);

        try {
            d = formater.parse(tsingmt);

            formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
            formater.setTimeZone(TimeZone.getDefault());

            s = formater.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return s;
    }

    /*
     * to convert string from the 24 hour format to 12 hour format
     */
    public static String convert24to12hourformat(String d) {

        String datein12hour = null;

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm", Locale.US);
            final Date dateObj = sdf.parse(d);

            datein12hour = new SimpleDateFormat("h:mm a", Locale.US).format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }

        return datein12hour;
    }

    public static String findOverlayDate(String date) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("EEE dd/MMM/yyyy", Locale.US);

            String m1 = "", m2 = "";

            String month1, month2;

            String d1, d2;

            d1 = sdf.format(
                    new Date(System.currentTimeMillis() - AppController.getInstance().getTimeDelta()));

            d2 = date;

            month1 = d1.substring(7, 10);

            month2 = d2.substring(7, 10);

            if (month1.equals("Jan")) {
                m1 = "01";
            } else if (month1.equals("Feb")) {
                m1 = "02";
            } else if (month2.equals("Mar")) {
                m2 = "03";
            } else if (month1.equals("Apr")) {
                m1 = "04";
            } else if (month1.equals("May")) {
                m1 = "05";
            } else if (month1.equals("Jun")) {
                m1 = "06";
            } else if (month1.equals("Jul")) {
                m1 = "07";
            } else if (month1.equals("Aug")) {
                m1 = "08";
            } else if (month1.equals("Sep")) {
                m1 = "09";
            } else if (month1.equals("Oct")) {
                m1 = "10";
            } else if (month1.equals("Nov")) {
                m1 = "11";
            } else if (month1.equals("Dec")) {
                m1 = "12";
            }

            if (month2.equals("Jan")) {
                m2 = "01";
            } else if (month2.equals("Feb")) {
                m2 = "02";
            } else if (month1.equals("Mar")) {
                m1 = "03";
            } else if (month2.equals("Apr")) {
                m2 = "04";
            } else if (month2.equals("May")) {
                m2 = "05";
            } else if (month2.equals("Jun")) {
                m2 = "06";
            } else if (month2.equals("Jul")) {
                m2 = "07";
            } else if (month2.equals("Aug")) {
                m2 = "08";
            } else if (month2.equals("Sep")) {
                m2 = "09";
            } else if (month2.equals("Oct")) {
                m2 = "10";
            } else if (month2.equals("Nov")) {
                m2 = "11";
            } else if (month2.equals("Dec")) {
                m2 = "12";
            }
            month1 = null;
            month2 = null;

            if (sdf.format(
                    new Date(System.currentTimeMillis() - AppController.getInstance().getTimeDelta()))
                    .equals(date)) {

                m2 = null;
                m1 = null;
                d2 = null;
                d1 = null;
                sdf = null;
                return "Today";
            } else if ((Integer.parseInt(d1.substring(11) + m1 + d1.substring(4, 6)) - Integer.parseInt(
                    d2.substring(11) + m2 + d2.substring(4, 6))) == 1) {

                m2 = null;
                m1 = null;
                d2 = null;
                d1 = null;
                sdf = null;
                return "Yesterday";
            } else {

                m2 = null;
                m1 = null;
                d2 = null;
                d1 = null;
                sdf = null;
                return date;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return date;
        }
    }

    /**
     * To remove an element from the jsonarray even below api level 19
     */
    public static JSONArray removeElementFromJSONArray(JSONArray jarray, int pos) {

        if (Build.VERSION.SDK_INT >= 19) {

            jarray.remove(pos);
            return jarray;
        }

        JSONArray Njarray = new JSONArray();
        try {
            for (int i = 0; i < jarray.length(); i++) {
                if (i != pos) Njarray.put(jarray.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Njarray;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    private static final NavigableMap<Double, String> suffixes = new TreeMap<>();

    static {
        suffixes.put(1_000D, "K");
        suffixes.put(1_000_000D, "M");
        suffixes.put(1_000_000_000D, "G");
        suffixes.put(1_000_000_000_000D, "T");
        suffixes.put(1_000_000_000_000_000D, "P");
        suffixes.put(1_000_000_000_000_000_000D, "E");
    }

    public static String formatMoney(double value) {
        //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
        if (value == Long.MIN_VALUE) return formatMoney(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + formatMoney(-value);
        if (value < 1000) return Double.toString(value); //deal with easy case

        Map.Entry<Double, String> e = suffixes.floorEntry(value);
        Double divideBy = e.getKey();
        String suffix = e.getValue();

        double truncated = value / (divideBy / 10); //the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        String b = hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
        String lastChar = b.substring(b.length() - 1);

        double balance = Double.parseDouble(b.replace(lastChar, ""));
        return df.format(balance) + "" + lastChar;
    }

    public static String stringDateFormat(String date) {
        try {

            TimeZone tz = TimeZone.getDefault();

            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
            SimpleDateFormat input = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            Date d = input.parse(date.replace("T", " ").replace("Z", " "));
            output.setTimeZone(tz);
            return output.format(d);
        } catch (Exception e) {
            return date;
        }
    }

    public static String getDate(String tsingmt) {
        Date d = null;
        String s = null;
        long epoch = 0;

        SimpleDateFormat formater = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
//    formater.setTimeZone(TimeZone.getTimeZone("GMT"));
        formater.setTimeZone(TimeZone.getDefault());
        epoch = Long.parseLong(tsingmt);

        d = new Date(epoch);
        s = formater.format(d);

        return s;
    }

    @SuppressLint("PrivateResource")
    public static Bitmap setCreditCardLogo(String cardMethod, Context context) {
        Bitmap anImage;
        switch (cardMethod) {
            case "Visa":
            case "visa":
                anImage = CardType.VISA.imageBitmap(context);//getBitmapFromVectorDrawable(context, CardBrand.Visa.getIcon());
                break;
            case "MasterCard":
            case "mastercard":
                anImage = CardType.MASTERCARD.imageBitmap(context);//getBitmapFromVectorDrawable(context, CardBrand.MasterCard.getIcon());
                break;
            case "American Express":
            case "amex":
                anImage = CardType.AMEX.imageBitmap(context);//getBitmapFromVectorDrawable(context, CardBrand.AmericanExpress.getIcon());
                break;
            case "Discover":
            case "discover":
                anImage = CardType.DISCOVER.imageBitmap(context); //getBitmapFromVectorDrawable(context, CardBrand.Discover.getIcon());
                break;
            case "Diners Club":
            case "diners":
                anImage = CardType.DINERSCLUB.imageBitmap(context);//getBitmapFromVectorDrawable(context, CardBrand.DinersClub.getIcon());
                break;

            case "JCB":
            case "jcb":
                anImage = CardType.JCB.imageBitmap(context);//getBitmapFromVectorDrawable(context, CardBrand.JCB.getIcon());
                break;

            case "unionpay":
                anImage = CardType.MAESTRO.imageBitmap(context);//getBitmapFromVectorDrawable(context, CardBrand.UnionPay.getIcon());
                break;

            case "Cash":
                anImage = getBitmapFromVectorDrawable(context, R.drawable.ic_cash_icon);
                break;
            case "Wallet":
                anImage = getBitmapFromVectorDrawable(context,
                        R.drawable.ic_account_balance_wallet_black_24dp);
                break;

            default:
                anImage = CardType.UNKNOWN.imageBitmap(context);//getBitmapFromVectorDrawable(context, R.drawable.visa_card);
                break;
        }
        return anImage;
    }

    @SuppressLint("RestrictedApi")
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static int bitrateValue() {
        int bitrate = 0;
        NetworkInfo info = getNetworkInfo(AppController.getInstance());
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {

            switch (info.getSubtype()) {

                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    bitrate = 128000;
                    break; // ~ 50-100 -Kbps slow-----
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    bitrate = 128000;
                    break; // ~ 14-64 -Kbps slow-----
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    bitrate = 128000;
                    break; // ~ 400-1000 -Kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    bitrate = 128000;
                    break; // ~ 600-1400 -Kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    bitrate = 128000;
                    break; // ~ 100 -Kbps slow-----
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    bitrate = 300000;
                    break; // ~ 2000-1400 -Kbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    bitrate = 128000;
                    break; // ~ 700-1700 -Kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    bitrate = 200000;
                    break; // ~ 1000-2300 -Kbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    bitrate = 200000;
                    break; // ~ 400-7000 -Kbps
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                    bitrate = 200000;
                    break; // ~ 1000-2000 -Kbps // API level 11
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    bitrate = 200000;
                    break; // ~ 5000 -Kbps // API level 9
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    bitrate = 200000;
                    break; // ~ 10000-20000-Kbps // API level 13
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    bitrate = 128000;
                    break; // ~ 25 -Kbps // API level 8
                case TelephonyManager.NETWORK_TYPE_LTE:
                    bitrate = 200000;
                    break; // ~ 10000+ -Kbps // API level 11
                case TelephonyManager.NETWORK_TYPE_UNKNOWN: // ~ Unknown
                    bitrate = 128000;
                    break;
                default:
                    break;
            }
        } else {
            bitrate = 2000000;
        }

        return bitrate;
    }

    /**
     * Get network info
     *
     * @param context - For getting connectivity service
     * @return NetworkInfo
     */
    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
    }

    public static String getHashCode(Context context) {
        AppSignatureHelper appSignature = new AppSignatureHelper(context);
        Log.e("asdfghjkl", "" + appSignature.getAppSignatures());
        Log.e(" asdfghjkl", "" + appSignature.getAppSignatures().get(0));
        return appSignature.getAppSignatures().get(0);
    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (Exception e) {
            Log.e("", "printHashKey()", e);
        }
    }


    public static void initSmsRetriever(Activity activity) {
        // Get an instance of SmsRetrieverClient, used to start listening for a matching
// SMS message.
        SmsRetrieverClient client = SmsRetriever.getClient(activity /* context */);

// Starts SmsRetriever, which waits for ONE matching SMS message until timeout
// (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
// action SmsRetriever#SMS_RETRIEVED_ACTION.
        Task<Void> task = client.startSmsRetriever();

// Listen for success/failure of the start Task. If in a background thread, this
// can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                // ...
                Log.d("SMS RETRIEVER", "onSuccess: ");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                // ...
                Log.d("SMS RETRIEVER", "onFailed: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * return if array is empty or not
     *
     * @param object List
     * @return boolean
     */
    public static boolean isEmptyArray(List object) {
        return object == null || object.size() == ZERO;
    }

    public static void printLog(String msg) {
        Log.i("Delivx", "msg  " + msg);
    }

    public static String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    public static void strikeThroughText(TextView price) {
        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public static String getDeviceId(Context context) {
        // use the ANDROID_ID constant, generated at the first device boot
        String deviceId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        // in case known problems are occured
        if ("9774d56d682e549c".equals(deviceId) || deviceId == null) {
            // getProductList a unique deviceID like IMEI for GSM or ESN for CDMA phones
            // don't forget:
            //
            deviceId = Build.SERIAL;
            // if nothing else works, generate a random number
            if (deviceId == null) {
                Random tmpRand = new Random();
                deviceId = String.valueOf(tmpRand.nextLong());
            }
        }
        return deviceId;
    }

    /**
     * used to get the ip address of the network which we have connected.
     *
     * @return ip address
     */
    public static String getIpAddress() {
        String fullString = "";
        URL url = null;
        try {
            url = new URL(GET_IP_ADDRESS);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String line = null;
        while (true) {
            try {
                if (reader != null && !((line = reader.readLine()) != null)) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            fullString += line;
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fullString;
    }

    public static String getIpAddress(Context context) {
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                     enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
            try {
                WifiManager wm = (WifiManager) context.getSystemService(WIFI_SERVICE);
                ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ip;
    }

    public static void setViewPagerDots(Context context, ImageView[] dots, int dotsCount,
                                        LinearLayout llPdpProductImagePosition) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(context);
            dots[i].setImageDrawable(context.getResources().getDrawable(R.drawable.nonselecteditem_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(FOUR, ZERO, FOUR, ZERO);
            llPdpProductImagePosition.addView(dots[i], params);
        }
        dots[0].setImageDrawable(context.getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    public static void setViewPagerSelectedDot(Context context, ImageView[] dots, int dotsCount,
                                               int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(context.getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }
        dots[position].setImageDrawable(
                context.getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = null;
        try {
            connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connectivity != null) {
                connectivity = null;
            }
        }
        return false;
    }

    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    /*matches the email format*/
    public static boolean isEmail(@Nullable CharSequence str) {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches();
    }

    public static void hideSoftKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    /**
     * return the today date
     *
     * @return String
     */
    public static String getTodayDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MM");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public static Spannable colorized(final String text, final String word, final int argb) {
        final Spannable spannable = new SpannableString(text);
        int substringStart = 0;
        int start;
        while ((start = text.indexOf(word, substringStart)) >= 0) {
            spannable.setSpan(
                    new ForegroundColorSpan(argb), start, start + word.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            substringStart = start + word.length();
        }
        return spannable;
    }

    public static String getPurchaseMediaUrl(String url) {
        if (url != null && !url.isEmpty()) {
            url = url.replace("upload/e_blur:2000,o_80", "upload");
            return url;
        } else {
            return url;
        }
    }

    public static void findMatch(Context context, SpannableString spanString, Matcher matcher) {
        while (matcher.find()) {
            final String tag = matcher.group(0);
            spanString.setSpan(new TagSpannable(context, tag, R.color.color_white), matcher.start(),
                    matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public static String getDateDDMMMYYYY(String d) {
        try {
            Date date = new SimpleDateFormat("E dd/MMM/yyyy").parse(d);
            return new SimpleDateFormat("dd MMM yyyy").format(date);
        } catch (Exception e) {
            return d;
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    /*
    * Bug Title: In home Page and the explore page: The profile icon of a user without profile pic should be with initials, similar to ios
    * Bug Id: #2676
    * Fix dev: Hardik
    * Fix Date: 24/6/21
    * */

    public static void setTextRoundDrawable(Context context, String firstName, String lastName, ImageView ivProfile) {
        try {
            String textToDrw = "";
            if (firstName != null && !firstName.isEmpty()) textToDrw = firstName.charAt(0) + "";
            if (lastName != null && !lastName.isEmpty()) {
                textToDrw = textToDrw + lastName.charAt(0);
            } else if (firstName != null && firstName.length() >= 2) {
                textToDrw = textToDrw + firstName.charAt(1);
            }
            float density = context.getResources().getDisplayMetrics().density;
            ivProfile.setImageDrawable(TextDrawable.builder()
                .beginConfig()
                .height((int) ((100) * density))
                .width((int) ((100) * density))
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .fontSize((int) ((24) * density)) /* size in px */
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(textToDrw, ContextCompat.getColor(context, R.color.base_color)));
        } catch (Exception ignore) {
        }
    }
    public static void setTextDrawable(Context context, String firstName, String lastName, ImageView ivProfile) {
        try {
            String textToDrw = "";
            if (firstName != null && !firstName.isEmpty()) textToDrw = firstName.charAt(0) + "";
            if (lastName != null && !lastName.isEmpty()) {
                textToDrw = textToDrw + lastName.charAt(0);
            } else if (firstName != null && firstName.length() >= 2) {
                textToDrw = textToDrw + firstName.charAt(1);
            }
            float density = context.getResources().getDisplayMetrics().density;
            ivProfile.setImageDrawable(TextDrawable.builder()
                .beginConfig()
                .height((int) ((100) * density))
                .width((int) ((100) * density))
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .fontSize((int) ((24) * density)) /* size in px */
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRect(textToDrw, ContextCompat.getColor(context, R.color.base_color)));
        } catch (Exception ignore) {
        }
    }

    /**
     * Bug Title- post appears black
     * Bug Id-DUBAND2721
     * Fix Description- add check for blurred video for locked post, loaded as cover image to load as
     * jpg instead of mp4
     * Developer Name-Ashutosh
     * Fix Date-25/6/21
     **/
    public static String getCoverImageUrlForPost(String url, Integer mediaType, boolean purchased,
        String thumbnailUrl) {

        if (url != null && url.contains("cloudinary")) {

            if (purchased) {
                String extension = url.substring(url.lastIndexOf("."));
                url = url.replace(extension, Constants.IMAGE_EXT);

                if (mediaType != null) {
                    if (mediaType == 1) {
                        //video
                        url = url.replace("upload", "upload/so_0.01");
                    }
                } else {
                    if (extension.equals(".mp4")) {
                        //video
                        url = url.replace("upload", "upload/so_0.01");
                    }
                }

                return url;
            } else {
                return thumbnailUrl;
            }
        } else {
            if (purchased) {
                return url;
            } else {
                return thumbnailUrl;
            }
        }
    }

    public static String getModifiedThumbnailLinkForPosts(String url, boolean purchased) {
        if (url.contains("cloudinary")) {
            String extension = url.substring(url.lastIndexOf("."));
            if (purchased) {
                url = url.replace(extension, Constants.IMAGE_EXT);
            } else {
                //Since .webp images failed to load properly for the blurred urls and from /t_media_lib_thumb is coming added to blurred url,which looks very pixelated on posts feed..
                url = url.replace(extension, ".jpg").replace("/t_media_lib_thumb/", "/");
            }
        }
        return url;
    }
}