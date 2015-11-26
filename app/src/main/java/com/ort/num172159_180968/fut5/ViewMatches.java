package com.ort.num172159_180968.fut5;

import android.app.LocalActivityManager;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

public class ViewMatches extends AppCompatActivity {

    private TabHost tabHost;

    private TabHost.TabSpec tab1;
    private TabHost.TabSpec tab2;
    private TabHost.TabSpec tab3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_matches);

        LocalActivityManager mlam = new LocalActivityManager(this, false);
        mlam.dispatchCreate(savedInstanceState);

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup(mlam);

        TabHost.TabSpec tab1 = tabHost.newTabSpec("Finished");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Next");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Others");

        tab1.setIndicator("Finished");
        Intent intent = new Intent(this,TabMatches.class);
        intent.putExtra("type","finished");
        tab1.setContent(intent);

        tab2.setIndicator("Next");
        Intent intent2 = new Intent(this,TabMatches.class);
        intent.putExtra("type", "next");
        tab2.setContent(intent2);

        tab3.setIndicator("Others");
        Intent intent3 = new Intent(this,TabMatches.class);
        intent.putExtra("type", "ohers");
        tab3.setContent(intent3);

        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);

        tabHost.setCurrentTab(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_matches, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
