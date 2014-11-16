package crashr.mapshackathon.com.crashr.ui;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import crashr.mapshackathon.com.crashr.R;
import crashr.mapshackathon.com.crashr.model.Listing;

public class AddListingActivity extends ActionBarActivity {

    private CheckBox mBreakfastWidget;
    private CheckBox mDinnerWidget;
    private CheckBox mShowerWidget;
    private CheckBox mWeedWidget;
    private EditText mCostWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUpUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_listing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.action_add_photo:
                Toast.makeText(this, "pics coming soon!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_add_listing:
                Listing listing = generateListing();
                NavUtils.navigateUpFromSameTask(this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpUI() {
        mBreakfastWidget = (CheckBox) findViewById(R.id.add_breakfast);
        mDinnerWidget = (CheckBox) findViewById(R.id.add_dinner);
        mShowerWidget = (CheckBox) findViewById(R.id.add_shower);
        mWeedWidget = (CheckBox) findViewById(R.id.add_weed);
        mCostWidget = (EditText) findViewById(R.id.add_cost);
    }

    private Listing generateListing() {
        Listing listing = new Listing();
        listing.isBreakfastAvailable = mBreakfastWidget.isChecked();
        listing.isDinnerAvailable = mDinnerWidget.isChecked();
        listing.isShowerAvailable = mShowerWidget.isChecked();
        listing.isWeedAvailable = mWeedWidget.isChecked();
        listing.cost = Integer.valueOf(mCostWidget.getText().toString());

        return listing;
    }
}
