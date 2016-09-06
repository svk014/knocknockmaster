package knockknock.delivr_it.knockknockmaster.managers;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmResults;
import knockknock.delivr_it.knockknockmaster.models.BaseDatabaseVersion;

public class BaseDatabaseVersionStorageManager {

    public static int getCurrentDatabaseVersion(Context context) {
        Realm realm = Realm.getInstance(context);
        RealmResults<BaseDatabaseVersion> baseBaseDatabaseVersionObject = realm.where(BaseDatabaseVersion.class).findAll();

        if (baseBaseDatabaseVersionObject.size() == 0)
            return 0;
        else
            return baseBaseDatabaseVersionObject.get(0).getBaseDatabaseVersion();
    }

    public static void setCurrentDatabaseVersion(Context context, int baseDatabaseVersion){
        Realm realm = Realm.getInstance(context);
        RealmResults<BaseDatabaseVersion> baseBaseDatabaseVersionObject = realm.where(BaseDatabaseVersion.class).findAll();

        realm.beginTransaction();

        baseBaseDatabaseVersionObject.clear();

        BaseDatabaseVersion newbaseDatabaseVersionObject = new BaseDatabaseVersion();
        newbaseDatabaseVersionObject.setBaseDatabaseVersion(baseDatabaseVersion);

        realm.copyToRealm(newbaseDatabaseVersionObject);

        realm.commitTransaction();
    }
}