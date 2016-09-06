package knockknock.delivr_it.knockknockmaster.tasks;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import knockknock.delivr_it.knockknockmaster.managers.BaseDatabaseVersionStorageManager;


public class UpdateDatabaseTask {

    private Context context;

    public UpdateDatabaseTask(Context context) {
        this.context = context;
    }

    public void checkAndUpdate() {
        new MatchAndUpdateBaseDatabaseVersion(context).execute();
    }

}


class MatchAndUpdateBaseDatabaseVersion extends AsyncTask<Void, Void, Integer> {

    private Context context;

    MatchAndUpdateBaseDatabaseVersion(Context context) {
        this.context = context;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("https://raw.githubusercontent.com/svk014/knocknockrawassets/master/base_database_version.md");
//            HttpGet httpGet = new HttpGet("http://knock-knock-server-0.herokuapp.com/baseDbVersion");

            HttpResponse response = httpclient.execute(httpGet);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
            String result11 = reader.readLine();
            return Integer.parseInt(result11);
        } catch (Exception ignored) {

        }


        return 0;
    }

    @Override
    protected void onPostExecute(Integer baseDbVersion) {
        if (baseDbVersion > BaseDatabaseVersionStorageManager.getCurrentDatabaseVersion(context)) {
            new BaseDatabaseWebToRealmTask(context, baseDbVersion).execute();
        }
    }

}
