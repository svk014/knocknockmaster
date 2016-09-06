package knockknock.delivr_it.knockknockmaster.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Order extends RealmObject {
    @PrimaryKey
    private String order_id;
    private String order_identifier_client;
    private String order_status;
    private String user_information;
    private String delivery_information;
    private String order_time;
    private String orders;
    private String total;
    private String estimated_delivery;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_identifier_client() {
        return order_identifier_client;
    }

    public void setOrder_identifier_client(String order_identifier_client) {
        this.order_identifier_client = order_identifier_client;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getUser_information() {
        return user_information;
    }

    public void setUser_information(String user_information) {
        this.user_information = user_information;
    }

    public String getDelivery_information() {
        return delivery_information;
    }

    public void setDelivery_information(String delivery_information) {
        this.delivery_information = delivery_information;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getEstimated_delivery() {
        return estimated_delivery;
    }

    public void setEstimated_delivery(String estimated_delivery) {
        this.estimated_delivery = estimated_delivery;
    }
}
