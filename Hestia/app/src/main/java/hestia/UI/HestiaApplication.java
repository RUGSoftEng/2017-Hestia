package hestia.UI;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.rugged.application.hestia.R;

import hestia.backend.NetworkHandler;

/**
 * An extension of the Application class provided by Android, which additionally creates
 * and manages the network handler for the entire activity
 */
public class HestiaApplication extends Application {
    private static Context mContext;
    private NetworkHandler networkHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        setupNetworkHandler();
        mContext = this;
    }

    private void setupNetworkHandler() {
        SharedPreferences prefs = getSharedPreferences(
                getApplicationContext().getString(R.string.hestiaIp), Context.MODE_PRIVATE);
        String ip = prefs.getString(getApplicationContext().getString(R.string.ipOfServer), null);
        networkHandler = new NetworkHandler(ip, Integer.valueOf(getApplicationContext().
                getString(R.string.defaultPort)));
    }

    public NetworkHandler getNetworkHandler() {
        return networkHandler;
    }

    public static Context getContext() {
        return mContext;
    }
}
