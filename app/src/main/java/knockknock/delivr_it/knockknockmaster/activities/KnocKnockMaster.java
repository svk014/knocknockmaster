package knockknock.delivr_it.knockknockmaster.activities;

import android.app.Application;
import android.util.Log;

import com.onesignal.OneSignal;

public class KnocKnockMaster extends Application {
    public static String one_signal_id;

    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .init();

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Log.d("user-id", userId);
                one_signal_id = userId;
            }
        });
    }
}
