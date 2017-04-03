package hestia.UI;


import android.view.View;
import android.widget.Switch;

import hestia.UIWidgets.HestiaSwitch;
import hestia.backend.Activator;
import hestia.backend.Device;

public class DeviceBar {
    //TODO Add ImageView
    private Device d;
    private Activator a;
//    private HestiaSwitch hestiaSwitch;

    public DeviceBar(Device d, Activator a) {
        this.d = d;
        this.a = a;
//        this.hestiaSwitch = hestiaSwitch;
    }

    public Device getDevice() {
        return d;
    }

    public void setDevice(Device d) {
        this.d = d;
    }

//    public HestiaSwitch getHestiaSwitch() {
//        return hestiaSwitch;
//    }
//
//    public void setHestiaSwitch(HestiaSwitch hestiaSwitch) {
//        this.hestiaSwitch = hestiaSwitch;
//    }

//    public void setLayout(View v, int layoutId) {
//        hestiaSwitch.setActivatorSwitch((Switch)v.findViewById(layoutId));
//    }

    public Activator getA() {
        return a;
    }

    public void setA(Activator a) {
        this.a = a;
    }

    @Override
    public boolean equals(Object object)
    {
        boolean equal = false;

        if (object != null && object instanceof DeviceBar)
        {
            if(this.d.getDeviceId() == ((DeviceBar) object).getDevice().getDeviceId()){
                equal = true;
            }
        }
        return equal;
    }

}
