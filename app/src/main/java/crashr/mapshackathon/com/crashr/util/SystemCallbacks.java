package crashr.mapshackathon.com.crashr.util;

import java.util.List;

import crashr.mapshackathon.com.crashr.model.Listing;

/**
 * Created by Phillip on 11/16/2014.
 */
public interface SystemCallbacks {
    public void onListingsReadFromCache(List<Listing> listings);
}
