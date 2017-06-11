package com.rugged.application.hestia.backend;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import hestia.backend.NetworkHandler;
import static junit.framework.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class NetworkHandlerTest {
    private NetworkHandler dummyHandler;
    private final String dummyIP = "0.0.0.0";
    private final Integer dummyPort = 1000;

    @Before
    public void setUp() {
        assertNull(dummyHandler);
        dummyHandler = new NetworkHandler(dummyIP, dummyPort);
        assertNotNull(dummyHandler);
    }

    @Test
    public void packageNameTest(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.rugged.application.hestia", appContext.getPackageName());
    }

    @Test
    public void setAndGetIpTest() {
        assertEquals(dummyIP, dummyHandler.getIp());
        String newIP ="1.0.0.0";
        dummyHandler.setIp(newIP);
        assertEquals(newIP, dummyHandler.getIp());
    }

    @Test
    public void setAndGetPortTest() {
        assertEquals(dummyPort, dummyHandler.getPort());
        Integer newPort = 2000;
        dummyHandler.setPort(newPort);
        assertEquals(newPort, dummyHandler.getPort());
    }

    @Test
    public void isSuccessfulRequestTest() {
        Integer responseCodeSuccess = 200;
        Integer responseCodeFail = 404;
        Integer responseCodeNull = null;
        assertTrue(dummyHandler.isSuccessfulRequest(responseCodeSuccess));
        assertFalse(dummyHandler.isSuccessfulRequest(responseCodeFail));
        assertFalse(dummyHandler.isSuccessfulRequest(responseCodeNull));
    }

    @Test
    public void getDefaultPathTest() {
        String path1 = "https://" + dummyIP + ":" + dummyPort + "/";
        assertNotNull(dummyHandler.getDefaultPath());
        assertEquals(path1, dummyHandler.getDefaultPath());

        String IP = "1.1.1.1";
        Integer port = 2000;
        String path2 = "https://" + IP + ":" + port + "/";
        dummyHandler.setIp(IP);
        dummyHandler.setPort(port);
        assertNotNull(dummyHandler.getDefaultPath());
        assertEquals(path2, dummyHandler.getDefaultPath());
    }

    @Test
    public void equalsCurrentReferenceTest() {
        NetworkHandler handlerCurrent = this.dummyHandler;
        assertNotNull(handlerCurrent);
        assertTrue(dummyHandler.equals(handlerCurrent));
    }

    @Test
    public void equalsAndHashCodeSamePropertiesTest() {
        NetworkHandler handlerSameProperties = new NetworkHandler(dummyIP, dummyPort);
        assertNotNull(handlerSameProperties);
        assertTrue(dummyHandler.equals(handlerSameProperties));
        assertEquals(dummyHandler.hashCode(), handlerSameProperties.hashCode());
    }

    @Test
    public void equalsNullTest() {
        NetworkHandler handlerNull = null;
        assertNull(handlerNull);
        assertFalse(dummyHandler.equals(handlerNull));
    }

    @Test
    public void equalsAndHashCodeDifferentPropertiesTest() {
        String IP = "1.1.1.1";
        Integer port = 2000;
        NetworkHandler handlerDifferentProperties = new NetworkHandler(IP, port);
        assertNotNull(handlerDifferentProperties);
        assertFalse(dummyHandler.equals(handlerDifferentProperties));
        assertNotSame(dummyHandler.hashCode(), handlerDifferentProperties.hashCode());
    }

    @After
    public void tearDown() {
        assertNotNull(dummyHandler);
        dummyHandler = null;
        assertNull(dummyHandler);
    }
}
