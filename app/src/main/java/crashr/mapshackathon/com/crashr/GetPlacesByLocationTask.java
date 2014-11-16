package crashr.mapshackathon.com.crashr;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import crashr.mapshackathon.com.crashr.model.Tuple;
import crashr.mapshackathon.com.crashr.util.WebUtil;

public class GetPlacesByLocationTask extends AsyncTask<Void, Void, Tuple<Boolean, List<LatLng>>> {
    private PlacesCallbacks mCallbacks;
    private LatLng mLocation;

    public GetPlacesByLocationTask(PlacesCallbacks callbacks, LatLng location) {
        mCallbacks = callbacks;
        mLocation = location;
    }

    @Override
    protected Tuple<Boolean, List<LatLng>> doInBackground(Void... params) {
        Tuple<Boolean, String> result = getNearbyLocations();
        if (!result.item1) {
            return Tuple.create(false, null);
        }

        String json = result.item2;



        return null;
    }

    @Override
    protected void onPostExecute(Tuple<Boolean, List<LatLng>> result) {
        if (result.item1 && result.item2 != null && !result.item2.isEmpty()) {
            mCallbacks.onPlacesByLocationFound(result.item2);
        } else {
            mCallbacks.onError();
        }
    }

    private Tuple<Boolean, String> getNearbyLocations() {
        String json = "";
        boolean networkError = false;

        try {
            json = WebUtil.downloadUrl("theurlgoeshere" +
                                        "?lat=" + mLocation.latitude +
                                        "&lng=" + mLocation.longitude);
        } catch (IOException e) {
            networkError = true;
        }

        return Tuple.create(networkError, json);
    }
}
