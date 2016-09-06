package knockknock.delivr_it.knockknockmaster.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import knockknock.delivr_it.knockknockmaster.R;
import knockknock.delivr_it.knockknockmaster.tasks.StoreOneSignalIdTask;
import knockknock.delivr_it.knockknockmaster.tasks.UpdateDatabaseTask;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new StoreOneSignalIdTask().execute();
        new UpdateDatabaseTask(getBaseContext()).checkAndUpdate();

    }


    public void startOfferActivity(View view) {
        Intent intent = new Intent(MainActivity.this, OffersDisplayActivity.class);
        startActivity(intent);
    }


    public void startPendingOrdersActivity(View view) {
        Intent intent = new Intent(MainActivity.this, PendingOrdersActivity.class);
        intent.putExtra("order_status", "Pending");
        startActivity(intent);

    }

    public void startAcceptedOrdersActivity(View view) {
        Intent intent = new Intent(MainActivity.this, PendingOrdersActivity.class);
        intent.putExtra("order_status", "Accepted");
        startActivity(intent);

    }

    public void startDispatchedOrdersActivity(View view) {
        Intent intent = new Intent(MainActivity.this, PendingOrdersActivity.class);
        intent.putExtra("order_status", "Dispatched");
        startActivity(intent);
    }

    public void startDeliveredOrdersActivity(View view) {
        Intent intent = new Intent(MainActivity.this, PendingOrdersActivity.class);
        intent.putExtra("order_status", "Delivered");
        startActivity(intent);
    }

    public void loadInventory(View view) {
        Intent intent = new Intent(MainActivity.this, ItemListActivity.class);
        startActivity(intent);
    }

}
