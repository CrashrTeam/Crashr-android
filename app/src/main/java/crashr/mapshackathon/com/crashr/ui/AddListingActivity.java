package crashr.mapshackathon.com.crashr.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import crashr.mapshackathon.com.crashr.R;
import crashr.mapshackathon.com.crashr.model.Listing;

public class AddListingActivity extends ActionBarActivity {

    private CheckBox mBreakfastWidget;
    private CheckBox mDinnerWidget;
    private CheckBox mShowerWidget;
    private CheckBox mWeedWidget;
    private EditText mCostWidget;
    private EditText mAddressWidget;
    private ImageView mImageView;

    private String mCurrentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;

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
                Toast.makeText(this, "pictures deferred", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_add_listing:
                ListingsActivity.mHackyListings.add(generateListing());
                NavUtils.navigateUpFromSameTask(this);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
        }
    }

    private void setUpUI() {
        mBreakfastWidget = (CheckBox) findViewById(R.id.add_breakfast);
        mDinnerWidget = (CheckBox) findViewById(R.id.add_dinner);
        mShowerWidget = (CheckBox) findViewById(R.id.add_shower);
        mWeedWidget = (CheckBox) findViewById(R.id.add_weed);
        mCostWidget = (EditText) findViewById(R.id.add_cost);
        mAddressWidget = (EditText) findViewById(R.id.add_address);
        //mImageView = (ImageView) findViewById(R.id.listing_img);
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

        return listing;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                // oh man
            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void galleryAddPicture() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPicture() {
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bitmapOptions);

        int photoW = bitmapOptions.outWidth;
        int photoH = bitmapOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        bitmapOptions.inJustDecodeBounds = false;
        bitmapOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bitmapOptions);
        mImageView.setImageBitmap(bitmap);
    }
}
