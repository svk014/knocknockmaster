package knockknock.delivr_it.knockknockmaster.tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import knockknock.delivr_it.knockknockmaster.activities.OffersDisplayActivity;


public class OfferRetrievalTask extends AsyncTask<Void, Void, JSONArray> {

    private OffersDisplayActivity offersDisplayActivity;

    public OfferRetrievalTask(OffersDisplayActivity offersDisplayActivity) {
        this.offersDisplayActivity = offersDisplayActivity;
    }

    @Override
    protected JSONArray doInBackground(Void... voids) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet("http://knock-knock-server-0.herokuapp.com/offer/all");

            HttpResponse response = httpclient.execute(httppost);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");
            String result11 = sb.toString();
            return new JSONArray(result11);
        } catch (Exception ignored) {
            Log.d("ignored", ignored.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONArray offers) {
        try {
            new OfferCleanupTask(offersDisplayActivity).deleteOutdatedOffers(offers);
            new OfferImageWebToStorageTask(offersDisplayActivity).downloadAndStoreImages(offers);
            Toast.makeText(offersDisplayActivity, "Done getting offers", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(offersDisplayActivity, "Could not get offers", Toast.LENGTH_SHORT).show();
        }
    }
}
