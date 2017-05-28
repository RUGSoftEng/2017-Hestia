package hestia.UI.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import com.rugged.application.hestia.R;
import hestia.backend.ServerCollectionsInteractor;

/**
 * This class represents the dialog screen with which the IP-address of the server is asked from the
 * user.
 */

public class IpDialog extends HestiaDialog2  {
    private final static String TAG = "IpDialog";
    private String ip;
    private EditText ipField;

    private ServerCollectionsInteractor serverCollectionsInteractor;

    public static IpDialog newInstance(String ip, ServerCollectionsInteractor serverCollectionsInteractor) {
        IpDialog fragment = new IpDialog();
        Bundle bundle = new Bundle();
        bundle.putString("IP_ADDRESS", ip);
        bundle.putSerializable("INTERACTOR", serverCollectionsInteractor);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            ip = savedInstanceState.getString("IP_ADDRESS");
            serverCollectionsInteractor = (ServerCollectionsInteractor)savedInstanceState
                    .getSerializable("INTERACTOR");

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set Dialog Title
        builder.setTitle("Change IP")

                // Positive button
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something else
                    }
                })

                // Negative Button
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,	int which) {
                        // Do something else
                    }

                });
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.ip_dialog, null);

        ipField = (EditText) view.findViewById(R.id.ip);
//        ipField.setInputType(InputType.TYPE_CLASS_NUMBER);
//        ipField.setRawInputType(Configuration.KEYBOARD_12KEY);

        if (ip != null) {
            ipField.setText(ip);
        }
        builder.setView(view);

        AlertDialog dlg = builder.create();
        dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return dlg;
    }


//    public IpDialog(Activity activity, ServerCollectionsInteractor serverCollectionsInteractor) {
//        this.activity = activity;
//        super(activity, R.layout.ip_dialog, "Set IP");
//        this.serverCollectionsInteractor = serverCollectionsInteractor;
//        ipField = (EditText) findViewById(R.id.ip);
//        ipField.setInputType(InputType.TYPE_CLASS_NUMBER);
//        ipField.setRawInputType(Configuration.KEYBOARD_12KEY);
//        this.setView(ipField);
//        if (this.serverCollectionsInteractor.getHandler().getIp() != null) {
//            ipField.setText(this.serverCollectionsInteractor.getHandler().getIp());
//        }
//    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return new AlertDialog.Builder(activity)
//                .setTitle("App")
//                .setPositiveButton(new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//
//                    }
//                })
//                .setNegativeButton()
//                .create();
//    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        Log.i(TAG, "I am here");
//        ipField = (EditText) findViewById(R.id.ip);
//        ipField.setInputType(InputType.TYPE_CLASS_NUMBER);
//        ipField.setRawInputType(Configuration.KEYBOARD_12KEY);
//        this.setView(ipField);
//        if (this.serverCollectionsInteractor.getHandler().getIp() != null) {
//            ipField.setText(this.serverCollectionsInteractor.getHandler().getIp());
//        }
////        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//    }

    @Override
    void pressCancel() {}

    @Override
    void pressConfirm() {
        ip = ipField.getText().toString();
        Log.i(TAG, "My ip is now:" + ip);
        if(ip!=null) {
            serverCollectionsInteractor.getHandler().setIp(ip);
//             TODO refresh layout
            Toast.makeText(getContext(),R.string.ipSetTo + serverCollectionsInteractor.getHandler()
                    .getIp() + ":" + serverCollectionsInteractor.getHandler().getPort(),
                    Toast.LENGTH_SHORT).show();

            //TODO give correct response from server after changing ip
            Toast.makeText(getContext(), "Server returned message: + serverMessage",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
