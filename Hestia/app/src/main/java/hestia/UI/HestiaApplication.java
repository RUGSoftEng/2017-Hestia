package hestia.UI;

import android.app.Application;

import hestia.backend.NetworkHandler;

/**
 * An extension of the Application class provided by Android, which additionally contains the
 * NetworkHandler singleton.
 * @see NetworkHandler
 */

public class HestiaApplication extends Application {
    private static final NetworkHandler NETWORK_HANDLER = NetworkHandler.getInstance();

   /* public NetworkHandler getBackendInteractor() {
        return NETWORK_HANDLER;
    }
    */

}
