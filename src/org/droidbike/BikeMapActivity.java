package org.droidbike;

import android.app.AlertDialog;
import android.content.*;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

import java.util.List;

public class BikeMapActivity extends MapActivity {

    private MapView mapView;
    private Drawable rentLocationMarker;
    private BikeMapOverlay rentLocationsOverlay;
    private MyLocationOverlay myLocationOverlay;
    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bike_map);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);

        rentLocationMarker = this.getResources().getDrawable(R.drawable.danger);
        rentLocationsOverlay = new BikeMapOverlay(rentLocationMarker, this);

        myLocationOverlay = new MyLocationOverlay(this, mapView);
        myLocationOverlay.enableCompass();
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                mapView.getController().animateTo(myLocationOverlay.getMyLocation());
                mapView.getController().setZoom(12);
            }
        });

        mapView.getOverlays().add(myLocationOverlay);
        mapView.getOverlays().add(rentLocationsOverlay);
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
        unregisterReceiver(newLocationsReceiver);
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
        rentLocationsOverlay.updateRentShopLocations(rentShopLocations);
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