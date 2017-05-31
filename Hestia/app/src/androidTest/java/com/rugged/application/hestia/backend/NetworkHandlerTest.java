package com.rugged.application.hestia.backend;

import android.support.test.runner.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import hestia.backend.NetworkHandler;
import static junit.framework.Assert.*;

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
        String path1 = "http://" + dummyIP + ":" + dummyPort + "/";
        assertNotNull(dummyHandler.getDefaultPath());
        assertEquals(path1, dummyHandler.getDefaultPath());

        String IP = "1.1.1.1";
        Integer port = 2000;
        String path2 = "http://" + IP + ":" + port + "/";
        dummyHandler.setIp(IP);
        dummyHandler.setPort(port);
        assertNotNull(dummyHandler.getDefaultPath());
        assertEquals(path2, dummyHandler.getDefaultPath());
    }

    @Test
    public void equalsTest() {
        NetworkHandler handlerCurrent = this.dummyHandler;
        assertNotNull(handlerCurrent);
        assertTrue(dummyHandler.equals(handlerCurrent));

        NetworkHandler handlerSameProperties = new NetworkHandler(dummyIP, dummyPort);
        assertNotNull(handlerSameProperties);
        assertTrue(dummyHandler.equals(handlerSameProperties));

        NetworkHandler handlerNull = null;
        assertNull(handlerNull);
        assertFalse(dummyHandler.equals(handlerNull));

        String IP = "1.1.1.1";
        Integer port = 2000;
        NetworkHandler handlerDifferentProperties = new NetworkHandler(IP, port);
        assertNotNull(handlerDifferentProperties);
        assertFalse(dummyHandler.equals(handlerDifferentProperties));
    }


    @After
    public void tearDown() {
        assertNotNull(dummyHandler);
        dummyHandler = null;
        assertNull(dummyHandler);
    }
}
