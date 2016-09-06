package knockknock.delivr_it.knockknockmaster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import knockknock.delivr_it.knockknockmaster.R;
import knockknock.delivr_it.knockknockmaster.adapters.OrderListAdapter;
import knockknock.delivr_it.knockknockmaster.managers.OrderStorageManager;
import knockknock.delivr_it.knockknockmaster.tasks.OrderWebToRealmTask;

public class PendingOrdersActivity extends AppCompatActivity {

    String orderStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_order_layout);

        orderStatus = getIntent().getExtras().getString("order_status");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show();
        new OrderWebToRealmTask(this).execute();

    }

    public void showPendingOrders() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.pending_orders_list);
        OrderListAdapter orderListAdapter = new OrderListAdapter(OrderStorageManager.getOrdersForStatus(getBaseContext(), orderStatus));

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(orderListAdapter);
    }

    public void showExpandedOrder(View view) {
        TextView orderId = (TextView) view.findViewById(R.id.order_id);

        Intent intent = new Intent(this, OrderProcessingActivity.class);
        intent.putExtra("order_id", orderId.getTag().toString());

        startActivity(intent);
        finish();
    }
}
