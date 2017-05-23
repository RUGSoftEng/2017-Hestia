package hestia.UI;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    public AddDeviceDialog(Context context) {
        super(context, R.layout.add_device_dialog, "Add a device");
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
    }

    @Override
    void pressConfirm() {
        String collection = collectionField.getText().toString();
        String pluginName = pluginField.getText().toString();
//        networkHandler.addDevice(collection, pluginName, context);
//                //TODO give correct response from server after adding device
//                Toast.makeText(context, "Added device x. Server should let us what response " +
//                                "it gave", Toast.LENGTH_SHORT).show();
    }


}
