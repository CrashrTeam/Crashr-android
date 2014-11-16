package crashr.mapshackathon.com.crashr;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Phillip on 11/15/2014.
 */
public interface PlacesCallbacks {

    public void onError();
    
    public void onPlacesByLocationFound(List<LatLng> locations);
}
