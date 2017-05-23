package com.rugged.application.hestia.backendTests;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import hestia.backend.Activator;
import hestia.backend.ActivatorState;
import hestia.backend.Device;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DeviceTest {
    private Device deviceTest;
    private ArrayList<Activator> activators;
    private final String DEFAULT_DEVICE_ID = "12";
    private final String DEFAULT_DEVICE_NAME = "deviceTest";
    private final String DEFAULT_DEVICE_TYPE = "deviceType";

    @Before
    public void createDevice(){
        ActivatorState<Boolean> testState = new ActivatorState<>(false,"bool");
        Activator testButton = new Activator("0",0,testState,"testButton");
        activators = new ArrayList<>();
        activators.add(testButton);

        deviceTest = new Device(DEFAULT_DEVICE_ID,DEFAULT_DEVICE_NAME,DEFAULT_DEVICE_TYPE,activators);
    }

    @Test
    public void setAndGetIdTest() {
        assertEquals(DEFAULT_DEVICE_ID,deviceTest.getId());
        deviceTest.setId("2");
        assertEquals("2",deviceTest.getId());
    }

    @Test
    public void setAndGetNameTest() {
        assertEquals(DEFAULT_DEVICE_NAME,deviceTest.getName());
        deviceTest.setName("hestiaDevice");
        assertEquals("hestiaDevice",deviceTest.getName());
    }

    @Test
    public void setAndGetTypeTest() {
        assertEquals(DEFAULT_DEVICE_TYPE,deviceTest.getType());
        deviceTest.setType("T_DEV");
        assertEquals("T_DEV", deviceTest.getType());
    }

    @Test
    public void setAndGetActivatorsTest() {
        assertFalse(deviceTest.getActivators().isEmpty());
        deviceTest.getActivators().clear();
        assertTrue(deviceTest.getActivators().isEmpty());

        ActivatorState<Float> testSliderState = new ActivatorState<>((float) 0.3,"float");
        Activator testSlider = new Activator("0",0,testSliderState,"SLIDER");
        ArrayList<Activator> newActivatorsList = new ArrayList<>();
        newActivatorsList.add(testSlider);
        deviceTest.setActivators(newActivatorsList);
        assertFalse(deviceTest.getActivators().isEmpty());
        assertTrue(deviceTest.getActivators().contains(testSlider));
    }

    @Test
    public void getToggleTest() {
        Activator currentToggle = deviceTest.getToggle();
        assertNotNull(currentToggle);
        assertEquals(0, (long) currentToggle.getRank());

        // clear the list of activators and create a new toggle
        deviceTest.getActivators().clear();
        ActivatorState<Boolean> newToggleState = new ActivatorState<>(false, "bool");
        Activator newToggle = new Activator("0",0,newToggleState, "newToggleActivator");
        ArrayList<Activator> newActivators = new ArrayList<>();
        newActivators.add(newToggle);
        deviceTest.setActivators(newActivators);

        assertNotNull(deviceTest.getToggle());
        assertEquals(0, (long) deviceTest.getToggle().getRank());
        assertEquals(newToggle, deviceTest.getToggle());
    }


    @Test
    public void getSlidersTest() {
        assertTrue(deviceTest.getSliders().isEmpty());

        ActivatorState<Float> testSliderState = new ActivatorState<>((float) 0.3,"float");
        Activator testSlider = new Activator("0",0,testSliderState,"SLIDER");
        ArrayList<Activator> newActivatorsList = new ArrayList<>();
        newActivatorsList.add(testSlider);
        deviceTest.setActivators(newActivatorsList);
        assertFalse(deviceTest.getSliders().isEmpty());
        assertTrue(deviceTest.getSliders().contains(testSlider));
    }

    @Test
    public void toStringTest() {
        String TO_STRING = DEFAULT_DEVICE_NAME +" "+ DEFAULT_DEVICE_ID + " " + activators.toString() + "\n";
        assertEquals(TO_STRING, deviceTest.toString());
    }

    @Test
    public void equalsTest() {
        // create a new Device object with the same data as the deviceTest object
        Device dummyDevice = new Device(DEFAULT_DEVICE_ID, DEFAULT_DEVICE_NAME, DEFAULT_DEVICE_TYPE,activators);
        assertTrue(dummyDevice.equals(deviceTest));
        assertEquals(dummyDevice, deviceTest);
    }
}
