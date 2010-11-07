package org.droidbike;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import java.util.ArrayList;
import java.util.List;

public class BikeMapOverlay extends ItemizedOverlay {
    private List<OverlayItem> locations = new ArrayList<OverlayItem>();
    private Drawable marker;
    private Context context;
    private Toast toast;

    public BikeMapOverlay(Drawable drawable, Context context) {
        super(boundCenterBottom(drawable));
        marker = drawable;
        this.context = context;
//        GeoPoint point1 = new
//                GeoPoint((int) (104.418971 * 1000000), (int) (-81.581436 * 1000000));
//        GeoPoint point2 = new
//                GeoPoint((int) (28.410067 * 1000000), (int) (-81.583699 * 1000000));
        //   locations.add(new OverlayItem(point1, "Magic Kingdom", "Magic Kingdom"));
//        locations.add(new OverlayItem(point2, "Seven Lagoon", "Seven Lagoon"));
//        locations.add(new OverlayItem(new GeoPoint((int) 46061708, (int) 14468432), "Večna pot", "Success"));
//        locations.add(new OverlayItem(new GeoPoint((int) 46049065, (int) 14488478), "Tacenska", "Success"));
//        locations.add(new OverlayItem(new GeoPoint((int) 46109256, (int) 14461787), "Tržaška", "Success"));

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
        boundCenterBottom(marker);
    }

    @Override
    protected boolean onTap(int index) {
        OverlayItem item = locations.get(index);
        if (toast != null) {
            toast.cancel();
        }
//        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//        dialog.setTitle(item.getTitle());
//        dialog.setMessage(item.getSnippet());
//        dialog.show();

        toast = Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT);
        toast.show();

        return true;
    }

    public void updateRentShopLocations(List<RentShopLocation> rentLocations) {
        locations.clear();
        for (RentShopLocation location : rentLocations) {
            int lon = (int) Math.round(location.longitude * 1E6);
            int lat = (int) Math.round(location.latitude * 1E6);
            String description = location.description;
            locations.add(new OverlayItem(new GeoPoint(lat, lon), description, "second string"));
        }
        populate();
    }

}