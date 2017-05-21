package hestia.UI;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;

import com.rugged.application.hestia.R;

import hestia.backend.NetworkHandler;

/**
* This class opens the dialog to enter the collection name and plugin name.
* It then sends this to the networkHandler which tries to get the required info.
* If this works it consecutively opens a new dialog for the other info.
* @see hestia.UI.AddDeviceInfo
 */

public class AddDeviceDialog extends HestiaDialog{
    private EditText collectionField, pluginField;
    private NetworkHandler networkHandler;
    private Activity context;

    public AddDeviceDialog(Activity activity) {
        super(activity, R.layout.add_device_dialog, "Add a device");
        this.context = activity;
        this.networkHandler = NetworkHandler.getInstance();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        collectionField = (EditText)findViewById(R.id.collection);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        pluginField = (EditText)findViewById(R.id.pluginName);
    }

    @Override
    void pressCancel() {
        Toast.makeText(context, "Cancel pressed", Toast.LENGTH_SHORT).show();
        dismiss();
    }

    @Override

    void pressConfirm() {
        String collection = collectionField.getText().toString();
        String pluginName = pluginField.getText().toString();
        networkHandler.addDevice(collection, pluginName, context);
//                //TODO give correct response from server after adding device
//                Toast.makeText(context, "Added device x. Server should let us what response " +
//                                "it gave", Toast.LENGTH_SHORT).show();
        dismiss();
    }

}
