package org.droidbike;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import java.util.List;

public class BikeMapActivity extends MapActivity {

    private MapView mapView;
    private StationsOverlay stationsOverlay;
    //private BikesFalseSpaceTrueOverlay bikesFalseSpaceTrueOverlay;
    //private BikesTrueSpaceFalseOverlay bikesTrueSpaceFalseOverlay;
    private MyLocationOverlay myLocationOverlay;
    private AlertDialog alertDialog;
    private LocationManager locationManager;
    private Location currentLocation;

    GeoPoint viennaLocation = new GeoPoint(48209206, 16372778);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maptab);



        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);

        requestNewData();

        stationsOverlay = new StationsOverlay(this);

        myLocationOverlay = new MyLocationOverlay(this, mapView);
        myLocationOverlay.enableCompass();
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
//                mapView.getController().animateTo(myLocationOverlay.getMyLocation());
                mapView.getController().animateTo(viennaLocation);
                mapView.getController().setZoom(13);
            }
        });

        mapView.getOverlays().add(myLocationOverlay);
        mapView.getOverlays().add(stationsOverlay);
        // Meant to refresh the map but it's not happening
        mapView.postInvalidate();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if (LocationHelper.isBetterLocation(location, currentLocation)) {
                    currentLocation = location;
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver(newLocationsReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private BroadcastReceiver newLocationsReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            requestNewData();
        }
    };

    private void requestNewData() {
        DataDownloadTask task = new DataDownloadTask(this);
        task.execute();
    }

    public void updateLocationOverlays(List<RentShopLocation> rentShopLocations) {
        stationsOverlay.updateRentShopLocations(rentShopLocations);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quit:
                finish();
                return true;
            case R.id.refresh:
                requestNewData();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createResourceDisabledAlert(int messageText, int enableButtonString, final String settingsIntentString) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(messageText)
                .setCancelable(false)
                .setPositiveButton(enableButtonString,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                showSettingsOptions(settingsIntentString);
                            }
                        });
        builder.setNegativeButton(R.string.do_nothing_button,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showSettingsOptions(String settingsIntentString) {
        Intent gpsOptionsIntent = new Intent(settingsIntentString);
        startActivity(gpsOptionsIntent);
    }
}
