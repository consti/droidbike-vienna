package org.droidbike;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TabHost;

public class BikeTabs extends TabActivity {
    TabHost mTabHost;
    FrameLayout mFrameLayout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs);
        mTabHost = getTabHost();
        TabHost.TabSpec tabSpec = mTabHost.newTabSpec("tab_test1");
        tabSpec.setIndicator("Map");
        Context ctx = this.getApplicationContext();
        Intent i = new Intent(ctx, BikeMapActivity.class);
        tabSpec.setContent(i);
        mTabHost.addTab(tabSpec);

        // Second tab with the Listview
        TabHost.TabSpec tab_test2 = mTabHost.newTabSpec("tab_test2");
        tab_test2.setIndicator("List");
        tab_test2.setContent(R.id.textview2);
        Intent i2 = new Intent(ctx, BikeListActivity.class);
        tab_test2.setContent(i2);
        mTabHost.addTab(tab_test2);
        mTabHost.setCurrentTab(0);
    }

}
