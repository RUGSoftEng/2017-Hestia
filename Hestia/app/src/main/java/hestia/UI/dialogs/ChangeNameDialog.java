package hestia.UI.dialogs;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.rugged.application.hestia.R;
import hestia.backend.Device;

public class ChangeNameDialog extends HestiaDialog {
    private EditText editText;
    private Device device;

    public ChangeNameDialog(Context context, Device device) {
        super(context, R.layout.set_name, "Change name of your device");
        this.device = device;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editText = (EditText)findViewById(R.id.change_name_device);
        editText.setText(device.getName());
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    void pressCancel() {
        Toast.makeText(context, "pressed cancel", Toast.LENGTH_SHORT).show();
    }

    @Override
    void pressConfirm() {
        final String result = editText.getText().toString();
        new AsyncTask<Object, Object, Integer>() {
            @Override
            protected Integer doInBackground(Object... params) {
                device.setName(result);
                return 0;
            }

            @Override
            protected void onPostExecute(Integer integer) {
                //UPDATE THE GUI
            }
        }.execute();
    }
}
