package hestia.UI.dialogs;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.rugged.application.hestia.R;

import java.io.IOException;
import java.util.HashMap;

import hestia.UI.activities.home.HomeActivity;
import hestia.backend.Cache;
import hestia.backend.models.RequiredInfo;

/**
 * This class dynamically creates the fields for the required information.
 * It receives as arguments an activity and a HashMap<String,String> and then adds
 * the text and the fields from the HashMap keys, with the values as values.
 * Finally it sends back the HashMap to the backendInteractor which posts it to the server.
 */

public class AddDeviceInfo extends HestiaDialog {
    private RequiredInfo info;
    private Cache cache;
    private final String TAG = "AddDeviceInfo";
    private final String fixedFieldCol = "collection";
    private final String fixedFieldPlugin = "plugin";
    private final String propReqInfo = "required_info";
    private static final String EMPTY_STRING="";


    public AddDeviceInfo(Context context, RequiredInfo info, Cache cache) {
        super(context, R.layout.enter_device_info, "Add a device");
        this.info = info;
        this.cache = cache;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        final LinearLayout mainLayout = (LinearLayout) findViewById(R.id.linearMain);
        int count = 0;

        this.setTitle("Adding " + info.getPlugin() + " from " + info.getCollection());

        HashMap<String, String> fields = info.getInfo();

        for (String key : fields.keySet()) {
            LinearLayout subLayout = new LinearLayout(context);

            // Add text
            TextView name = new TextView(context);
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
        final EditText field = new EditText(context);
        field.setText(info.getInfo().get(key));
        field.setId(count);
        if (key.equals(fixedFieldCol)||key.equals(fixedFieldPlugin)) {
            field.setFocusable(false);
            field.setClickable(false);
        }
        field.setLayoutParams(params);
        field.setWidth(800);
        return field;
    }

    @Override
    void pressCancel() {
        Toast.makeText(context, R.string.cancel, Toast.LENGTH_SHORT).show();
    }

    @Override
    void pressConfirm() {
        new AsyncTask<Object, Object, Integer>() {
            @Override
            protected Integer doInBackground(Object... params) {
                updateRequiredInfo();
                try {
                    cache.addDevice(info);
                } catch (IOException e) {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                return 0;
            }

            @Override
            protected void onPostExecute(Integer returnValue) {
                // TODO: find a way to update the GUI from here
            }
        }.execute();

    }


    /**
     * Updates the required info with the entered information
     */
    private void updateRequiredInfo() {
        int count = 0;
        for(String key : this.info.getInfo().keySet()) {
            EditText field = (EditText) findViewById(count);
            String valueField = field.getText().toString();

            this.info.getInfo().put(key, valueField);
            count++;
        }
    }

}