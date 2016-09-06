package knockknock.delivr_it.knockknockmaster.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class BaseDatabaseWebToRealmTask extends AsyncTask<Void, Void, JSONArray> {

    private Context context;
    private int baseDbVersion;

    BaseDatabaseWebToRealmTask(Context context, int baseDbVersion) {
        this.context = context;
        this.baseDbVersion = baseDbVersion;
    }

    @Override
    protected JSONArray doInBackground(Void... voids) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://raw.githubusercontent.com/svk014/knocknockrawassets/master/base_database.txt");

            HttpResponse response = httpclient.execute(httpGet);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
            StringBuilder everything = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                everything.append(line);
            }

            return new JSONArray(everything.toString());
        } catch (Exception ignored) {
            Log.d("ignored", ignored.toString());
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray) {
        new ItemStorageTask(context).store(jsonArray, baseDbVersion);
    }
}
