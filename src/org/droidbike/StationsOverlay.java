package org.droidbike;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import java.util.ArrayList;
import java.util.List;

public class StationsOverlay extends ItemizedOverlay {
    private List<OverlayItem> locations = new ArrayList<OverlayItem>();
    private Drawable bikesTrueFreespaceTrue, bikesTrueFreespaceFalse, bikesFalseFreespaceTrue;
    private Context context;
    private Toast toast;

    public StationsOverlay(Context context) {
        super(boundCenterBottom(context.getResources().getDrawable(R.drawable.bikes_true_freespace_true)));
        bikesTrueFreespaceTrue = context.getResources().getDrawable(R.drawable.bikes_true_freespace_true);
        bikesTrueFreespaceTrue.setBounds(-bikesTrueFreespaceTrue.getIntrinsicWidth() / 2, -bikesTrueFreespaceTrue.getIntrinsicHeight(), bikesTrueFreespaceTrue.getIntrinsicWidth() / 2, 0);

        bikesTrueFreespaceFalse = context.getResources().getDrawable(R.drawable.bikes_true_freespace_false);
        bikesTrueFreespaceFalse.setBounds(-bikesTrueFreespaceFalse.getIntrinsicWidth() / 2, -bikesTrueFreespaceFalse.getIntrinsicHeight(), bikesTrueFreespaceFalse.getIntrinsicWidth() / 2, 0);
        bikesFalseFreespaceTrue = context.getResources().getDrawable(R.drawable.bikes_false_freespace_true);
        bikesFalseFreespaceTrue.setBounds(-bikesFalseFreespaceTrue.getIntrinsicWidth() / 2, -bikesFalseFreespaceTrue.getIntrinsicHeight(), bikesFalseFreespaceTrue.getIntrinsicWidth() / 2, 0);
        this.context = context;

        populate();

    }

    @Override
    protected OverlayItem createItem(int i) {
        return locations.get(i);
    }

    @Override
    public int size() {
        return locations.size();
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean b) {
        super.draw(canvas, mapView, b);
        //boundCenterBottom(bikesTrueFreespaceTrue);
    }

    @Override
    protected boolean onTap(int index) {
        OverlayItem item = locations.get(index);
        if (toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(context, item.getTitle() + "\n " + item.getSnippet(), Toast.LENGTH_SHORT);
        toast.show();

        return true;
    }

    public void updateRentShopLocations(List<RentShopLocation> rentLocations) {
        locations.clear();
        for (RentShopLocation location : rentLocations) {

            int lon = (int) Math.round(location.longitude * 1E6);
            int lat = (int) Math.round(location.latitude * 1E6);

            OverlayItem overlayItem = new OverlayItem(new GeoPoint(lat, lon), location.description, "Bikes available: " + location.freeBikes + ", slots free: " + location.freeBoxes);

            if (location.freeBikes > 0 && location.freeBoxes > 0) {
                overlayItem.setMarker(bikesTrueFreespaceTrue);
            }
            else if (location.freeBikes > 0 && location.freeBoxes == 0) {
                 overlayItem.setMarker(bikesTrueFreespaceFalse);
                }
            else if (location.freeBikes == 0 && location.freeBoxes > 0) {
                overlayItem.setMarker(bikesFalseFreespaceTrue);
            }

            locations.add(overlayItem);
            Log.w("BikeMap", "new location: lat=" + lat + " lon=" + lon);
        }
        populate();
    }

}