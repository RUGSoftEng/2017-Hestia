package com.rugged.application.hestia.backend;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import hestia.backend.NetworkHandler;
import hestia.backend.ServerCollectionsInteractor;
import hestia.backend.models.Activator;
import hestia.backend.models.Device;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class ServerCollectionsInteractorTest {
    private ServerCollectionsInteractor dummyServerCollectionsInteractor;
    private Device dummyDevice;
    private String DUMMY_IP;
    private Integer DUMMY_PORT;
    private NetworkHandler dummyHandler;

    @Before
    public void setUp() {
        DUMMY_IP = "0.0.0.0";
        DUMMY_PORT = 1000;
        dummyHandler = new NetworkHandler(DUMMY_IP, DUMMY_PORT);
        dummyServerCollectionsInteractor = new ServerCollectionsInteractor(dummyHandler);
        dummyDevice = new Device("dummyId","dummyName","dummyType",new ArrayList<Activator>(), dummyHandler);
    }

    @Test
    public void packageNameTest(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.rugged.application.hestia", appContext.getPackageName());
    }

    @Test
    public void getDevicesTest(){
    }

    @Test
    public void addDeviceTest() {
    }

    @Test
    public void removeDeviceTest(){
    }

    @Test
    public void getCollectionsTest(){
    }

    @Test
    public void getPluginsTest(){
    }

    @Test
    public void getRequiredInfoTest(){
    }

    @Test
    public void setAndGetIpTest(){
    }

    @Test
    public void setAndGetPortTest() {
    }


}
