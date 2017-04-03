package hestia.UIWidgets;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import hestia.backend.Activator;
import hestia.backend.ActivatorState;
import hestia.backend.ClientInteractionController;
import hestia.backend.Device;

public class HestiaSwitch implements UIWidget {
    private final static String TAG = "HestiaSwitch";
    private Device d;
    private Activator a;
    private Switch activatorSwitch;
    private ClientInteractionController cic;

    public HestiaSwitch(Device d, Activator a, View v, int layoutId) {
        this.a = a;
//        activatorSwitch = createActivatorSwitch(c);
        activatorSwitch = (Switch)v.findViewById(layoutId);
        this.d = d;
        this.cic = ClientInteractionController.getInstance();
    }

    @Override
    public Device getDevice() {
        return d;
    }

    @Override
    public void setActivator(Activator a) {
        this.a = a;
    }

    @Override
    public Activator getActivator() {
        return a;
    }

    public Switch getActivatorSwitch() {
        return activatorSwitch;
    }

//    public void setActivatorSwitch(Switch s) {
//        s.setOnCheckedChangeListener(this);
//        s.setChecked(activatorSwitch.isChecked());
//        this.activatorSwitch = s;
//        Log.i(TAG, "Changed switch");
//    }

//    private Switch createActivatorSwitch(Context c) {
//        Switch s = new Switch(c);
//        Log.i(TAG, "the switch is being created");
//
//        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//        return s;
//    }
//
//    @Override
//    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//        Log.i(TAG, "I am being clicked");
//        int activatorId = a.getId();
//
//        ActivatorState state = a.getState();
//        activatorSwitch.setChecked(b);
//        if (b) {
//            // True
//            state.setState(true);
//            cic.setActivatorState(d, activatorId, state);
//            Log.i(TAG, "I Am being set to true");
//        } else {
//            // False
//            state.setState(false);
//            cic.setActivatorState(d, activatorId, state);
//            Log.i(TAG, "I Am being set to false");
//        }
//    }

}
