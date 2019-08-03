package com.zikrabyte.organic.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by KRISH on 4/4/2018.
 */

public class SmsListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null) {
                //---retrieve the SMS message received---
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        if (Build.VERSION.SDK_INT <= 22) {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        } else {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], bundle.getString("format"));
                        }
                        msg_from = msgs[i].getOriginatingAddress();
                        if (msg_from.contains("EZZOTP")) {
                            String msgBody = msgs[i].getMessageBody();
                            //String pinNo = msgBody.substring(msgBody.indexOf('"') + 1, msgBody.indexOf('"', msgBody.indexOf('"') + 2));
                            String pinNo = msgBody.replaceAll("[^0-9]", "");
                            Log.d("SMS", "From -" + msg_from + " : Body- " + msgBody);
                            //CodeVerification.insertCode(pinNo);

                            // Broadcast to Auto read Code sms
                            final String DISPLAY_MESSAGE_ACTION = context.getPackageName() + ".CodeSmsReceived";
                            Intent intentCodeSms = new Intent(DISPLAY_MESSAGE_ACTION);
                            intentCodeSms.putExtra("varificationCode", pinNo);
                            context.sendBroadcast(intentCodeSms);
                        }
                    }
                } catch (Exception e) {
                    Log.d("Exception caught", e.getMessage());
                }
            }
        }
    }
}
