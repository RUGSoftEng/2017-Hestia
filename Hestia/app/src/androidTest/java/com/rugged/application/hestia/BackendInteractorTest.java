package com.rugged.application.hestia;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import hestia.UI.DeviceListFragment;
import hestia.backend.Activator;
import hestia.backend.ActivatorState;
import hestia.backend.BackendInteractor;
import hestia.backend.Device;
import hestia.backend.DevicesChangeListener;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class BackendInteractorTest {
    private static final String TEST_DEVICE_ID = "1";

    private static final String TEST_ACTIVATOR_ID = "0";
    private String TAG = "ClientInteractionTest";
    private static BackendInteractor backendInteractor;

    @BeforeClass
    public static void runBeforeTests(){
        backendInteractor = BackendInteractor.getInstance();
        ActivatorState<Boolean> testState = new ActivatorState<Boolean>(false,"TOGGLE");
        Activator testButton = new Activator("0",0,testState,"testButton");
        ArrayList<Activator> arr = new ArrayList<>();
        arr.add(testButton);
        Device testDevice = new Device("0","testDevice", "testing",arr);
        backendInteractor.addDevice(testDevice);

    }

    @Before
    public void addTestDevice(){
        ActivatorState<Boolean> testState = new ActivatorState<Boolean>(false,"TOGGLE");

        Activator testButton = new Activator(TEST_ACTIVATOR_ID,0,testState,"testButton");
        ArrayList<Activator> arr = new ArrayList<>();
        arr.add(testButton);
        Device testDevice = new Device(TEST_DEVICE_ID,"testDevice", "testing",arr);
        backendInteractor.addDevice(testDevice);
    }

    @After
    public void removeTestDevice(){
        backendInteractor.deleteTestDevice(Integer.parseInt(TEST_DEVICE_ID));
    }

    @Test
    public void packageNameTest(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.rugged.application.hestia", appContext.getPackageName());
    }

    @Test
    public void getDevicesTest(){
        StringBuilder sb = new StringBuilder();
        for(Device d : backendInteractor.getDevices()){
            sb.append(d.toString());
        }
        Log.i(TAG, sb.toString());
    }

    /*
     * Test of the singleton reference, two different references should refer to the same object.
     */

    @Test
    public void singletonTest(){
        BackendInteractor copyOfInteractor = BackendInteractor.getInstance();
        assertEquals(backendInteractor,copyOfInteractor);
    }


    @Test
    public void ipTest(){
        String testIp = "192.168.0.1";
        backendInteractor.setIp(testIp);
        assertEquals(testIp,backendInteractor.getIp());
    }

    @Test
    public void setActivatorStateTest(){
        ArrayList<Device> testDeviceList = backendInteractor.getDevices();
        Device testDevice = testDeviceList.get(Integer.parseInt(TEST_DEVICE_ID));
        ActivatorState state = testDevice.getActivators().get(Integer.parseInt(TEST_ACTIVATOR_ID)).getState();
        boolean testState = (boolean)state.getRawState();
        assertEquals(testState,false);
        state.setRawState(true);

        backendInteractor.setActivatorState(testDevice,testDevice.getActivators().get(Integer.parseInt(TEST_ACTIVATOR_ID)),state);

        Activator activator = backendInteractor.getDevices().get(Integer.parseInt(TEST_DEVICE_ID)).getActivators().get(Integer.parseInt(TEST_ACTIVATOR_ID));
        assertEquals(true,activator.getState().getRawState());
    }

    @Test
    public void deleteDeviceTest(){
        Device temp = backendInteractor.getDevices().get(Integer.parseInt(TEST_DEVICE_ID));

        // Removing a device
        backendInteractor.deleteDevice(temp);
        backendInteractor.clearDevices();

        // The list should be empty, updating should leave it empty
        backendInteractor.updateDevices();
        assertTrue(backendInteractor.getDevices().isEmpty());

        // Now adding a device, trying the same device twice
        backendInteractor.addDevice(temp);
        backendInteractor.addDevice(temp);
        assertEquals(2,backendInteractor.getDevices().size());

    }

    @Test
    public void setDevicesTest(){
        Device temp = backendInteractor.getDevices().get(Integer.parseInt(TEST_DEVICE_ID));
        ArrayList<Device> newDevices = new ArrayList<>();
        // Adding the same device three times
        newDevices.add(temp);
        newDevices.add(temp);
        newDevices.add(temp);

        backendInteractor.setDevices(newDevices);
        assertEquals(3,backendInteractor.getDevices().size());
    }

    @Test
    public void listenerTest(){
        DevicesChangeListener l = new DeviceListFragment();

        // Testing adding a listener
        backendInteractor.addDevicesChangeListener(l);
        assertEquals(l,backendInteractor.getListeners().get(0));

        // Testing removing a listener
        backendInteractor.removeDevicesChangeListener(l);
        assertEquals(0,backendInteractor.getListeners().size());
    }
}