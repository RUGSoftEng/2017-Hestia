package hestia.UI.dialogs;

import android.content.Context;
import android.content.res.Configuration;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rugged.application.hestia.R;

import hestia.UI.HestiaApplication;
import hestia.backend.ServerCollectionsInteractor;

/**
 * This class represents the dialog screen with which the IP-address of the server is asked from the
 * user.
 */

public class ChangeIpDialog extends HestiaDialog {
    private final static String TAG = "ChangeIpDialog";
    private String ip;
    private EditText ipField;
    private Button discoveryButton;
    private ServerCollectionsInteractor serverCollectionsInteractor;
    private NsdManager nsdManager;
    private NsdManager.ResolveListener resolveListener;
    private NsdManager.DiscoveryListener discoveryListener;
    private String serviceName;
    private String serviceType;

    public static ChangeIpDialog newInstance() {
        ChangeIpDialog fragment = new ChangeIpDialog();
        return fragment;
    }

    public void setInteractor(ServerCollectionsInteractor interactor) {
        serverCollectionsInteractor = interactor;
    }

    @Override
    String buildTitle() {
        return "Change IP";
    }

    @Override
    View buildView() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.ip_dialog, null);
        nsdManager = (NsdManager) HestiaApplication.getContext().getSystemService(Context.NSD_SERVICE);

        this.addDiscoveryButton(view);
        this.addIpField(view);

        return view;
    }

    public void addIpField(View view) {
        ipField = (EditText) view.findViewById(R.id.ip);
        ipField.setRawInputType(Configuration.KEYBOARD_12KEY);

        String currentIP = serverCollectionsInteractor.getHandler().getIp();
        if (currentIP == null) {
            ipField.setHint("Enter ip here");
        } else {
            ipField.setText(currentIP);
        }
    }


    public void addDiscoveryButton(View view) {
        discoveryButton = (Button) view.findViewById(R.id.findServerButton);
        discoveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(discoveryListener != null) nsdManager.stopServiceDiscovery(discoveryListener);
                new AsyncTask<Object, Object, Void>() {
                    @Override
                    protected Void doInBackground(Object... params) {
                        serviceName = getResources().getString(R.string.serviceName);
                        serviceType = getResources().getString(R.string.serviceType);
                        initializeDiscoveryListener();
                        initializeResolveListener();
                        startDiscovery();

                        return null;
                    }

                }.execute();
            }
        });
    }

    private void initializeDiscoveryListener() {
        discoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
                Log.d(TAG, "  Service name: " + serviceName);
                Log.d(TAG, "  Service type: " + serviceType);
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                Log.d(TAG, "Service discovery success : " + service);
                Log.d(TAG, "  Host = "+ service.getServiceName());
                Log.d(TAG, "  Port = " + String.valueOf(service.getPort()));

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
                // TODO: do something once the code was found
                ip = serviceInfo.getHost().getHostAddress();
                Log.d(TAG, "Host ip: " + ip);
                ipField.setText(ip);
            }

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                Log.e(TAG, "Resolve failed. ErrorCode = " + errorCode);
                Log.e(TAG, "   Failed Service = " + serviceInfo);
            }
        };
    }

    private void startDiscovery() {
        nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener);
    }

    @Override
    public void pressConfirm() {
        ip = ipField.getText().toString();
        Log.i(TAG, "My ip is now:" + ip);
        if(ip != null) {
            serverCollectionsInteractor.getHandler().setIp(ip);
            Log.i(TAG, "My ip is changed to: " + ip);
            Toast.makeText(getContext(), serverCollectionsInteractor.getHandler().getIp(),
                    Toast.LENGTH_SHORT).show();
        }
        if(discoveryListener != null) {
            nsdManager.stopServiceDiscovery(discoveryListener);
        }
    }

    @Override
    public void pressCancel() {
        Toast.makeText(getContext(), "Cancel pressed", Toast.LENGTH_SHORT).show();
        if(discoveryListener != null) {
            nsdManager.stopServiceDiscovery(discoveryListener);
        }
    }
}
