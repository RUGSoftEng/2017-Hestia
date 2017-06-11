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

public class ChangeNameDialog extends HestiaDialog {
    private EditText editText;
    private Device device;
    private final String TAG = "ChangeNameDialog";

    public static ChangeNameDialog newInstance() {
        ChangeNameDialog fragment = new ChangeNameDialog();
        return fragment;
    }

    public void setDevice(Device d) {
        device = d;
    }

    @Override
    String buildTitle() {
        return getString(R.string.changeNameTitle);
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
                    String exceptionMessage = getString(R.string.serverNotFound);
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
