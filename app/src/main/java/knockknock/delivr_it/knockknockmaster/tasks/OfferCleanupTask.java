package knockknock.delivr_it.knockknockmaster.tasks;

import android.content.Context;

import org.json.JSONArray;

import knockknock.delivr_it.knockknockmaster.activities.MainActivity;
import knockknock.delivr_it.knockknockmaster.managers.OfferStorageManager;


public class OfferCleanupTask {
    private Context context;

    public OfferCleanupTask(Context context) {
        this.context = context;
    }

    public void deleteOutdatedOffers(JSONArray offers) {
        try {
            OfferStorageManager.deleteOffers(context, offers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
