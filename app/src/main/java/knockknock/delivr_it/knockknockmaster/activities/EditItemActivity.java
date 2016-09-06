package knockknock.delivr_it.knockknockmaster.activities;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import knockknock.delivr_it.knockknockmaster.R;
import knockknock.delivr_it.knockknockmaster.managers.ItemStorageManager;
import knockknock.delivr_it.knockknockmaster.models.Item;
import knockknock.delivr_it.knockknockmaster.models.ItemDTO;
import knockknock.delivr_it.knockknockmaster.tasks.ItemUpdateTask;

public class EditItemActivity extends AppCompatActivity {

    private Spinner vegetarianPreferenceControlSpinner;
    private EditText itemName;
    private EditText itemSection;
    private EditText itemPrice;
    private EditText itemCategory;
    private EditText itemDiscount;
    private SwitchCompat inStock;
    private HashMap<String, String> vegetarianPreferenceOptionsMap;
    Item item;
    ItemDTO oldItemDTO;
    private EditText itemImageURL;
    private boolean alreadyRequested = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_edit_layout);

        String id = getIntent().getExtras().getString("id");
        item = ItemStorageManager.getItemById(getBaseContext(), id);
        init();
        inflateSpinner();
        setData(item);
        storeOldItemData();
    }

    private void storeOldItemData() {
        oldItemDTO = new ItemDTO();
        oldItemDTO.id = item.getId().trim();
        oldItemDTO.item_name = itemName.getText().toString().trim();
        oldItemDTO.category = itemCategory.getText().toString().trim();
        oldItemDTO.section = itemSection.getText().toString().trim();
        oldItemDTO.price = itemPrice.getText().toString().trim();
        oldItemDTO.discount = itemDiscount.getText().toString().trim();
        oldItemDTO.image_url = item.getImage_url().trim();
        oldItemDTO.description = item.getDescription().trim();
        oldItemDTO.in_stock = inStock.isChecked() + "";
        String vegetarianPreference = vegetarianPreferenceControlSpinner.getSelectedItem().toString();
        oldItemDTO.vegetarian = vegetarianPreferenceOptionsMap.get(vegetarianPreference);
    }

    private void setData(Item item) {
        itemName.setText(item.getItem_name().trim());
        itemSection.setText(item.getSection().trim());
        itemCategory.setText(item.getCategory().trim());
        itemPrice.setText(item.getPrice().trim());
        itemDiscount.setText(item.getDiscount().trim());
        itemImageURL.setText(item.getImage_url().trim());
        inStock.setChecked(Boolean.parseBoolean(item.getIn_stock()));
    }

    private void init() {

        itemName = (EditText) findViewById(R.id.item_name);
        itemSection = (EditText) findViewById(R.id.item_section);
        itemPrice = (EditText) findViewById(R.id.item_price);
        itemCategory = (EditText) findViewById(R.id.item_category);
        itemDiscount = (EditText) findViewById(R.id.discount);
        itemImageURL = (EditText) findViewById(R.id.item_image_url);
        inStock = (SwitchCompat) findViewById(R.id.in_stock_control_switch);
        itemImageURL.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (itemImageURL.getRight() - itemImageURL
                            .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        showImageDialog();
                        Toast.makeText(getBaseContext(), "Loading Image", Toast.LENGTH_LONG).show();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void showImageDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.item_image);
        ImageView itemImage = (ImageView) dialog.findViewById(R.id.item_image);

        Picasso.with(getBaseContext()).load(itemImageURL.getText().toString()).placeholder(R.drawable.default_place_holder_image)
                .into(itemImage);

        dialog.show();
    }

    private void inflateSpinner() {
        vegetarianPreferenceControlSpinner = (Spinner) findViewById(R.id.vegetarian_preference_control_spinner);
        List<String> sortCategories = new ArrayList<>();
        sortCategories.add("Vegetarian");
        sortCategories.add("Non-Vegetarian");
        sortCategories.add("Not-Applicable");

        vegetarianPreferenceOptionsMap = new HashMap<>();

        vegetarianPreferenceOptionsMap.put("Vegetarian", "true");
        vegetarianPreferenceOptionsMap.put("Non-Vegetarian", "false");
        vegetarianPreferenceOptionsMap.put("Not-Applicable", "na");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sortCategories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        vegetarianPreferenceControlSpinner.setAdapter(dataAdapter);

        String currentVegetarianPreference = item.getVegetarian();
        for (String vegetarianPreference : vegetarianPreferenceOptionsMap.keySet()) {
            String vegetarianPreferenceUIText = vegetarianPreferenceOptionsMap.get(vegetarianPreference);
            if (currentVegetarianPreference.equals(vegetarianPreferenceUIText)) {
                int index = sortCategories.indexOf(vegetarianPreference);
                vegetarianPreferenceControlSpinner.setSelection(index);
            }
        }

    }


    public void verifyAndUpdateItem(View view) {
        if (alreadyRequested)
            Toast.makeText(getBaseContext(), "ALREADY REQUESTED", Toast.LENGTH_SHORT).show();
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.id = item.getId().trim();
        itemDTO.item_name = itemName.getText().toString().trim();
        itemDTO.category = capitalizeFirst(itemCategory.getText().toString().trim());
        itemDTO.section = itemSection.getText().toString().trim().toUpperCase();
        itemDTO.price = itemPrice.getText().toString().trim();
        itemDTO.discount = itemDiscount.getText().toString().trim();
        itemDTO.image_url = itemImageURL.getText().toString().trim();
        itemDTO.description = item.getDescription().trim();
        itemDTO.in_stock = inStock.isChecked() + "";

        String vegetarianPreference = vegetarianPreferenceControlSpinner.getSelectedItem().toString();
        itemDTO.vegetarian = vegetarianPreferenceOptionsMap.get(vegetarianPreference);

        if (itemDTO.equals(oldItemDTO)) {
            Toast.makeText(getBaseContext(), "Nothing to update", Toast.LENGTH_SHORT).show();
            return;
        }
        if (itemDTO.areFieldsValid()) {
            alreadyRequested = true;
            new ItemUpdateTask(this, item.getId(), "update", new Gson().toJson(itemDTO)).execute();
        } else
            Toast.makeText(this, "Incomplete fields.", Toast.LENGTH_LONG).show();
    }

    public void deleteItem(View view) {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.id = item.getId().trim();
        itemDTO.item_name = itemName.getText().toString().trim();
        itemDTO.category = itemCategory.getText().toString().trim();
        itemDTO.section = itemSection.getText().toString().trim();
        itemDTO.price = itemPrice.getText().toString().trim();
        itemDTO.discount = itemDiscount.getText().toString().trim();
        itemDTO.image_url = item.getImage_url().trim();
        itemDTO.description = item.getDescription().trim();
        itemDTO.in_stock = inStock.isChecked() + "";

        String vegetarianPreference = vegetarianPreferenceControlSpinner.getSelectedItem().toString();
        itemDTO.vegetarian = vegetarianPreferenceOptionsMap.get(vegetarianPreference);

        new ItemUpdateTask(this, item.getId(), "delete", new Gson().toJson(itemDTO)).execute();
    }

    private String capitalizeFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }
}
