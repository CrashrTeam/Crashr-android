package crashr.mapshackathon.com.crashr.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.melnykov.fab.FloatingActionButton;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import crashr.mapshackathon.com.crashr.R;
import crashr.mapshackathon.com.crashr.model.Listing;

public class ListingsActivity extends ActionBarActivity {
    private ListView mListView;
    private ArrayList<Listing> mListings = new ArrayList<Listing>();
    private ListingsAdapter mAdapter;

    static final int ADD_NEW_LISTING = 1;
    static final int EDIT_LISTING = 2;
    static final String ADD_LISTING_KEY = "add_listing";
    static final String EDIT_LISTING_KEY = "edit_listing";
    static final String EDIT_INDEX = "edit_index";
    static final String HAS_CACHE_KEY = "listing_has_cache";
    static final String SP_LISTINGS = "sp_listings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        boolean hasCache = getSharedPreferences(SP_LISTINGS, 0)
                .getBoolean(HAS_CACHE_KEY, false);
        if (hasCache) {
            String json = getSharedPreferences(SP_LISTINGS, 0)
                    .getString(ADD_LISTING_KEY, null);
            Type listingType = new TypeToken<List<Listing>>() {
            }.getType();

            mListings = new Gson().fromJson(json, listingType);
            if (mListings == null) {
                mListings = new ArrayList<Listing>();
            }
        }

        setUpUi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.action_listings:
                Toast.makeText(this, "hey", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null || data.getExtras() == null) {
            return;
        }

        String json = "";

        if (requestCode == EDIT_LISTING) {
            Bundle extras = data.getExtras();

            json = (String) extras.get(EDIT_LISTING_KEY);
            int idx = (Integer) extras.get(EDIT_INDEX);

            Listing listing = new Gson().fromJson(json, Listing.class);

            mListings.set(idx, listing);
        } else if (requestCode == ADD_NEW_LISTING) {
            json = (String) data.getExtras().get(ADD_LISTING_KEY);
            Listing listing = new Gson().fromJson(json, Listing.class);
            mListings.add(listing);
        }

        mAdapter.notifyDataSetChanged();

        SharedPreferences sp = getSharedPreferences(SP_LISTINGS, 0);
        SharedPreferences.Editor editor = sp.edit();

        boolean hasCache = sp.getBoolean(HAS_CACHE_KEY, false);
        if (!hasCache) {
            editor.putBoolean(HAS_CACHE_KEY, true);
            editor.apply();
        }

        Type listingsType = new TypeToken<List<Listing>>() {
        }.getType();
        json = new Gson().toJson(mListings, listingsType);

        editor.putString(ADD_LISTING_KEY, json);
        editor.apply();
    }

    private void setUpUi() {
        mListView = (ListView) findViewById(android.R.id.list);
        mAdapter = new ListingsAdapter(this, mListings);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Listing listing = mListings.get(position);

                String json = new Gson().toJson(listing, Listing.class);

                Intent sendListingForEditIntent = new Intent(ListingsActivity.this, EditListingActivity.class);
                sendListingForEditIntent.putExtra(EDIT_LISTING_KEY, json);
                sendListingForEditIntent.putExtra(EDIT_INDEX, position);

                startActivityForResult(sendListingForEditIntent, EDIT_LISTING);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.listings_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNewListingActivity = new Intent(
                        ListingsActivity.this, AddListingActivity.class);
                startActivityForResult(addNewListingActivity, ADD_NEW_LISTING, null);
            }
        });
        fab.attachToListView(mListView);
    }
}
