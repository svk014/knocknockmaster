package knockknock.delivr_it.knockknockmaster.managers;

import android.content.Context;

import org.json.JSONArray;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import knockknock.delivr_it.knockknockmaster.activities.OrderProcessingActivity;
import knockknock.delivr_it.knockknockmaster.models.Order;

public class OrderStorageManager {
    public static void storePendingOrders(Context context, JSONArray pendingOrders) {
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        realm.createOrUpdateAllFromJson(Order.class, pendingOrders);
        realm.commitTransaction();
    }

    public static String getMostRecentlyStoredOrder(Context context) {
        Realm realm = Realm.getInstance(context);
        RealmResults<Order> order_id = realm.where(Order.class).findAllSorted("order_id");
        if (order_id.size() == 0)
            return "0";
        return order_id.last().getOrder_id();
    }

    public static List<Order> getOrdersForStatus(Context context, String status) {
        Realm realm = Realm.getInstance(context);
        return realm.where(Order.class).equalTo("order_status", status).findAll();
    }

    public static Order getOrderForId(Context context, String orderId) {
        Realm realm = Realm.getInstance(context);
        return realm.where(Order.class).equalTo("order_id", orderId).findAll().first();
    }

    public static void updateOrder(Context context, String orderId, String orderStatus, String estimatedDelivery, String total) {
        Realm realm = Realm.getInstance(context);
        Order order = realm.where(Order.class).equalTo("order_id", orderId).findAll().first();

        realm.beginTransaction();
        order.setEstimated_delivery(estimatedDelivery);
        order.setOrder_status(orderStatus);
        order.setTotal(total);
        realm.commitTransaction();
    }
}
