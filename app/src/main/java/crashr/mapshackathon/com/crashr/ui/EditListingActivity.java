package crashr.mapshackathon.com.crashr.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import crashr.mapshackathon.com.crashr.R;
import crashr.mapshackathon.com.crashr.model.Listing;

public class EditListingActivity extends ActionBarActivity {
    private CheckBox mBreakfastWidget;
    private CheckBox mDinnerWidget;
    private CheckBox mShowerWidget;
    private CheckBox mWeedWidget;
    private EditText mCostWidget;
    private EditText mAddressWidget;
    private EditText mDescriptionWidget;

    private Listing mListing;
    private int mEditingIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_listing);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle etras = getIntent().getExtras();

        String json = etras.getString(ListingsActivity.EDIT_LISTING_KEY);

        mListing = new Gson().fromJson(json, Listing.class);
        mEditingIndex = etras.getInt(ListingsActivity.EDIT_INDEX);

        setUpUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_listing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.action_add_photo:
                Toast.makeText(this, "photos deferred", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_edit_listing:
                String json = new Gson().toJson(generateListing(), Listing.class);

                Intent sendEditedListingBackIntent = new Intent(Intent.ACTION_EDIT);
                sendEditedListingBackIntent.putExtra(ListingsActivity.EDIT_LISTING_KEY, json);
                sendEditedListingBackIntent.putExtra(ListingsActivity.EDIT_INDEX, mEditingIndex);
                setResult(ListingsActivity.EDIT_LISTING, sendEditedListingBackIntent);

                finish();
                break;
            case R.id.action_delete:
                Toast.makeText(this, "deleting coming soon!", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpUI() {
        mBreakfastWidget = (CheckBox) findViewById(R.id.edit_breakfast);
        mBreakfastWidget.setChecked(mListing.isBreakfastAvailable);

        mDinnerWidget = (CheckBox) findViewById(R.id.edit_dinner);
        mDinnerWidget.setChecked(mListing.isDinnerAvailable);

        mShowerWidget = (CheckBox) findViewById(R.id.edit_shower);
        mShowerWidget.setChecked(mListing.isShowerAvailable);

        mWeedWidget = (CheckBox) findViewById(R.id.edit_weed);
        mWeedWidget.setChecked(mListing.isWeedAvailable);

        mCostWidget = (EditText) findViewById(R.id.edit_cost);
        mCostWidget.setText(String.valueOf(mListing.cost));

        mAddressWidget = (EditText) findViewById(R.id.edit_address);
        mAddressWidget.setText(mListing.address);

        mDescriptionWidget = (EditText) findViewById(R.id.edit_description);
        mDescriptionWidget.setText(mListing.description);
    }

    private Listing generateListing() {
        Listing listing = new Listing();
        listing.isBreakfastAvailable = mBreakfastWidget.isChecked();
        listing.isDinnerAvailable = mDinnerWidget.isChecked();
        listing.isShowerAvailable = mShowerWidget.isChecked();
        listing.isWeedAvailable = mWeedWidget.isChecked();

        String costString = mCostWidget.getText().toString();
        listing.cost = (costString == null || costString.isEmpty())
                ? 0 : Double.valueOf(costString);
        listing.address = mAddressWidget.getText().toString();
        listing.description = mDescriptionWidget.getText().toString();

        return listing;
    }
}
