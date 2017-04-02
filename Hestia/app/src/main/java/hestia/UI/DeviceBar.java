package hestia.UI;


import android.view.View;
import android.widget.Switch;

import hestia.UIWidgets.HestiaSwitch;
import hestia.backend.Device;

public class DeviceBar {
    //TODO Add ImageView
    private Device d;
    private HestiaSwitch hestiaSwitch;

    public DeviceBar(Device d, HestiaSwitch hestiaSwitch) {
        this.d = d;
        this.hestiaSwitch = hestiaSwitch;
    }

    public Device getDevice() {
        return d;
    }

    public void setDevice(Device d) {
        this.d = d;
    }

    public HestiaSwitch getHestiaSwitch() {
        return hestiaSwitch;
    }

    public void setHestiaSwitch(HestiaSwitch hestiaSwitch) {
        this.hestiaSwitch = hestiaSwitch;
    }

    public void setLayout(View v, int layoutId) {
        hestiaSwitch.setActivatorSwitch((Switch)v.findViewById(layoutId));
    }





}
