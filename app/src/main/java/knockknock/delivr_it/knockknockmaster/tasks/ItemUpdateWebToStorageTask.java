package knockknock.delivr_it.knockknockmaster.tasks;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import knockknock.delivr_it.knockknockmaster.managers.ItemStorageManager;
import knockknock.delivr_it.knockknockmaster.managers.UpdateVersionStorageManager;


public class ItemUpdateWebToStorageTask {
    private Context context;

    public ItemUpdateWebToStorageTask(Context context) {
        this.context = context;
    }

    public void updateItems(JSONArray itemUpdates) throws Exception {

        for (int i = 0; i < itemUpdates.length(); i++) {
            final JSONObject jsonObject = itemUpdates.getJSONObject(i);
            String id = jsonObject.getString("id");

            int currentUpdateVersion = UpdateVersionStorageManager.getCurrentUpdateVersion(context);

            if (currentUpdateVersion >= Integer.parseInt(id)) {
                continue;
            }

            String update_type = jsonObject.getString("update_type");
            if (update_type.equals("update") || update_type.equals("create")) {
                updateOrCreate(jsonObject);
            }
            if (update_type.equals("delete")) {
                delete(jsonObject);
            }

            UpdateVersionStorageManager.setCurrentUpdateVersion(context, Integer.parseInt(id));

        }
    }

    private void delete(JSONObject jsonObject) throws JSONException {
        String jsonItemString = jsonObject.getString("item_json");

        JSONObject itemUpdate = new JSONObject(jsonItemString);
        String id = itemUpdate.getString("id");

        ItemStorageManager.deleteItemById(context, id);

    }

    public void updateOrCreate(JSONObject jsonObject) throws JSONException {
        String jsonItemString = jsonObject.getString("item_json");

        JSONObject itemUpdate = new JSONObject(jsonItemString);
        itemUpdate.put("image_local_path", "<empty>");

        ItemStorageManager.storeItem(context, itemUpdate);
    }
}
