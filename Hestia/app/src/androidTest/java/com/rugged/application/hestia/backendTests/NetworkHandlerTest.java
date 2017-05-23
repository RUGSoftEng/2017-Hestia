package com.rugged.application.hestia.backendTests;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.JsonPrimitive;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import hestia.backend.Activator;
import hestia.backend.ActivatorState;
import hestia.backend.Cache;
import hestia.backend.Device;
import hestia.backend.NetworkHandler;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class NetworkHandlerTest {
    private NetworkHandler networkHandler;

    @Before
    public void setUp() {
        this.networkHandler = NetworkHandler.getInstance();
    }

    @Test
    public void packageNameTest(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.rugged.application.hestia", appContext.getPackageName());
    }

    @Test
    public void singletonTest(){
        NetworkHandler copyOfHandler = NetworkHandler.getInstance();
        assertEquals(networkHandler,copyOfHandler);
    }

    @Test
    public void getPathTest() {
        Cache cache = Cache.getInstance();

        // dummy IP address and port number
        String IP = "0.0.0.0";
        Integer port = 1000;
        cache.setIp(IP);
        cache.setPort(port);

        // expected path
        String path = "http://" + IP + ":" + port + "/";
        assertEquals(networkHandler.getPath(), path);
    }

    @Test
    public void getNewStateValueTest() {
        Boolean DUMMY_BOOL = true;
        Float DUMMY_FLOAT = (float) 0.5;
        String DUMMY_STRING = "dummy";
        ActivatorState<Boolean> stateBool = new ActivatorState<>(DUMMY_BOOL, "bool");
        ActivatorState<Float> stateFloat = new ActivatorState<>(DUMMY_FLOAT, "float");
        ActivatorState<String> stateString = new ActivatorState<>(DUMMY_STRING, "dummyType");

        JsonPrimitive jsonBool = networkHandler.getNewStateValue(stateBool);
        JsonPrimitive jsonFloat = networkHandler.getNewStateValue(stateFloat);
        JsonPrimitive jsonString = networkHandler.getNewStateValue(stateString);

        assertEquals(jsonBool.getAsBoolean(), DUMMY_BOOL);
        assertEquals(jsonFloat.getAsFloat(), DUMMY_FLOAT, 0.000001);
        assertEquals(jsonString.getAsString(), DUMMY_STRING);
    }

}
