package knockknock.delivr_it.knockknockmaster.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import knockknock.delivr_it.knockknockmaster.R;
import knockknock.delivr_it.knockknockmaster.models.Order;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.HolderView> {

    private List<Order> orders;

    public OrderListAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public HolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_order_single_order, parent, false);
        return new HolderView(inflatedView);
    }

    @Override
    public void onBindViewHolder(HolderView holder, int position) {
        holder.orderId.setTag(orders.get(position).getOrder_id());
        holder.orderId.setText("Order # " + orders.get(position).getOrder_id());
        holder.timeOfOrder.setText("Placed on\n" + orders.get(position).getOrder_time());
        try {
            JSONObject jsonObject = new JSONObject(orders.get(position).getUser_information());
            holder.orderOwner.setText(jsonObject.getString("userName") + "\n" + jsonObject.getString("primaryPhoneNumber"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class HolderView extends RecyclerView.ViewHolder {

        TextView orderId;
        TextView timeOfOrder;
        TextView orderOwner;

        public HolderView(View itemView) {
            super(itemView);
            orderId = (TextView) itemView.findViewById(R.id.order_id);
            timeOfOrder = (TextView) itemView.findViewById(R.id.order_date_time);
            orderOwner = (TextView) itemView.findViewById(R.id.orderee);
        }
    }
}
