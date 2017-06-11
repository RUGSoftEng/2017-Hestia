package hestia.UI.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.WindowManager;

import hestia.UI.activities.home.HomeActivity;
import com.rugged.application.hestia.R;

abstract class HestiaDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(buildTitle());
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        pressConfirm();
                        dismiss();

                    }
                });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,	int which) {
                        pressCancel();
                        dismiss();
                    }
                });
        builder.setView(buildView());
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    protected void refreshUserInterface(){
        ((HomeActivity)this.getActivity()).refreshUserInterface();
    }

    abstract String buildTitle();
    abstract View buildView();
    abstract void pressConfirm();
    abstract void pressCancel();
}
