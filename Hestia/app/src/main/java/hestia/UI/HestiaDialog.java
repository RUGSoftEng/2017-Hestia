package hestia.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.rugged.application.hestia.R;

import hestia.backend.NetworkHandler;


abstract class HestiaDialog extends Dialog implements android.view.View.OnClickListener{
    protected Context context;
    private int layoutReference;

    private String title;
    private Button cancel;
    private Button confirm;

    public HestiaDialog(Context context, int reference, String title) {
        super(context);
        this.context = context;
        this.layoutReference = reference;
        this.title = title;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hestia_dialog);
        TextView header = (TextView)findViewById(R.id.dialog_header);
        header.setText(title);
        cancel = (Button)findViewById(R.id.cancel_button);
        cancel.setOnClickListener(this);
        confirm = (Button)findViewById(R.id.confirm_button);
        confirm.setOnClickListener(this);
        LayoutInflater inflater = getLayoutInflater();
        FrameLayout frame = (FrameLayout)findViewById(R.id.dialog_container);
        View v = inflater.inflate(layoutReference, null);
        frame.addView(v);
    }

    abstract void pressCancel();

    abstract void pressConfirm();

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_button:
                pressCancel();
                dismiss();
                break;
            case R.id.confirm_button:
                pressConfirm();
                dismiss();
                break;
        }
    }

}
