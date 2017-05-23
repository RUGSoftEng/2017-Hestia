package hestia.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import hestia.backend.NetworkHandler;
import hestia.backend.Cache;

/**
 * The activity which presents a list containing all peripherals to the user. An activity is a
 * single, focused thing the user can do. Also setting the IP is added for improved persistency.
 */

public class DeviceListActivity extends SingleFragmentActivity {
    private static final String HESTIA_IP = "HESTIA.IP";
    private static final String SERVER_IP = "IP_OF_SERVER";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Cache cache = Cache.getInstance();
        SharedPreferences prefs = getSharedPreferences(HESTIA_IP, 0);
        cache.setIp(prefs.getString(SERVER_IP, cache.getIp()));
    }

    /**
     * When the app resumes, the list of devices is refreshed automatically using onResume.
     */
    @Override
    public void onResume(){
        super.onResume();
        NetworkHandler.getInstance().updateDevices();
    }

    @Override
    protected Fragment createFragment() {
        return new DeviceListFragment();
    }

    @Override
    protected void onStop() {
        storeIP();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        storeIP();
        super.onDestroy();
    }

    private void storeIP(){
        Cache cache = Cache.getInstance();
        SharedPreferences.Editor prefs = getSharedPreferences(HESTIA_IP, 0).edit();
        prefs.putString(SERVER_IP, cache.getIp()).apply();
    }
}
