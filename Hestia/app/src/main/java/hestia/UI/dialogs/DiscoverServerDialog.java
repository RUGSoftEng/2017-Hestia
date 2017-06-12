package hestia.UI.dialogs;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rugged.application.hestia.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hestia.backend.NetworkHandler;

/**
 * This class represents the dialog screen with which the IP-address of the server is asked from the
 * user.
 */

public class DiscoverServerDialog extends HestiaDialog {
    private final static String TAG = "DiscoverServerDialog";
    private String foundIp;
    private EditText ipField;
    private Button discoverButton;
    private TextView status;
    private NetworkHandler handler;
    private NsdManager.ResolveListener resolveListener;
    private NsdManager.DiscoveryListener discoveryListener;
    private NsdManager nsdManager;
    private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    public static DiscoverServerDialog newInstance() {
        DiscoverServerDialog fragment = new DiscoverServerDialog();
        return fragment;
    }

    public void setNetworkHandler(NetworkHandler handler) {
        this.handler = handler;
    }

    @Override
    String buildTitle() {
        return getString(R.string.findServerTitle);
    }

    @Override
    View buildView() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.discover_server_dialog, null);
        initStatus(view);
        initDiscoverButton(view);
        initIpField(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            discoverServer();
        } else {
            status.setText(R.string.versionTooLow);
        }

        return view;
    }

    private void discoverServer() {
        new AsyncTask<Object, Object, Void>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            protected Void doInBackground(Object... params) {
                nsdManager = (NsdManager) getContext().getSystemService(Context.NSD_SERVICE);
                initializeDiscoveryListener();
                nsdManager.discoverServices(getString(R.string.serviceType), NsdManager.PROTOCOL_DNS_SD, discoveryListener);
                return null;
            }
        }.execute();
    }

    private void initStatus(View view) {
        status = (TextView) view.findViewById(R.id.status);
        status.setEnabled(false);
        status.setFocusable(false);
        status.setText(R.string.searching);
    }

    private void initIpField(View view) {
        ipField = (EditText) view.findViewById(R.id.ip);
        ipField.setRawInputType(Configuration.KEYBOARD_12KEY);
        String currentIP = handler.getIp();
        if (currentIP != null) {
            ipField.setText(currentIP);
            ipField.setSelection(ipField.getText().length());
        }
    }

    private void initDiscoverButton(View view) {
        discoverButton = (Button) view.findViewById(R.id.discover);
        discoverButton.setText(R.string.autocomplete);
        discoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ipField.setText(foundIp);
                ipField.setSelection(ipField.getText().length());
            }
        });
        discoverButton.setEnabled(false);
    }


    @Override
    void pressConfirm() {
        String ip = ipField.getText().toString();
        if (checkIp(ip)) {
            handler.setIp(ip);
        } else {
            Toast.makeText(getContext(), getString(R.string.incorrIp), Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    void pressCancel() {
        Toast.makeText(getContext(), "Cancel pressed", Toast.LENGTH_SHORT).show();
        nsdManager.stopServiceDiscovery(discoveryListener);
        discoveryListener = null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initializeDiscoveryListener() {
        discoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
                Log.d(TAG, "  Service name: " + getString(R.string.serviceName));
                Log.d(TAG, "  Service type: " + getString(R.string.serviceType));
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                if (!service.getServiceType().equals(getString(R.string.serviceType))) {
                    Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
                } else if (service.getServiceName().equals(getString(R.string.serviceName))) {
                    Log.d(TAG, "Same machine: " + getString(R.string.serviceName));
                } else {
                    Log.d(TAG, "Found a different Host: " + getString(R.string.serviceName));
                    initializeResolveListener();
                    nsdManager.resolveService(service, resolveListener);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                Log.e(TAG, "service lost" + service);
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.d(TAG, "Discovery stopped: " + serviceType);
                ;
                Handler mainHandler = new Handler(DiscoverServerDialog.this.getContext().getMainLooper());
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        status.setText(getString(R.string.serverNotFound));
                    }
                };
                mainHandler.post(myRunnable);
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initializeResolveListener() {
        resolveListener = new NsdManager.ResolveListener() {
            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.d(TAG, "Resolve Succeeded. " + serviceInfo);
                if (serviceInfo.getServiceName().equals(getString(R.string.serviceName))) {
                    Log.d(TAG, "Same IP.");
                    return;
                }
                foundIp = serviceInfo.getHost().getHostAddress();
                Handler mainHandler = new Handler(DiscoverServerDialog.this.getContext().getMainLooper());
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
                        status.setText(getString(R.string.serverFound) + foundIp);
                        discoverButton.setEnabled(true);
                    }
                };
                mainHandler.post(myRunnable);
            }

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "Resolve failed" + errorCode);
            }
        };
    }

    private boolean checkIp(String ip) {
        Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }
}
