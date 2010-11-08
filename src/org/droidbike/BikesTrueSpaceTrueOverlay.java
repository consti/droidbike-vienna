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

public class BikesTrueSpaceTrueOverlay extends ItemizedOverlay {
    private List<OverlayItem> locations = new ArrayList<OverlayItem>();
    private Drawable bikesTrueFreespaceTrue, bikesTrueFreespaceFalse, bikesFalseFreespaceTrue;
    private Context context;
    private Toast toast;

    public BikesTrueSpaceTrueOverlay(Drawable drawable, Context context) {
        super(boundCenterBottom(drawable));
        bikesTrueFreespaceTrue = drawable;
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
        boundCenterBottom(bikesTrueFreespaceTrue);
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
            if (location.freeBikes > 0 && location.freeBoxes > 0) {
                int lon = (int) Math.round(location.longitude * 1E6);
                int lat = (int) Math.round(location.latitude * 1E6);
                String description = location.description;
                OverlayItem overlayItem = new OverlayItem(new GeoPoint(lat, lon), description,
                        "bikes available: " + location.freeBikes + "  boxes free: " + location.freeBoxes);

                locations.add(overlayItem);
                Log.w("BikeMap", "new location: lat=" + lat + " lon=" + lon);
            }
        }
        populate();
    }

}