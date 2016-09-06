package knockknock.delivr_it.knockknockmaster.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import knockknock.delivr_it.knockknockmaster.activities.ItemListActivity;

public class ItemUpdateRetrievalTask extends AsyncTask<Void, Void, JSONArray> {

    private Context anyContext;
    private ItemListActivity itemListActivity;

    public ItemUpdateRetrievalTask(ItemListActivity itemListActivity) {
        this.itemListActivity = itemListActivity;
    }

    public ItemUpdateRetrievalTask(Context anyContext) {
        this.anyContext = anyContext;
    }

    @Override
    protected JSONArray doInBackground(Void... voids) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet("http://knock-knock-server-0.herokuapp.com/itemUpdate/all");

            HttpResponse response = httpclient.execute(httppost);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");
            String result11 = sb.toString();
            return new JSONArray(result11);
        } catch (Exception ignored) {
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONArray itemUpdates) {
        Context localContext = itemListActivity != null ? itemListActivity : anyContext;
        try {
            new ItemUpdateWebToStorageTask(localContext).updateItems(itemUpdates);
            if (itemListActivity != null)
                itemListActivity.inflateMainMenuItems();
            Toast.makeText(localContext, "Done getting updates", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(localContext, "Could not get updates", Toast.LENGTH_SHORT).show();
        }
    }
}
