package knockknock.delivr_it.knockknockmaster.managers;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import knockknock.delivr_it.knockknockmaster.models.Offer;

public class OfferStorageManager {

    public static void storeDeal(Context context, JSONObject deal) {
        Realm realm = Realm.getInstance(context);

        realm.beginTransaction();

        Offer offer = realm.createObjectFromJson(Offer.class, deal);
        realm.commitTransaction();
        RealmResults<Offer> results = realm.where(Offer.class).findAll();
    }

    public static List<Offer> allOffers(Context context) {
        Realm realm = Realm.getInstance(context);
        RealmResults<Offer> results = realm.where(Offer.class).findAll();
        return results;
    }

    public static void deleteOffers(Context context, ArrayList<String> ids) {
        Realm realm = Realm.getInstance(context);
        final RealmResults<Offer> results = itemsToDelete(realm, ids);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.clear();
            }
        });
    }

    private static RealmResults<Offer> itemsToDelete(Realm realm, ArrayList<String> ids) {

        RealmQuery<Offer> deleteQuery = realm.where(Offer.class);
        int i = 0;
        for (String id : ids) {
            if (i != 0) {
                deleteQuery = deleteQuery.or();
            }
            deleteQuery = deleteQuery.equalTo("id", id);
            i++;
        }
        return deleteQuery.findAll();
    }

    public static void storeOffer(Context context, JSONObject offerJSON) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        Offer offer = realm.createObjectFromJson(Offer.class, offerJSON);
        realm.copyToRealmOrUpdate(offer);
        realm.commitTransaction();
    }

    public static boolean deleteOffers(Context context, JSONArray offers) throws Exception {
        Realm realm = Realm.getInstance(context);
        int i = 0;
        RealmQuery<Offer> where = realm.where(Offer.class);
        for (; i < offers.length() - 1; i++) {
            where.notEqualTo("id", offers.getJSONObject(i).getString("id"));
        }
        RealmResults<Offer> itemsToDelete = where.notEqualTo("id", offers.getJSONObject(i).getString("id")).findAll();

        realm.beginTransaction();
        itemsToDelete.clear();
        realm.commitTransaction();

        return true;
    }

    public static boolean offerExists(Context context, String id) {
        Realm realm = Realm.getInstance(context);
        RealmResults<Offer> results = realm.where(Offer.class).equalTo("id", id).findAll();
        return results.size() != 0;
    }
}
