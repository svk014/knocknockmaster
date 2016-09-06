package knockknock.delivr_it.knockknockmaster.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Offer extends RealmObject {

    @PrimaryKey
    private String id;

    private String offer_id;
    private String offer_title;
    private String offer_text;
    private String image_url;
    private String allow_order;
    private String image_local_path;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public String getOffer_title() {
        return offer_title;
    }

    public void setOffer_title(String offer_title) {
        this.offer_title = offer_title;
    }

    public String getOffer_text() {
        return offer_text;
    }

    public void setOffer_text(String offer_text) {
        this.offer_text = offer_text;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getAllow_order() {
        return allow_order;
    }

    public void setAllow_order(String allow_order) {
        this.allow_order = allow_order;
    }

    public String getImage_local_path() {
        return image_local_path;
    }

    public void setImage_local_path(String image_local_path) {
        this.image_local_path = image_local_path;
    }

}
