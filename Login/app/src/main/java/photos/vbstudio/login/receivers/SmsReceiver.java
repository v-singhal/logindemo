package photos.vbstudio.login.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = SmsReceiver.class.getSimpleName();

    public static final String OTP_BROADCAST = "photos.vbstudio.login.optbroadcast";
    private static final String SMS_SENDER_NAME = "CUROFY";

    @Override
    public void onReceive(final Context context, Intent intent) {

        Log.d(TAG, "sms received");

        Bundle smsBundle = intent.getExtras();
        SmsMessage[] messages = null;

        if (smsBundle != null) {
            final Object[] pdusObj = (Object[]) smsBundle.get("pdus");
            String senderName = "";
            String smsMessage = "";

            messages = new SmsMessage[pdusObj.length];
            for (int counter = 0; counter < messages.length; counter++) {
                messages[counter] = SmsMessage.createFromPdu((byte[]) pdusObj[counter]);
                senderName = messages[counter].getOriginatingAddress();
                smsMessage = messages[counter].getMessageBody();
            }

            if (senderName.contains(SMS_SENDER_NAME) && smsMessage.contains("OTP") && smsMessage.contains("Curofy") && smsMessage.contains("access")) {
                processOTPSms(context, smsMessage);
            }
        }
    }

    private void processOTPSms(Context context, String smsMessage) {
        String otp = parseSMSForOTP(smsMessage);
        Intent optReceivedIntent = new Intent(OTP_BROADCAST);
        optReceivedIntent.putExtra("otp", otp);
        LocalBroadcastManager.getInstance(context).sendBroadcast(optReceivedIntent);
    }

    private String parseSMSForOTP(String smsMessage) {
        //I could have added verification for null checks but making assumption it won't be.
        return smsMessage.substring(0, smsMessage.indexOf(".")).split(" ")[1];
    }
}