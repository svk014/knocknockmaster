package knockknock.delivr_it.knockknockmaster.managers;

import android.content.Context;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import knockknock.delivr_it.knockknockmaster.models.Item;

public class ItemStorageManager {

    public static void storeItem(Context context, JSONObject itemJSON) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        Item item = realm.createOrUpdateObjectFromJson(Item.class, itemJSON);

        realm.commitTransaction();
    }

    public static void updateItemLocalImagePath(Context context, String itemId, String image_local_path) {
        Realm realm = Realm.getInstance(context);
        Item itemToBeUpdated = realm.where(Item.class).equalTo("id", itemId).findAll().first();
        realm.beginTransaction();
        itemToBeUpdated.setImage_local_path(image_local_path);
        realm.commitTransaction();
    }

    public static boolean confirmItemsStored(Context context, int count) {
        Realm realm = Realm.getInstance(context);
        RealmResults<Item> allItems = realm.where(Item.class).findAll();
        return allItems.size() == count;
    }

    public static List<Item> getAllItemsForCategory(Context context, String category, List<String> sections, String vegOnly) {
        Realm realm = Realm.getInstance(context);
        RealmQuery<Item> itemsQuery = realm.where(Item.class).equalTo("category", category).beginGroup();

        for (String section : sections) {
            itemsQuery.equalTo("section", section).or();
        }

        return itemsQuery.isEmpty("id").endGroup().beginGroup().equalTo("vegetarian", vegOnly)
                .or().equalTo("vegetarian", "na").or().equalTo("vegetarian", "true")
                .endGroup().findAll();
    }

    public static List<String> getAllSectionsForCategory(Context context, String category) {
        Realm realm = Realm.getInstance(context);
        RealmResults<Item> allItems = realm.where(Item.class).equalTo("category", category).findAll();

        HashSet<String> sections = new HashSet<>();
        for (Item item : allItems) {
            sections.add(item.getSection());
        }
        return new ArrayList<>(sections);
    }

    public static String getPriceForId(Context context, String itemId) {
        Realm realm = Realm.getInstance(context);

        return realm.where(Item.class).equalTo("id", itemId).findAll().first().getPrice();
    }

    public static List<Item> getAllItemsFor(Context context, String item) {
        Realm realm = Realm.getInstance(context);
        return realm.where(Item.class)
                .contains("item_name", item, false)
                .or().contains("section", item, false)
                .or().contains("category", item, false)
                .findAll();
    }

    public static String getNameForId(Context context, String itemId) {
        Realm realm = Realm.getInstance(context);
        return realm.where(Item.class).equalTo("id", itemId).findAll().first().getItem_name();
    }

    public static String getImageForId(Context context, String itemId) {
        Realm realm = Realm.getInstance(context);
        return realm.where(Item.class).equalTo("id", itemId).findAll().first().getImage_local_path();
    }


    public static List<Item> getAllItems(Context context) {
        Realm realm = Realm.getInstance(context);
        return realm.where(Item.class).findAll();
    }

    public static Item getItemById(Context context, String id) {
        Realm realm = Realm.getInstance(context);
        return realm.where(Item.class).equalTo("id", id).findFirst();
    }

    public static void deleteAllItems(Context context) {
        Realm realm = Realm.getInstance(context);

        realm.beginTransaction();
        realm.clear(Item.class);
        realm.commitTransaction();

    }

    public static void deleteItemById(Context context, String itemId) {
        Realm realm = Realm.getInstance(context);
        RealmResults<Item> itemsToDelete = realm.where(Item.class).equalTo("id", itemId).findAll();

        realm.beginTransaction();
        itemsToDelete.clear();
        realm.commitTransaction();
    }

    public static String getNewId(Context context) {
        Realm realm = Realm.getInstance(context);
        RealmResults<Item> all = realm.where(Item.class).findAll();
        int currentMaximumId = 0;
        for (Item item : all) {
            int itemId = Integer.parseInt(item.getId());
            if (itemId > currentMaximumId)
                currentMaximumId = itemId;
        }
        int newId = currentMaximumId + 1;
        return String.valueOf(newId);
    }
}
