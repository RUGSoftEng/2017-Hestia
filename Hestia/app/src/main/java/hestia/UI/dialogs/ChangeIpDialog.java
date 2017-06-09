package hestia.UI.dialogs;

import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.rugged.application.hestia.R;

import hestia.backend.ServerCollectionsInteractor;

/**
 * This class represents the dialog screen with which the IP-address of the server is asked from the
 * user.
 */
public class ChangeIpDialog extends HestiaDialog {
    private final static String TAG = "ChangeIpDialog";
    private String ip;
    private EditText ipField;

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

        ipField = (EditText) view.findViewById(R.id.ip);
        ipField.setRawInputType(Configuration.KEYBOARD_12KEY);

        String currentIP = serverCollectionsInteractor.getHandler().getIp();
        if (currentIP == null) {
            ipField.setHint("Enter ip here");
        } else {
            ipField.setText(currentIP);
        }

        return view;
    }

    /**
     * After pressing confirm the entered ip is checked and, if it is a valid address,
     * sent to the handler. A text message is displayed to the user showing the new IP-address.
     */
    @Override
    void pressConfirm() {
        ip = ipField.getText().toString();
        Log.i(TAG, "My ip before changing is:" + ip);
        if(ip!=null) {
            serverCollectionsInteractor.getHandler().setIp(ip);
            Log.i(TAG, "My ip was changed to: " + ip);
            Toast.makeText(getContext(), serverCollectionsInteractor.getHandler()
                            .getIp(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Pressing cancel on the dialog simply displays a text message.
     */
    @Override
    void pressCancel() {
        Toast.makeText(getContext(), getResources().getText(R.string.cancelIPChange),
                Toast.LENGTH_SHORT).show();
    }
}
