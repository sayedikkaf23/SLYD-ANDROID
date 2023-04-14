package chat.hola.com.app.Utilities;
/*
 * Created by moda on 01/09/16.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import java.io.UnsupportedEncodingException;
import java.util.UUID;


/**
 * To fetch the public deviceId for the device
 */
public class DeviceUuidFactory {

    private static final String PREFS_FILE = "defaultPreferences";
    private static final String PREFS_DEVICE_ID = "deviceId";
    private volatile static UUID uuid;

    public DeviceUuidFactory(Context context) {
        if (uuid == null) {
            synchronized (DeviceUuidFactory.class) {
                if (uuid == null) {
                    final SharedPreferences prefs = context
                            .getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
                    final String id = prefs.getString(PREFS_DEVICE_ID, null);
                    if (id != null) {

                        uuid = UUID.fromString(id);
                    } else {
                        final String androidId = Settings.Secure.getString(
                                context.getContentResolver(), Settings.Secure.ANDROID_ID);

                        try {
                            if (!"9774d56d682e549c".equals(androidId)) {
                                uuid = UUID.nameUUIDFromBytes(androidId
                                        .getBytes("utf8"));
                            } else {


                                /*
                                 *
                                 *
                                 * not using device id at it requires runtime permissions
                                 *
                                 * */


//                                final String deviceId = (
//                                        (TelephonyManager) context
//                                                .getSystemService(Context.TELEPHONY_SERVICE))
//                                        .getDeviceId();
//                                uuid = deviceId != null ? UUID
//                                        .nameUUIDFromBytes(deviceId
//                                                .getBytes("utf8")) : UUID
//                                        .randomUUID();


                                uuid = UUID
                                        .randomUUID();


                            }
                        } catch (UnsupportedEncodingException e) {
                            throw new RuntimeException(e);
                        }

                        prefs.edit()
                                .putString(PREFS_DEVICE_ID, uuid.toString())
                                .apply();
                    }
                }
            }
        }
    }


    public String getDeviceUuid() {
        return uuid.toString();
    }
}