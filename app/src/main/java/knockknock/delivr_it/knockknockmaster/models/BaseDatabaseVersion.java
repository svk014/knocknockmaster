package knockknock.delivr_it.knockknockmaster.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class BaseDatabaseVersion extends RealmObject {
    @PrimaryKey
    private int baseDatabaseVersion;

    public int getBaseDatabaseVersion() {
        return baseDatabaseVersion;
    }

    public void setBaseDatabaseVersion(int baseDatabaseVersion) {
        this.baseDatabaseVersion = baseDatabaseVersion;
    }
}
