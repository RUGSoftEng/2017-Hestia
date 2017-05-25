package hestia.UI.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import com.rugged.application.hestia.R;
import hestia.backend.Cache;

/**
 * This class represents the dialog screen with which the IP-address of the server is asked from the
 * user.
 */

public class IpDialog extends HestiaDialog {
    private EditText ipField;
    private String ip;
    private Cache cache;

    public IpDialog(Activity activity, Cache cache) {
        super(activity, R.layout.ip_dialog, "Set IP");
        this.cache = cache;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ipField = (EditText) findViewById(R.id.ip);
        ipField.setText(this.cache.getHandler().getIp());
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    void pressCancel() {}

    @Override
    void pressConfirm() {
        ip = ipField.getText().toString();
        if(ip!=null) {
            cache.getHandler().setIp(ip);
            // TODO refresh layout
            Toast.makeText(getContext(),R.string.ipSetTo + cache.getHandler().getIp() + ":"
                    + cache.getHandler().getPort(),Toast.LENGTH_SHORT).show();

            //TODO give correct response from server after changing ip
            Toast.makeText(getContext(), "Server returned message: + serverMessage",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
