package knockknock.delivr_it.knockknockmaster.tasks;

import android.graphics.Bitmap;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import knockknock.delivr_it.knockknockmaster.activities.OffersDisplayActivity;
import knockknock.delivr_it.knockknockmaster.managers.FileStorageManager;
import knockknock.delivr_it.knockknockmaster.managers.OfferStorageManager;


public class OfferImageWebToStorageTask {
    private OffersDisplayActivity offersDisplayActivity;

    public OfferImageWebToStorageTask(OffersDisplayActivity offersDisplayActivity) {
        this.offersDisplayActivity = offersDisplayActivity;
    }

    private int offers_processed = 0;

    public void downloadAndStoreImages(final JSONArray offers) throws Exception {
        for (int i = 0; i < offers.length(); i++) {
            final JSONObject jsonObject = offers.getJSONObject(i);
            String image_url = jsonObject.getString("image_url");
            String id = jsonObject.getString("id");

            boolean offerExists = OfferStorageManager.offerExists(offersDisplayActivity, id);

            if (offerExists) {
                offers_processed++;
                continue;
            }

            try {
                Picasso.with(offersDisplayActivity).load(image_url).transform(new Transformation() {
                    @Override
                    public Bitmap transform(Bitmap source) {
                        try {
                            String image_local_path = FileStorageManager.storeImageInFileSystem(offersDisplayActivity, "offers", System.currentTimeMillis() + "", source);
                            jsonObject.put("image_local_path", image_local_path);
                        } catch (JSONException ignored) {
                        }
                        saveOfferToDatabase(jsonObject);
                        offers_processed++;
                        if (offers_processed == offers.length())
                            offersDisplayActivity.showOffers();

                        return source;
                    }

                    @Override
                    public String key() {
                        return "key";
                    }
                }).fetch();
            } catch (Exception e) {
                Log.d("-->", e.toString());
            }
        }

    }

    public void saveOfferToDatabase(JSONObject jsonObject) {

        OfferStorageManager.storeOffer(offersDisplayActivity, jsonObject);

    }

}
