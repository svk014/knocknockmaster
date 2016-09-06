package knockknock.delivr_it.knockknockmaster.tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import knockknock.delivr_it.knockknockmaster.managers.OfferStorageManager;

public class OfferUpdateTask extends AsyncTask<Void, Void, JSONObject> {
    private Context context;
    private final String title;
    private final String text;
    private final String image_url;
    private String imagePath;

    public OfferUpdateTask(Context context, String title, String text, String image_url, String imagePath) {
        this.context = context;
        this.title = title;
        this.text = text;
        this.image_url = image_url;
        this.imagePath = imagePath;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://knock-knock-server-0.herokuapp.com/offer/add");

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("Id", "12345"));
            nameValuePairs.add(new BasicNameValuePair("Title", title));
            nameValuePairs.add(new BasicNameValuePair("Text", text));
            nameValuePairs.add(new BasicNameValuePair("ImageUrl", image_url));
            nameValuePairs.add(new BasicNameValuePair("AllowOrder", "true"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");
            String result11 = sb.toString();

            return new JSONObject(result11);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        if (result != null) {
            Toast.makeText(context, "Offer uploaded successfully", Toast.LENGTH_LONG).show();
            try {
                result.put("image_local_path", imagePath);
//                OfferStorageManager.storeDeal(context, result);
                terminateActivity();
            } catch (JSONException e) {
            }
        }
    }

    private void terminateActivity() {
        ((Activity) context).finish();
    }
}
