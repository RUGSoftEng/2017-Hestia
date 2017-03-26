/**
 * This class smoothens the transition from an activity to a fragment.
 */

package com.rugged.application.hestia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public abstract class SingleFragmentActivity extends AppCompatActivity{

    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setIp:
                showIpDialog();
                return true;

            case R.id.action_logout:
                gotoLoginActivity();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void gotoLoginActivity(){
        Intent i = new Intent(SingleFragmentActivity.this, LoginActivity.class);
        String s = null;
        i.putExtra("login", s);
        startActivity(i);
        finish();
    }

    private void showIpDialog() {
        IpDialog ipDialog = new IpDialog(SingleFragmentActivity.this);
        ipDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        ipDialog.show();
    }
}
