package knockknock.delivr_it.knockknockmaster.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UpdateVersion extends RealmObject {
    @PrimaryKey
    private int latestUpdate;

    public int getLatestUpdate() {
        return latestUpdate;
    }

    public void setLatestUpdate(int latestUpdate) {
        this.latestUpdate = latestUpdate;
    }
}
