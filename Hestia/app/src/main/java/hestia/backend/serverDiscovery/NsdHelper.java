package hestia.backend.serverDiscovery;

import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

public class NsdHelper {
    private NsdManager nsdManager;
    private NsdServiceInfo serviceInfo;
    private NsdManager.ResolveListener resolveListener;
    private NsdManager.DiscoveryListener discoveryListener;
    private String serviceName;
    private String serviceType;
    private static final String TAG = "NsdHelper";

    public NsdHelper(NsdManager nsdManager, String serviceName, String serviceType) {
        this.nsdManager = nsdManager;
        this.serviceName = serviceName;
        this.serviceType = serviceType;
        this.initializeNsd();
    }

    private void initializeNsd() {
        initializeDiscoveryListener();
        initializeResolveListener();
    }

    private void initializeDiscoveryListener() {
        discoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                Log.d(TAG, "Service found!!  " + service);
                if (!service.getServiceType().equals(serviceType)) {
                    Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
                } else if (service.getServiceName().equals(serviceName)) {
                    Log.d(TAG, "Same machine: " + serviceName);
                } else { // if (service.getServiceName().contains(serviceName)){
                    nsdManager.resolveService(service, resolveListener);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                Log.e(TAG, "service lost" + service);
                if (serviceInfo == service) {
                    serviceInfo = null;
                }
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.d(TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                nsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                nsdManager.stopServiceDiscovery(this);
            }
        };
    }

    private void initializeResolveListener() {
        resolveListener = new NsdManager.ResolveListener() {
            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.d(TAG, "Resolve Succeeded. " + serviceInfo);
                if (serviceInfo.getServiceName().equals(serviceName)) {
                    Log.d(TAG, "Same IP.");
                    return;
                }
                NsdHelper.this.serviceInfo = serviceInfo;
            }

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "Resolve failed" + errorCode);
            }
        };
    }

    public void discoverServices() {
        Log.d(TAG, "discoverServices() method called");
        nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
    }

    public NsdServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    public void tearDown() {
        nsdManager.stopServiceDiscovery(discoveryListener);
    }
}
