package crashr.mapshackathon.com.crashr.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.melnykov.fab.FloatingActionButton;
import com.r0adkll.postoffice.PostOffice;
import com.r0adkll.postoffice.model.Design;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import crashr.mapshackathon.com.crashr.PlacesCallbacks;
import crashr.mapshackathon.com.crashr.R;
import crashr.mapshackathon.com.crashr.model.Tuple;

public class MapActivity extends FragmentActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        PlacesCallbacks {

    private final static int
            CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private GoogleMap mMap;
    private LocationClient mLocationClient;
    private SlidingUpPanelLayout mSlide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLocationClient = new LocationClient(this, this, this);

        setContentView(R.layout.activity_map);

        setUpUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMapIfNeeded();
    }

    @Override
    protected void onStart() {
        super.onStart();

        getMapIfNeeded();

        Tuple<Integer, Boolean> result = servicesConnected();
        if (result.item2) {
            mLocationClient.connect();
        } else {
            launchServiceDialog(result.item1);
        }
    }

    @Override
    protected void onStop() {
        mLocationClient.disconnect();
        super.onStop();
    }

    /**
     * Handles toggling the view of the Slider.
     */
    @Override
    public void onBackPressed() {
        if (mSlide != null &&
                (mSlide.isPanelExpanded() || mSlide.isPanelAnchored())) {
            mSlide.collapsePanel();
        } else if (mSlide != null && !mSlide.isPanelHidden()) {
            mSlide.hidePanel();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = mLocationClient.getLastLocation();
        if (location != null && mMap != null) {
            // GetPlacesByLocation spun off

            setUpMap(location);
        }
    }

    @Override
    public void onDisconnected() {
        // maybe something
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(
                        this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                Log.d("location", "failed to start connection resolution", e);
            }
        }
    }

    @Override
    public void onPlacesByLocationFound(List<LatLng> locations) {
        addMarkers(locations);
    }

    @Override
    public void onError() {
        PostOffice.newMail(this)
                .setTitle("Network Error")
                .setMessage("Problem getting info from the network.")
                .setDesign(Design.MATERIAL_LIGHT)
                .setCanceledOnTouchOutside(true)
                .show(getSupportFragmentManager());
    }

    /**
     * Launches an ErrorDialog based on the result code from
     * a failed GPlay service connection attempt.
     */
    private void launchServiceDialog(int resultCode) {
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                resultCode,
                this,
                CONNECTION_FAILURE_RESOLUTION_REQUEST);

        if (errorDialog != null) {
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();
            errorFragment.setDialog(errorDialog);
            errorFragment.show(getSupportFragmentManager(), "Location Updates");
        }
    }

    /**
     * Checks if the device can connect to GPlay services.
     *
     * @return A tuple containing the result code of the service call,
     * and a boolean representing if GPlay services were available.
     */
    private Tuple<Integer, Boolean> servicesConnected() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        return Tuple.create(resultCode, resultCode == ConnectionResult.SUCCESS);
    }

    private void setUpUi() {
        getMapIfNeeded();
        setUpSlider();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, ListingsActivity.class);

                // put stuff in bundle later?

                startActivity(intent);
            }
        });
    }

    private void getMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
        }
    }

    private void setUpMap(Location location) {
        LatLng latLng = (location == null)
                ? new LatLng(0, 0)
                : new LatLng(location.getLatitude(),
                location.getLongitude());

        mMap.getUiSettings().setZoomControlsEnabled(false);

        mMap.setMyLocationEnabled(true);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(16)
                .build();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                MapActivity.this.mSlide.showPanel();
                return false;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                SlidingUpPanelLayout slider = MapActivity.this.mSlide;
                if (slider != null && !slider.isPanelHidden()) {
                    slider.hidePanel();
                }
            }
        });
    }

    private void addMarkers(List<LatLng> locations) {
        for (LatLng latLng : locations) {
            mMap.addMarker(new MarkerOptions()
                    .position(latLng));
        }
    }

    private void setUpSlider() {
        mSlide = (SlidingUpPanelLayout) findViewById(R.id.info_slide);
        mSlide.hidePanel();
        mSlide.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {

            }

            @Override
            public void onPanelCollapsed(View view) {

            }

            @Override
            public void onPanelExpanded(View view) {

            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        });
    }

    /**
     * Nested class designed to easily facilitate an error dialog that
     * can manage its own lifecycle, rather than relying on the calling activity.
     */
    public static class ErrorDialogFragment extends DialogFragment {
        private Dialog mDialog;

        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
}
