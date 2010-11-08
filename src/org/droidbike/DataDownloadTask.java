package org.droidbike;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DataDownloadTask extends AsyncTask<Void, Void, List<RentShopLocation>> {

    BikeMapActivity parent;

    public DataDownloadTask(BikeMapActivity parent) {
        this.parent = parent;
    }

    @Override
    protected List<RentShopLocation> doInBackground(Void... voids) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("http://dynamisch.citybikewien.at/citybike_xml.php");
        HttpResponse response = null;

        try {
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();

                return SaxParser.parseXmlFile(instream);
            }
        } catch (IOException e) {
            Log.e("DataDownloadTask", e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<RentShopLocation> locations) {
        Log.e("DB1", "updating..");

        parent.updateLocationOverlays(locations);
    }
}
