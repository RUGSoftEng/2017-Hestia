package hestia.UI.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
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
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set Dialog Title
        builder.setTitle("Change name")

                // Positive button
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something else
                        pressConfirm();
                        dismiss();

                    }
                })

                // Negative Button
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something else
                        pressCancel();

                    }

                });
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.set_name, null);


        editText = (EditText) view.findViewById(R.id.change_name_device);
        editText.setText(device.getName());
        builder.setView(view);

        AlertDialog dlg = builder.create();
        dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return dlg;
    }

    @Override
    View buildView() {
        return null;
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
