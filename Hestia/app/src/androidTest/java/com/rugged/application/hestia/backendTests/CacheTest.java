package com.rugged.application.hestia.backendTests;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import hestia.UI.DeviceListFragment;
import hestia.backend.Activator;
import hestia.backend.Cache;
import hestia.backend.Device;
import hestia.backend.DevicesChangeListener;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class CacheTest {
    private Cache cache;
    private Device dummyDevice;
    private String DUMMY_IP;
    private Integer DUMMY_PORT;

    @Before
    public void setUp() {
        cache = Cache.getInstance();
        dummyDevice = new Device("dummyId","dummyName","dummyType",new ArrayList<Activator>());
        DUMMY_IP = "0.0.0.0";
        DUMMY_PORT = 1000;
    }

    @Test
    public void packageNameTest(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.rugged.application.hestia", appContext.getPackageName());
    }

    @Test
    public void getDevicesTest(){
        assertNotNull(cache.getDevices());
        assertTrue(cache.getDevices().isEmpty());

        // adding 3 devices
        cache.getDevices().add(dummyDevice);
        cache.getDevices().add(dummyDevice);
        cache.getDevices().add(dummyDevice);
        assertTrue(cache.getDevices().contains(dummyDevice));
        assertEquals(cache.getDevices().size(), 3);
    }

    @Test
    public void singletonTest(){
        Cache copyOfCache = Cache.getInstance();
        assertEquals(cache,copyOfCache);
    }

    @Test
    public void setAndGetIpTest(){
        cache.setIp(DUMMY_IP);
        assertEquals(DUMMY_IP,cache.getIp());
    }

    @Test
    public void setAndGetPortTest() {
        cache.setPort(DUMMY_PORT);
        assertEquals((long) DUMMY_PORT, (long) cache.getPort());
    }

    @Test
    public void clearDevicesTest() {
        // empty list
        cache.getDevices().clear();
        assertTrue(cache.getDevices().isEmpty());
        assertEquals(cache.getDevices().size(), 0);

        // adding 3 devices
        cache.getDevices().add(dummyDevice);
        assertTrue(cache.getDevices().contains(dummyDevice));
        assertEquals(cache.getDevices().size(), 1);

        // clearing the list again
        cache.getDevices().clear();
        assertTrue(cache.getDevices().isEmpty());
        assertEquals(cache.getDevices().size(), 0);
    }

    @Test
    public void deleteDeviceTest(){
        cache.getDevices().add(dummyDevice);
        int lengthBefore = cache.getDevices().size();
        cache.getDevices().remove(dummyDevice);
        int lengthAfter = cache.getDevices().size();
        assertNotEquals(lengthBefore, lengthAfter);
    }

    @Test
    public void setDevicesTest(){
        ArrayList<Device> newDevices = new ArrayList<>();

        // Adding the same dummy device three times
        newDevices.add(dummyDevice);
        newDevices.add(dummyDevice);
        newDevices.add(dummyDevice);

        // setting new list of devices
        cache.setDevices(newDevices);
        assertEquals(3,cache.getDevices().size());
    }

    @Test
    public void listenerTest(){
        DevicesChangeListener listener = new DeviceListFragment();
        cache.getListeners().clear();
        assertTrue(cache.getListeners().isEmpty());

        // Testing adding a listener
        cache.addDevicesChangeListener(listener);
        assertEquals(listener,cache.getListeners().get(0));

        // Testing removing a listener
        cache.removeDevicesChangeListener(listener);
        assertEquals(0,cache.getListeners().size());
    }

    @After
    public void tearDown(){
        cache.getDevices().clear();
    }
}
