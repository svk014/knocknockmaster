package knockknock.delivr_it.knockknockmaster.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import knockknock.delivr_it.knockknockmaster.R;
import knockknock.delivr_it.knockknockmaster.managers.ItemStorageManager;
import knockknock.delivr_it.knockknockmaster.managers.OrderStorageManager;
import knockknock.delivr_it.knockknockmaster.models.Order;
import knockknock.delivr_it.knockknockmaster.tasks.OrderStatusUpdateTask;

public class OrderProcessingActivity extends AppCompatActivity {
    private Order order;
    private TextView orderDetails;
    private TextView deliveryDetails;
    private TextView itemNames;
    private TextView itemPrices;
    private TextView currentOrderStatus;
    private TextView total;
    private EditText chargesEditText;
    private EditText estimatedDeliveryEditText;


    HashMap<String, String> nextStatusMapping = new HashMap<>();
    HashMap<String, String> statusButtonMapping = new HashMap<>();
    private boolean alreadyRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expanded_order);
        String orderId = getIntent().getExtras().getString("order_id");

        order = OrderStorageManager.getOrderForId(getBaseContext(), orderId);

        init();
        initStatuses();
        showOrderDetails();
        showNextStage();
    }

    private void init() {
        orderDetails = (TextView) findViewById(R.id.order_details);
        deliveryDetails = (TextView) findViewById(R.id.delivery_details);
        itemNames = (TextView) findViewById(R.id.item_names);
        itemPrices = (TextView) findViewById(R.id.item_prices);
        currentOrderStatus = (TextView) findViewById(R.id.order_status);
        chargesEditText = (EditText) findViewById(R.id.charges);
        total = (TextView) findViewById(R.id.total);
        estimatedDeliveryEditText = (EditText) findViewById(R.id.estimated_delivery);

        chargesEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    int totalCost = calculateTotalWithCharges(charSequence.toString());
                    total.setText("Rs. " + totalCost);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private int calculateTotalWithCharges(String charges) throws Exception {
        charges = charges.equals("") ? "0" : charges;
        int total = 0;
        JSONArray jsonArray = new JSONArray(order.getOrders());
        for (int keyIndex = 0; keyIndex < jsonArray.length(); keyIndex++) {
            JSONObject jsonObject = jsonArray.getJSONObject(keyIndex);
            total += Integer.parseInt(jsonObject.getString("quantity")) * Integer.parseInt(jsonObject.getString("price_per_unit"));
        }
        return total + Integer.parseInt(charges);
    }

    private void showOrderDetails() {
        try {
            orderDetails.setText(buildOrderDetails());
            deliveryDetails.setText(buildDeliveryDetails());
            itemNames.setText(buildItemNameDetails());
            itemPrices.setText(buildItemPriceQuantityDetails());
            currentOrderStatus.setText(order.getOrder_status());
            total.setText("Rs. " + calculateTotalWithCharges("0"));
            if (!order.getOrder_status().equals("Pending")) {
                int charges = Integer.parseInt(order.getTotal()) - calculateTotalWithCharges("0");
                chargesEditText.setText(charges + "");
                chargesEditText.setEnabled(false);
                estimatedDeliveryEditText.setText(order.getEstimated_delivery());
                estimatedDeliveryEditText.setEnabled(false);
            }

        } catch (Exception ignored) {
        }
    }

    private String buildItemPriceQuantityDetails() throws Exception {
        String s = "\n";

        JSONArray jsonArray = new JSONArray(order.getOrders());
        for (int keyIndex = 0; keyIndex < jsonArray.length(); keyIndex++) {
            JSONObject jsonObject = jsonArray.getJSONObject(keyIndex);
            s += jsonObject.getString("quantity") + " * " + jsonObject.getString("price_per_unit") + "\n\n";
        }

        return s;
    }

    private String buildDeliveryDetails() throws Exception {
        String s = "";

        JSONObject userDetails = new JSONObject(order.getUser_information());
        s += userDetails.getString("userName") + "\n";
        s += userDetails.getString("address") + "\n";
        s += userDetails.getString("primaryPhoneNumber") + "\t\t\t";
        s += userDetails.getString("secondaryPhoneNumber") + "\n";
        s += userDetails.getString("email") + "\n";

        return s;
    }

    private String buildOrderDetails() throws Exception {
        String s = "Order # " + order.getOrder_id() + "\n";

        JSONObject deliveryDetails = new JSONObject(order.getDelivery_information());
        s += deliveryDetails.getString("deliveryType") + "\n";
        if (!deliveryDetails.getString("deliveryDay").equals("not applicable"))
            s += deliveryDetails.getString("deliveryDay") + "\n";
        if (!deliveryDetails.getString("deliveryDay").equals("not applicable"))
            s += deliveryDetails.getString("deliveryTime") + "\n";

        return s;
    }

    private String buildItemNameDetails() throws Exception {
        String s = "";

        JSONArray jsonArray = new JSONArray(order.getOrders());
        for (int keyIndex = 0; keyIndex < jsonArray.length(); keyIndex++) {
            JSONObject jsonObject = jsonArray.getJSONObject(keyIndex);
            String itemName = ItemStorageManager.getNameForId(getBaseContext(), jsonObject.getString("item_id"));
            s += itemName + "\n\n";
        }

        return s;
    }

    private void showNextStage() {

        TextView statusChangeButton = (TextView) findViewById(R.id.status_change_button);
        View rejectButton = findViewById(R.id.reject_button);

        if (order.getOrder_status().equals("Delivered")) {
            statusChangeButton.setVisibility(View.GONE);
        } else {
            statusChangeButton.setVisibility(View.VISIBLE);
            String nextStatus = nextStatusMapping.get(order.getOrder_status());
            String buttonTextForNextStatus = statusButtonMapping.get(nextStatus);
            statusChangeButton.setText(buttonTextForNextStatus);
        }

        if (order.getOrder_status().equals("Pending")) {
            rejectButton.setVisibility(View.VISIBLE);
        } else {
            rejectButton.setVisibility(View.GONE);
        }
    }

    private void initStatuses() {
        nextStatusMapping.put("Pending", "Accepted");
        nextStatusMapping.put("Accepted", "Dispatched");
        nextStatusMapping.put("Dispatched", "Delivered");

        statusButtonMapping.put("Accepted", "ACCEPT");
        statusButtonMapping.put("Dispatched", "ORDER DISPATCHED");
        statusButtonMapping.put("Delivered", "SUCCESSFULLY DELIVERED");
    }


    public void verifyAndUpdateOrder(View view) {
        if (alreadyRequested)
            Toast.makeText(getBaseContext(), "ALREADY REQUESTED", Toast.LENGTH_SHORT).show();

        if (verifyAllFieldsSet()) {
            try {
                String estimatedDelivery = estimatedDeliveryEditText.getText().toString();
                int totalCost = calculateTotalWithCharges(chargesEditText.getText().toString());
                String newStatus = view.getTag().equals("rejected") ? "Rejected" : nextStatusMapping.get(order.getOrder_status());

                alreadyRequested = true;
                new OrderStatusUpdateTask(this, order.getOrder_id(), newStatus,
                        estimatedDelivery, totalCost + "").execute();

            } catch (Exception ignored) {
            }
        } else {
            Toast.makeText(getBaseContext(), "Incomplete fields", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean verifyAllFieldsSet() {
        return !chargesEditText.getText().toString().equals("") && !estimatedDeliveryEditText.getText().toString().equals("");
    }

    public void sendNotification(String message) {
        try {
            JSONObject jsonObject = new JSONObject(order.getUser_information());
            JSONObject oneSignalDeliveryObject = new JSONObject("{'contents': {'en':'" + message + "'}, 'include_player_ids': ['"
                    + jsonObject.getString("oneSingalId")
                    + "']}");
            OneSignal.postNotification(oneSignalDeliveryObject, getNotificationPostHandler());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public OneSignal.PostNotificationResponseHandler getNotificationPostHandler() {
        return new OneSignal.PostNotificationResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.i("OneSignalExample", "postNotification Success: " + response.toString());
            }

            @Override
            public void onFailure(JSONObject response) {
                Log.e("OneSignalExample", "postNotification Failure: " + response.toString());
            }
        };
    }
}
