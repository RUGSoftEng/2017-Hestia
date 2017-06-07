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

import hestia.backend.NetworkHandler;
import hestia.backend.ServerCollectionsInteractor;
import hestia.backend.serverDiscovery.NsdHelper;

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
                new AsyncTask<Object, Object, NsdServiceInfo>() {
                    @Override
                    protected NsdServiceInfo doInBackground(Object... params) {
                        NsdManager nsdManager = (NsdManager) getContext().getSystemService(Context.NSD_SERVICE);
                        String serviceName = getResources().getString(R.string.serviceName);
                        String serviceType = getResources().getString(R.string.serviceType);
                        NsdHelper nsdHelper = new NsdHelper(nsdManager, serviceName, serviceType);
                        nsdHelper.discoverServices();
                        NsdServiceInfo serviceInfo = nsdHelper.getServiceInfo();
                        return serviceInfo;
                    }

                    @Override
                    protected void onPostExecute(NsdServiceInfo serviceInfo) {
                        if(serviceInfo != null) {
                            Log.d(TAG, "ServiceInfo is NOT null");
                            String ip = serviceInfo.getHost().getHostAddress();
                            Integer port = serviceInfo.getPort();
                            serverCollectionsInteractor.setHandler(new NetworkHandler(ip, port));
                            //nsdHelper.tearDown();
                        } else {
                            Log.d(TAG, "ServiceInfo is null");
                            String message = "Server not found, enter the IP manually";
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
            }
        });
    }

    @Override
    public void pressConfirm() {
        ip = ipField.getText().toString();
        Log.i(TAG, "My ip is now:" + ip);
        if(ip!=null) {
            serverCollectionsInteractor.getHandler().setIp(ip);
            Log.i(TAG, "My ip is changed to: " + ip);
            Toast.makeText(getContext(), serverCollectionsInteractor.getHandler().getIp(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void pressCancel() {
        Toast.makeText(getContext(), "Cancel pressed", Toast.LENGTH_SHORT).show();
    }
}
