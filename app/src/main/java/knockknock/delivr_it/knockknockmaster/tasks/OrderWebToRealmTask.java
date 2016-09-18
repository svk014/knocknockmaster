package knockknock.delivr_it.knockknockmaster.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import knockknock.delivr_it.knockknockmaster.activities.PendingOrdersActivity;
import knockknock.delivr_it.knockknockmaster.managers.OrderStorageManager;

public class OrderWebToRealmTask extends AsyncTask<Void, Void, JSONObject> {


    private final PendingOrdersActivity pendingOrdersActivity;

    public OrderWebToRealmTask(PendingOrdersActivity pendingOrdersActivity) {
        this.pendingOrdersActivity = pendingOrdersActivity;
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://knock-knock-server-0.herokuapp.com/order/getOrder");

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            String mostRecentlyStoredOrder = OrderStorageManager.getMostRecentlyStoredOrder(pendingOrdersActivity);
            nameValuePairs.add(new BasicNameValuePair("current_most_recent_order", "0"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
            StringBuilder everything = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                everything.append(line);
            }
            return new JSONObject(everything.toString());
        } catch (Exception ignored) {
            Log.d("m", ignored.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        try {
            JSONArray pendingOrders = new JSONArray();
            for (int keyIndex = 0; keyIndex < jsonObject.names().length(); keyIndex++) {
                String key = jsonObject.names().getString(keyIndex);
                pendingOrders.put(jsonObject.getJSONObject(key));
            }
            OrderStorageManager.storePendingOrders(pendingOrdersActivity, pendingOrders);
        } catch (Exception ignored) {
            Log.d("m", ignored.toString());
        }
        pendingOrdersActivity.showPendingOrders();

    }
}
