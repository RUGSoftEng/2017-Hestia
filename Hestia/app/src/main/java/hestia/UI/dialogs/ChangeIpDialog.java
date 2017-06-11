package hestia.UI.dialogs;

import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.rugged.application.hestia.R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hestia.backend.ServerCollectionsInteractor;

/**
 * This class represents the dialog screen with which the IP-address of the server is asked from the
 * user.
 */

public class ChangeIpDialog extends HestiaDialog {
    private EditText ipField;
    private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                                                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                                                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                                                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

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
        return getString(R.string.changeIpTitle);
    }

    @Override
    View buildView() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.ip_dialog, null);

        ipField = (EditText) view.findViewById(R.id.ip);
        ipField.setRawInputType(Configuration.KEYBOARD_12KEY);

        String currentIP = serverCollectionsInteractor.getHandler().getIp();
        if (currentIP == null) {
            ipField.setHint(getString(R.string.setIpHint));
        } else {
            ipField.setText(currentIP);
        }
        return view;
    }

    @Override
    void pressConfirm() {
        String ip = ipField.getText().toString();
        if(checkIp(ip)) {
            serverCollectionsInteractor.getHandler().setIp(ip);
            Toast.makeText(getContext(), serverCollectionsInteractor.getHandler().getIp(),
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(),getString(R.string.incorr_ip),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    void pressCancel() {
    }

    private boolean checkIp(String ip) {
        Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }
}
