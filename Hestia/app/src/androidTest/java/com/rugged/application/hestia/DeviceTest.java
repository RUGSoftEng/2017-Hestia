package com.rugged.application.hestia;

import android.bluetooth.BluetoothClass;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import hestia.backend.Activator;
import hestia.backend.ActivatorState;
import hestia.backend.Device;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class DeviceTest {
    private Device tDev;

    private final int DEFAULT_DEV_ID = 12;


    @Before
    public void createDevice(){
        ActivatorState<Boolean> testState = new ActivatorState<>(false,"TOGGLE");
        Activator testButton = new Activator(0,testState,"testButton");
        ArrayList<Activator> arr = new ArrayList<>();
        arr.add(testButton);

        tDev = new Device(DEFAULT_DEV_ID,"testDevice","HESTIA_DEVICE",arr);
    }


    @Test
    public void getterAndSetterTest(){
        assertEquals(DEFAULT_DEV_ID,tDev.getDeviceId());
        tDev.setDeviceId(2);
        assertEquals(2,tDev.getDeviceId());

        assertEquals("testDevice",tDev.getName());
        tDev.setName("hestiaDevice");
        assertEquals("hestiaDevice",tDev.getName());

        assertEquals("HESTIA_DEVICE",tDev.getType());
        tDev.setType("T_DEV");
        assertEquals("T_DEV", tDev.getType());

        assertTrue(tDev.getSliders() == null);
        ActivatorState<Float> testSliderState = new ActivatorState<>((float) 0.3,"SLIDER");
        ArrayList<Activator> arr = new ArrayList<>();
        Activator testSlider = new Activator(0,testSliderState,"newButton");
        arr.add(testSlider);
        tDev.setActivators(arr);
        assertFalse(tDev.getSliders() == null);

    }

    @Test
    public void equalsAndHashTest(){
        assertTrue(tDev.equals(tDev));
        assertFalse(tDev.equals(0));
        assertFalse(tDev.equals(null));

        Device dev2 = new Device(DEFAULT_DEV_ID,"testDevice","HESTIA_DEVICE", new ArrayList<Activator>());

        assertNotEquals(tDev.hashCode(),dev2.hashCode());
    }

}
