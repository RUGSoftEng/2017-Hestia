package hestia.UI;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.rugged.application.hestia.R;

import hestia.backend.NetworkHandler;

/**
 * An extension of the Application class provided by Android, which additionally contains the
 * NetworkHandler singleton.
 */

public class HestiaApplication extends Application {

    private NetworkHandler networkHandler;

    private static final String HESTIA_IP = "HESTIA.IP";
    private static final String SERVER_IP = "IP_OF_SERVER";

    @Override
    public void onCreate() {
        setupNetworkHandler();
        super.onCreate();
    }

    private void setupNetworkHandler() {
        SharedPreferences prefs = getSharedPreferences(HESTIA_IP, Context.MODE_PRIVATE);
        String ip = prefs.getString(SERVER_IP, null);
        networkHandler = new NetworkHandler(ip
                , Integer.valueOf(getApplicationContext().getString(R.string.default_port)));
    }

    public NetworkHandler getNetworkHandler(){
        return networkHandler;
    }
}
