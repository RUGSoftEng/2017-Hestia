package hestia.UI.dialogs;

import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.rugged.application.hestia.R;

import java.io.IOException;

import hestia.backend.exceptions.ComFaultException;
import hestia.backend.models.Device;

/**
 * This dialog can be used for changing the name of a particular device.
 */
public class ChangeNameDialog extends HestiaDialog {
    private EditText editText;
    private Device device;
    private final String TAG = "ChangeNameDialog";

    public static ChangeNameDialog newInstance() {
        ChangeNameDialog fragment = new ChangeNameDialog();
        return fragment;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Override
    String buildTitle() {
        return getResources().getText(R.string.titleNameChange).toString();
    }

    @Override
    View buildView() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.set_name, null);
        editText = (EditText) view.findViewById(R.id.change_name_device);
        editText.setText(device.getName());

        return view;
    }

    @Override
    void pressCancel() {
        Toast.makeText(getContext(), getResources().getText(R.string.cancelNameChange),
                Toast.LENGTH_LONG).show();
    }

    /**
     * Upon confirming a new name, the server is contacted to set the name. This method does not
     * require any further action from the client.
     */
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
                } catch (ComFaultException comFaultException) {
                    Log.e(TAG, comFaultException.toString());
                    String error = comFaultException.getError();
                    String message = comFaultException.getMessage();
                    String exceptionMessage = error + ":" + message;
                    publishProgress(exceptionMessage);
                }
                return isSuccessful;
            }

            @Override
            protected void onProgressUpdate(String... exceptionMessage) {
                Toast.makeText(getContext(), exceptionMessage[0], Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }

}
