package hestia.UI;

import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import hestia.backend.Device;

/**
 *  This class takes care of the deviceBar.
 * The devicebar is the 'row' in the expandable list of a single device.
 * The DeviceBar class also sets the HestiaSwitch for the boolean activator.
 */

public class DeviceBar {
    private Device device;
    private HestiaSwitch hestiaSwitch;

    private final static String TAG = "DeviceBar";

    public DeviceBar(Device device, HestiaSwitch hestiaSwitch) {
        this.device = device;
        this.hestiaSwitch = hestiaSwitch;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public void setLayout(View view, int layoutId, boolean state) {
        hestiaSwitch.addLayout(view, layoutId);
        hestiaSwitch.getActivatorSwitch().setOnCheckedChangeListener(null);
        hestiaSwitch.getActivatorSwitch().setChecked(state);
        hestiaSwitch.getActivatorSwitch().setOnCheckedChangeListener(hestiaSwitch);

    }

    @Override
    public boolean equals(Object object) {
        boolean equal = false;

        if (object != null && object instanceof DeviceBar) {
            DeviceBar bar = (DeviceBar) object;
            if(this.device.getId().equals (bar.getDevice().getId())){
                equal = true;
            }
        }
        return equal;
    }

}
