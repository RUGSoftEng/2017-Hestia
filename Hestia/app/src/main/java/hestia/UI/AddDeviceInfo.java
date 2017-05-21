package hestia.UI;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.rugged.application.hestia.R;
import java.util.HashMap;

import hestia.backend.NetworkHandler;

/**
 * This class dynamically creates the fields for the required information.
 * It receives as arguments an activity and a HashMap<String,String> and then adds
 * the text and the fields from the HashMap keys, with the values as values.
 * Finally it sends back the HashMap to the backendInteractor which posts it to the server.
 */

public class AddDeviceInfo extends HestiaDialog {
    private HashMap<String, String> fields;
    private Activity content;
    private final String TAG = "AddDeviceInfo";
    private final String fixedFieldOrg = "organization";
    private final String fixedFieldPlugin = "plugin";
    private final String propReqInfo = "required_info";
    private static final String EMPTY_STRING="";

    public AddDeviceInfo(Activity activity, HashMap<String, String> fields) {
        super(activity, R.layout.enter_device_info, "Add a device");
        this.content = activity;
        this.fields = fields;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        final LinearLayout mainLayout = (LinearLayout) findViewById(R.id.linearMain);
        int count = 0;

        for (String key : fields.keySet()) {
            LinearLayout subLayout = new LinearLayout(content);

            // Add text
            TextView name = new TextView(content);
            name.setText(key);
            subLayout.addView(name);

            //Add field
            EditText field = createEditText(key, params , count);
            field.requestFocus();
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            subLayout.addView(field);

            mainLayout.addView(subLayout);
            count++;
        }
    }

    private EditText createEditText(String key, LinearLayout.LayoutParams params, int count) {
        final EditText field = new EditText(content);
        field.setText(fields.get(key));
        field.setId(count);
        if (key.equals(fixedFieldOrg)||key.equals(fixedFieldPlugin)) {
            field.setFocusable(false);
            field.setClickable(false);
        }
        field.setLayoutParams(params);
        field.setWidth(800);
        return field;
    }

    @Override
    void pressCancel() {
        Toast.makeText(content, R.string.cancel, Toast.LENGTH_SHORT).show();
        dismiss();
    }

    @Override
    void pressConfirm() {
        JsonObject requiredInfo = this.getRequiredInfo();
        if(requiredInfo==null) {
            Toast.makeText(getContext(), R.string.emptyValuesEntered,
                    Toast.LENGTH_SHORT).show();
            return;
        }
        NetworkHandler.getInstance().postDevice(requiredInfo);
        dismiss();
    }

    /**
     * Creates a JSON object containing the relevant information in
     * the "required_information" key. If any field is left empty, it will return a null.
     * @return addDeviceJSON json object containing the information needed for adding a new device.
     */
    private JsonObject getRequiredInfo() {
        int count = 0;
        JsonObject requiredInfo = new JsonObject();
        for(String key : this.fields.keySet()) {
            EditText field = (EditText) findViewById(count);
            String valueField = field.getText().toString();

            if(EMPTY_STRING.equals(valueField)) {
                return null;
            }
            this.fields.put(key, valueField);
            String value = this.fields.get(key);
            requiredInfo.addProperty(key, value);
            count++;
        }
        JsonObject addDeviceJSON = new JsonObject();
        addDeviceJSON.add(propReqInfo, requiredInfo);
        return addDeviceJSON;
    }

}