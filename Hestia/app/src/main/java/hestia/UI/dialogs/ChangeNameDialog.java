package hestia.UI.dialogs;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.rugged.application.hestia.R;
import java.io.IOException;

import hestia.backend.exceptions.ServerExceptions.DeviceNotFoundException;
import hestia.backend.models.Device;

public class ChangeNameDialog extends HestiaDialog {
    private EditText editText;
    private Device device;
    private final String TAG = "ChangeNameDialog";

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
    void pressConfirm() {
        final String result = editText.getText().toString();
        new AsyncTask<Object, String, Boolean>() {
            @Override
            protected Boolean doInBackground(Object... params) {
                Boolean isSuccessful = false;
                try {
                    device.setName(result);
                    isSuccessful = true;
                } catch (IOException e) {
                  
                    Log.e(TAG,e.toString());
                    String exceptionMessage = "Could not connect to the server";
                    publishProgress(exceptionMessage);
                    e.printStackTrace();
                } catch (DeviceNotFoundException e) {
                    Log.d("comFault",e.getError()+":" +e.getMessage());
                    e.printStackTrace();
                }
                return isSuccessful;
            }

            @Override
            protected void onProgressUpdate(String... exceptionMessage) {
                Toast.makeText(context, exceptionMessage[0], Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void onPostExecute(Boolean isSuccessful) {
                if(isSuccessful) {
                    //UPDATE THE GUI
                }
            }
        }.execute();
    }
}
