package chat.hola.com.app.NumberVerification;
/*
 * Created by moda on 15/07/16.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;


/* Service to automatically read the SMS for verifying the account of the user */
public abstract class ReadSms extends BroadcastReceiver {


    final SmsManager sms = SmsManager.getDefault();


    public void onReceive(Context context, Intent intent) {


        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");


                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);


                    String senderNum = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    if (message != null) {
                        String splitted[] = message.split(" ");
                        message = splitted[0];
//                        if (senderNum.equals("TA-DOCOMO")) {
                        onSmsReceived(message);
//                        }
                        abortBroadcast();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    protected abstract void onSmsReceived(String s);

}