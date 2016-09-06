package knockknock.delivr_it.knockknockmaster.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import knockknock.delivr_it.knockknockmaster.R;
import knockknock.delivr_it.knockknockmaster.activities.OrderProcessingActivity;
import knockknock.delivr_it.knockknockmaster.managers.OrderStorageManager;

@SuppressWarnings("ALL")
public class OrderStatusUpdateTask extends AsyncTask<Void, Void, String> {
    private final OrderProcessingActivity activity;
    private final String orderId;
    private final String orderStatus;
    private final String estimatedDelivery;
    private final String total;

    public OrderStatusUpdateTask(OrderProcessingActivity activity, String orderId, String orderStatus, String estimatedDelivery, String total) {

        this.activity = activity;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.estimatedDelivery = estimatedDelivery;
        this.total = total;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://knock-knock-server-0.herokuapp.com/order/modify");

            showLoadingAnimation();

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("order_id", orderId));
            nameValuePairs.add(new BasicNameValuePair("order_status", orderStatus));
            nameValuePairs.add(new BasicNameValuePair("estimated_delivery", estimatedDelivery));
            nameValuePairs.add(new BasicNameValuePair("total", total));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
            StringBuilder everything = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                everything.append(line);
            }
            return everything.toString();

        } catch (Exception ignored) {
        }
        return "";
    }

    private void showLoadingAnimation() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.findViewById(orderStatus.equals("Rejected") ? R.id.spin_kit_reject : R.id.spin_kit)
                        .setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("success")) {
            OrderStorageManager.updateOrder(activity, orderId, orderStatus, estimatedDelivery, total);
            activity.sendNotification("Your order #" + orderId + " has been " + orderStatus.toLowerCase());
            Toast.makeText(activity, "SUCCESS", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity, "FAILED", Toast.LENGTH_SHORT).show();
        }
        activity.finish();

    }
}
