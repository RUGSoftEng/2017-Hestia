package hestia.backend.serverDiscovery;

import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

public class NsdHelper {
    private NsdManager nsdManager;
    private WifiManager wifi;
    private NsdServiceInfo serviceInfo;
    private NsdManager.ResolveListener resolveListener;
    private NsdManager.DiscoveryListener discoveryListener;
    private String serviceName;
    private String serviceType;
    private String hostIpAddress;
    private static final String TAG = "NsdHelper";

    public NsdHelper(NsdManager nsdManager, WifiManager wifi, String serviceName, String serviceType) {
        this.nsdManager = nsdManager;
        this.wifi = wifi;
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
                // A service was found! Do something with it.
                Log.d(TAG, "Service discovery success : " + service);
                Log.d(TAG, "    Host = "+ service.getServiceName());
                Log.d(TAG, "    Port = " + String.valueOf(service.getPort()));

                if (!service.getServiceType().equals(serviceType)) {
                    Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
                } else if (service.getServiceName().equals(serviceName)) {
                    Log.d(TAG, "Same machine: " + serviceName);
                } else {
                    Log.d(TAG, "Diff Machine : " + service.getServiceName());
                    nsdManager.resolveService(service, resolveListener);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                Log.e(TAG, "service lost: " + service);
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
                NsdHelper.this.setHostIpAddress(NsdHelper.this.getLocalIpAddress());
            }

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "Resolve failed. ErrorCode = " + errorCode);
                Log.e(TAG, "   Failed Service = " + serviceInfo);
            }
        };
    }

    public String getLocalIpAddress() {
        Integer ipAddress = wifi.getConnectionInfo().getIpAddress();
        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }
        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();
        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            Log.e("WIFIIP", "Unable to get host address.");
            ipAddressString = null;
        }
        return ipAddressString;
    }

    public void discoverServices() {
        Log.d(TAG, "discoverServices() method called");
        nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
    }

    public NsdServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    public void setHostIpAddress(String ipAddress) {
        this.hostIpAddress = ipAddress;
    }

    public String getHostIpAddress() {
        return this.hostIpAddress;
    }

    public void tearDown() {
        nsdManager.stopServiceDiscovery(discoveryListener);
    }
}
