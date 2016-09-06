package knockknock.delivr_it.knockknockmaster.managers;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmResults;
import knockknock.delivr_it.knockknockmaster.models.UpdateVersion;

public class UpdateVersionStorageManager {

    public static int getCurrentUpdateVersion(Context context) {
        Realm realm = Realm.getInstance(context);
        RealmResults<UpdateVersion> updateVersionObject = realm.where(UpdateVersion.class).findAll();

        if (updateVersionObject.size() == 0)
            return 0;
        else
            return updateVersionObject.get(0).getLatestUpdate();
    }

    public static void setCurrentUpdateVersion(Context context, int updateVersion) {
        Realm realm = Realm.getInstance(context);
        RealmResults<UpdateVersion> updateVersionObject = realm.where(UpdateVersion.class).findAll();

        realm.beginTransaction();

        updateVersionObject.clear();

        UpdateVersion newUpdateVersionObject = new UpdateVersion();
        newUpdateVersionObject.setLatestUpdate(updateVersion);

        realm.copyToRealm(newUpdateVersionObject);

        realm.commitTransaction();
    }
}
