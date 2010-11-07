package org.droidbike;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.List;

public class DataDownloadTask extends AsyncTask<Void, Void, List<RentShopLocation>> {

    BikeMapActivity parent;

    public DataDownloadTask(BikeMapActivity parent) {
        this.parent = parent;
    }

    @Override
    protected List<RentShopLocation> doInBackground(Void... voids) {
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<RentShopLocation> locations) {

        parent.updateLocationOverlays(locations);
    }
}
