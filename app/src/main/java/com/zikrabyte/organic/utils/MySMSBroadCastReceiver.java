package com.zikrabyte.organic.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class MySMSBroadCastReceiver extends BroadcastReceiver
{

        private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            //You must check here if the sender is your provider and not another one with same text.

            String messageBody = smsMessage.getMessageBody();
            //Pass on the text to our listener.
            if(messageBody!=null)
            {
                mListener.messageReceived(messageBody);
            }

        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }

}
