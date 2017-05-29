package hestia.UI.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.util.Log;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rugged.application.hestia.R;

import java.io.IOException;
import java.util.HashMap;

import hestia.backend.ServerCollectionsInteractor;
import hestia.backend.exceptions.ComFaultException;
import hestia.backend.models.RequiredInfo;

/**
 * This class dynamically creates the fields for the required information.
 * It receives as arguments an activity and a HashMap<String,String> and then adds
 * the text and the fields from the HashMap keys, with the values as values.
 * Finally it sends back the HashMap to the backendInteractor which posts it to the server.
 */

public class EnterRequiredInfoDialog extends HestiaDialog {
    private RequiredInfo info;
    private ServerCollectionsInteractor serverCollectionsInteractor;
    private final String TAG = "EnterRequiredInfoDialog";
    private final String fixedFieldCol = "collection";
    private final String fixedFieldPlugin = "plugin";
    private final String propReqInfo = "required_info";
    private static final String EMPTY_STRING="";
    private View view;

    public static EnterRequiredInfoDialog newInstance() {
        EnterRequiredInfoDialog fragment = new EnterRequiredInfoDialog();
        return fragment;
    }

    public void setData(RequiredInfo info, ServerCollectionsInteractor serverCollectionsInteractor) {
        this.info = info;
        this.serverCollectionsInteractor = serverCollectionsInteractor;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int count = 0;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set Dialog Title
        builder.setTitle("Adding " + info.getPlugin() + " from " + info.getCollection())

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
                    public void onClick(DialogInterface dialog,	int which) {
                        // Do something else
                    }

                });

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        view = inflater.inflate(R.layout.enter_device_info, null);

        final LinearLayout mainLayout = (LinearLayout) view.findViewById(R.id.linearMain);
        mainLayout.setLayoutParams(params);
        float scale = getResources().getDisplayMetrics().density;
        int padding = 8;
        int dpAsPixels = (int) (padding*scale + 0.5f);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(0, dpAsPixels, 0, 0);

        final HashMap<String, String> fields = info.getInfo();

        LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (final String key : fields.keySet()) {
            EditText field = createEditText(key, editParams , count, fields);
            if (field.getId() == 0) {
                field.requestFocus();

            }
            mainLayout.addView(field);

            count++;
        }

        builder.setView(view);

        AlertDialog dlg = builder.create();
        dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return dlg;
    }

    private EditText createEditText(final String key, LinearLayout.LayoutParams params, int count,
                                    final HashMap<String, String> fields) {
        final EditText field = new EditText(getActivity());
        field.setId(count);
        field.setHint(key);
        field.setInputType(InputType.TYPE_CLASS_TEXT);
        field.setMaxLines(1);
        field.setFilters(new InputFilter[] {new InputFilter.LengthFilter(15)});
        field.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);


        field.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(fields.get(key));
                AlertDialog dialog = builder.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

                wmlp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;

                dialog.show();
                return true;
            }
        });
        if (key.equals(fixedFieldCol)||key.equals(fixedFieldPlugin)) {
            field.setFocusable(false);
            field.setClickable(false);
        }
        field.setLayoutParams(params);


        return field;
    }

    @Override
    void pressCancel() {
        Toast.makeText(getActivity(), R.string.cancel, Toast.LENGTH_SHORT).show();
    }

    @Override
    void pressConfirm() {
        new AsyncTask<Object, String, Integer>() {
            @Override
            protected Integer doInBackground(Object... params) {
                updateRequiredInfo(view);
                try {
                    serverCollectionsInteractor.addDevice(info);
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
                return 0;
            }

            @Override
            protected void onProgressUpdate(String... exceptionMessage) {
                Toast.makeText(getContext(), exceptionMessage[0], Toast.LENGTH_SHORT).show();
            }

        }.execute();

    }

    /**
     * Updates the required info with the entered information
     */
    private void updateRequiredInfo(View v) {
        int count = 0;
        for(String key : this.info.getInfo().keySet()) {
            EditText field = (EditText) v.findViewById(count);
            String valueField = field.getText().toString();

            this.info.getInfo().put(key, valueField);
            count++;
        }
    }

}