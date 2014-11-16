package crashr.mapshackathon.com.crashr.util;

import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import crashr.mapshackathon.com.crashr.model.Listing;

public class ReadListingFromCacheTask extends AsyncTask<Void, Void, List<Listing>> {
    private SharedPreferences mSharedPreferences;
    private String mListingKey;
    private SystemCallbacks mCallbacks;

    public ReadListingFromCacheTask(SystemCallbacks callbacks,
                                    SharedPreferences sharedPreferences,
                                    String listingKey) {
        mCallbacks = callbacks;
        mSharedPreferences = sharedPreferences;
        mListingKey = listingKey;
    }

    @Override
    protected List<Listing> doInBackground(Void... params) {
        String json = mSharedPreferences.getString(mListingKey, null);
        Type listingType = new TypeToken<List<Listing>>() {}.getType();

        return new Gson().fromJson(json, listingType);
    }

    @Override
    protected void onPostExecute(List<Listing> listings) {
        mCallbacks.onListingsReadFromCache(listings);
    }
}
