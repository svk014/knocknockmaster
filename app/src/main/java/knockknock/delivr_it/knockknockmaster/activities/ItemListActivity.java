package knockknock.delivr_it.knockknockmaster.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import knockknock.delivr_it.knockknockmaster.R;
import knockknock.delivr_it.knockknockmaster.adapters.ItemListAdapter;
import knockknock.delivr_it.knockknockmaster.managers.ItemStorageManager;
import knockknock.delivr_it.knockknockmaster.tasks.ItemUpdateRetrievalTask;


public class ItemListActivity extends AppCompatActivity {

    private EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items);

        init();
        setupSearchBar();

    }

    private void init() {
    }


    public void inflateMainMenuItems() {
        ItemListAdapter itemListAdapter =
                new ItemListAdapter(
                        ItemStorageManager.getAllItems(getApplicationContext()));

        setRecyclerView(itemListAdapter);
    }

    public void inflateMainMenuItems(String searchString) {
        ItemListAdapter itemListAdapter =
                new ItemListAdapter(
                        ItemStorageManager.getAllItemsFor(getApplicationContext(), searchString));

        setRecyclerView(itemListAdapter);
    }

    private void setRecyclerView(ItemListAdapter itemListAdapter) {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.all_items);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(itemListAdapter);
    }

    public void showItemDetails(View view) {
        Intent intent = new Intent(this, EditItemActivity.class);
        intent.putExtra("id", view.getTag().toString());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new ItemUpdateRetrievalTask(this).execute();
    }

    public void startItemCreationActivity(View view) {
        Intent intent = new Intent(this, ItemCreationActivity.class);
        startActivity(intent);
    }

    private void setupSearchBar() {
        searchBar = (EditText) findViewById(R.id.search_bar);

        searchBar.addTextChangedListener(getTextWatcher());
    }

    @NonNull
    private TextWatcher getTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 2)
                    inflateMainMenuItems(charSequence.toString());
                if (charSequence.length() == 0)
                    inflateMainMenuItems();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }

}
