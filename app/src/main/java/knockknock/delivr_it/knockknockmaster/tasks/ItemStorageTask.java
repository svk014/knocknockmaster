package knockknock.delivr_it.knockknockmaster.tasks;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import knockknock.delivr_it.knockknockmaster.managers.BaseDatabaseVersionStorageManager;
import knockknock.delivr_it.knockknockmaster.managers.ItemStorageManager;
import knockknock.delivr_it.knockknockmaster.managers.UpdateVersionStorageManager;


public class ItemStorageTask {
    private Context context;

    ItemStorageTask(Context context) {

        this.context = context;
    }

    public void store(JSONArray items, int baseDbVersion) {
        clearOldEntriesInDb();

        for (int i = 0; i < items.length(); i++) {
            try {
                JSONObject item = items.getJSONObject(i);
                item.put("image_local_path", "<empty>");
                ItemStorageManager.storeItem(context, item);
            } catch (JSONException ignored) {
            }
        }
        UpdateVersionStorageManager.setCurrentUpdateVersion(context, 0);
        new ItemUpdateRetrievalTask(context).execute();
        if (ItemStorageManager.confirmItemsStored(context, items.length())) {
            BaseDatabaseVersionStorageManager.setCurrentDatabaseVersion(context, baseDbVersion);
        }
    }

    private void clearOldEntriesInDb() {
        ItemStorageManager.deleteAllItems(context);
    }

}