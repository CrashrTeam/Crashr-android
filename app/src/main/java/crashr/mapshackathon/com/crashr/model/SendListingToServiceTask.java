package crashr.mapshackathon.com.crashr.model;

import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

public class SendListingToServiceTask extends AsyncTask<Void, Void, Void> {
    private Listing mListing;

    public SendListingToServiceTask(Listing listing) {
        mListing = listing;
    }

    @Override
    protected Void doInBackground(Void... params) {
        String Url = "http://crashr-app-test.appspot.com/addlisting" +
                "?un=" + "obama" +
                "&st_add=" + mListing.address +
                "&city=" + "corvallis" +
                "&state=" + "oregon" +
                "&zip=" + 97330 +
                "&desc=" + mListing.address +
                "&food=" + (mListing.isDinnerAvailable ? "true" : "false");

        Url = Url.replace(" ", "%20");

        try {
            URL url = new URL(Url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("LITERAL FUCKING YOLO", "The response is: " + response);
        } catch (Exception ioe) {
            Log.d("LITERAL FUCKING YOLO", ioe.getMessage());
        }

        return null;
    }


}
