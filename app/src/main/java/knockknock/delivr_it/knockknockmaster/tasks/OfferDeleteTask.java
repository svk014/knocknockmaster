package knockknock.delivr_it.knockknockmaster.tasks;

import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import knockknock.delivr_it.knockknockmaster.managers.OfferStorageManager;
import knockknock.delivr_it.knockknockmaster.activities.OffersDisplayActivity;

public class OfferDeleteTask extends AsyncTask<Void, Void, String> {

    private ArrayList<String> ids;
    private OffersDisplayActivity offersDisplayActivity;

    public OfferDeleteTask(OffersDisplayActivity offersDisplayActivity, ArrayList<String> ids) {
        this.ids = ids;
        this.offersDisplayActivity = offersDisplayActivity;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://knock-knock-server-0.herokuapp.com/offer/delete");

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            for (String key : ids) {
                nameValuePairs.add(new BasicNameValuePair("a" + key, key));
            }
            nameValuePairs.add(new BasicNameValuePair("count", ids.size() + ""));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);

            StringBuilder sb = new StringBuilder();
            sb.append(reader.readLine());
            return sb.toString();

        } catch (Exception e) {

        }

        return "";
    }

    @Override
    protected void onPostExecute(String response) {
        Toast.makeText(offersDisplayActivity, response, Toast.LENGTH_SHORT).show();
        if (response.equals("DELETED"))
            OfferStorageManager.deleteOffers(offersDisplayActivity, ids);
        offersDisplayActivity.showOffers();
    }
}
