package knockknock.delivr_it.knockknockmaster.activities;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import knockknock.delivr_it.knockknockmaster.models.ItemDTO;
import knockknock.delivr_it.knockknockmaster.tasks.ItemUpdateTask;

public class ItemCreationActivity extends AppCompatActivity {
    private Spinner vegetarianPreferenceControlSpinner;
    private EditText itemName;
    private EditText itemSection;
    private EditText itemPrice;
    private EditText itemCategory;
    private EditText itemDiscount;
    private EditText itemImageURL;
    private SwitchCompat inStock;
    private HashMap<String, String> vegetarianPreferenceOptionsMap;
    private boolean alreadyRequested = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_item);
        init();
        inflateSpinner();
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

    }

    public void createNewItem(View view) {
        if (alreadyRequested)
            Toast.makeText(getBaseContext(), "ALREADY REQUESTED", Toast.LENGTH_SHORT).show();

        ItemDTO itemDTO = new ItemDTO();
        itemDTO.id = ItemStorageManager.getNewId(getBaseContext());
        itemDTO.item_name = itemName.getText().toString();
        itemDTO.category = capitalizeFirst(itemCategory.getText().toString().trim());
        itemDTO.section = itemSection.getText().toString().toUpperCase();
        itemDTO.price = itemPrice.getText().toString();
        itemDTO.discount = itemDiscount.getText().toString();
        itemDTO.image_url = itemImageURL.getText().toString();
        itemDTO.description = "Some description";
        itemDTO.in_stock = inStock.isChecked() + "";

        String vegetarianPreference = vegetarianPreferenceControlSpinner.getSelectedItem().toString();
        itemDTO.vegetarian = vegetarianPreferenceOptionsMap.get(vegetarianPreference);
        if (itemDTO.areFieldsValid()) {
            alreadyRequested = true;
            new ItemUpdateTask(this, itemDTO.id, "create", new Gson().toJson(itemDTO)).execute();
        } else
            Toast.makeText(this, "Incomplete fields.", Toast.LENGTH_LONG).show();
    }

    @NonNull
    private String capitalizeFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }
}
