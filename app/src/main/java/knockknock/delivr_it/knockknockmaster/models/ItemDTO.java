package knockknock.delivr_it.knockknockmaster.models;

public class ItemDTO {
    public String id;
    public String item_name;
    public String category;
    public String section;
    public String price;
    public String discount;
    public String image_url;
    public String description;
    public String in_stock;
    public String vegetarian;

    public boolean areFieldsValid() {
        return id.length() > 0
                && item_name.length() > 0
                && category.length() > 0
                && section.length() > 0
                && price.length() > 0
                && image_url.length() > 0
                && discount.length() > 0 && discount.length() < 3;
    }

    @Override
    public boolean equals(Object obj) {
        ItemDTO otherItem = (ItemDTO) obj;
        boolean equal =
                otherItem.item_name.equals(item_name) && otherItem.category.equals(category)
                        && otherItem.section.equals(section) && otherItem.discount.equals(discount)
                        && otherItem.price.equals(price) && otherItem.in_stock.equals(in_stock)
                        && otherItem.image_url.equals(image_url) && otherItem.vegetarian.equals(vegetarian);
        return equal;
    }
}
