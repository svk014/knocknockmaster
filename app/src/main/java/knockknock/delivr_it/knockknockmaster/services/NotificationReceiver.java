package knockknock.delivr_it.knockknockmaster.services;


import android.support.annotation.NonNull;
import android.telephony.SmsManager;
import android.util.Log;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;

import java.util.Random;

import knockknock.delivr_it.knockknockmaster.tasks.SetVerificationCodeTask;

public class NotificationReceiver extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult receivedResult) {

        Log.i("header", receivedResult.payload.title);
//        Toast.makeText(getApplicationContext(), receivedResult.payload.body, Toast.LENGTH_LONG).show();
        // Return true to stop the notification from displaying.

        if (receivedResult.payload.title.equals("Verify Number")) {
            String code = getRandomVerificationCode();
            String phoneNumber = receivedResult.payload.body;
            new SetVerificationCodeTask(phoneNumber, code).execute();
            sendSMS(phoneNumber, getMessage(phoneNumber, code));
            return true;
        } else
            return false;

    }

    @NonNull
    private String getMessage(String phoneNumber, String code) {
        String message = "The 6 digit verification code for your number " + phoneNumber + " is " + code;
        return message;
    }

    private void sendSMS(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    private String getRandomVerificationCode() {
        int max = 999_989;
        int min = 100_010;
        Random random = new Random();
        int randomCode = random.nextInt(max - min + 1) + min;
        return String.valueOf(randomCode);
    }
}
