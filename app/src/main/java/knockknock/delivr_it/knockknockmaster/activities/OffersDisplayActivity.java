package knockknock.delivr_it.knockknockmaster.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import knockknock.delivr_it.knockknockmaster.tasks.OfferDeleteTask;
import knockknock.delivr_it.knockknockmaster.managers.OfferStorageManager;
import knockknock.delivr_it.knockknockmaster.R;
import knockknock.delivr_it.knockknockmaster.adapters.OffersListAdapter;
import knockknock.delivr_it.knockknockmaster.tasks.OfferRetrievalTask;

public class OffersDisplayActivity extends Activity {
    private OffersListAdapter offersListAdapter;
    private ArrayList<String> idsToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_offers);
    }

    public void showOffers() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                idsToDelete = new ArrayList<>();
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                offersListAdapter = new OffersListAdapter(OffersDisplayActivity.this
                        , OfferStorageManager.allOffers(getApplicationContext())
                        , idsToDelete);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());

                recyclerView.setAdapter(offersListAdapter);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        new OfferRetrievalTask(this).execute();
        showOffers();
    }

    public void startOfferCreationActivity(View view) {
        Intent intent = new Intent(OffersDisplayActivity.this, OfferCreationActivity.class);
        startActivity(intent);
    }

    public void deleteSelectedOffers(View view) {
        OfferDeleteTask offerDeleteTask = new OfferDeleteTask(OffersDisplayActivity.this, idsToDelete);
        offerDeleteTask.execute();
    }
}