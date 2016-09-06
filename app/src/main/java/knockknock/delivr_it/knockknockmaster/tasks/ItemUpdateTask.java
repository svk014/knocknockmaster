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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ItemUpdateTask extends AsyncTask<Void, Void, String> {
    private Context context;
    private final String update_id;
    private final String update_type;
    private final String item_json;

    public ItemUpdateTask(Context context, String update_id, String update_type, String item_JSON) {
        this.context = context;
        this.update_id = update_id;
        this.update_type = update_type;
        this.item_json = item_JSON;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://knock-knock-server-0.herokuapp.com/itemUpdate/add");

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            nameValuePairs.add(new BasicNameValuePair("update_id", update_id));
            nameValuePairs.add(new BasicNameValuePair("update_type", update_type));
            nameValuePairs.add(new BasicNameValuePair("item_json", item_json));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");
            String result11 = sb.toString();

            return result11;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            Toast.makeText(context, "SUCCESS", Toast.LENGTH_SHORT).show();
            terminateActivity();
        }
        Toast.makeText(context, "FAILED", Toast.LENGTH_SHORT).show();
    }

    private void terminateActivity() {
        ((Activity) context).finish();
    }
}
