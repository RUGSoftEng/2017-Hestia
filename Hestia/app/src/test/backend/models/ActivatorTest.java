package com.rugged.application.hestia.backend.models;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.IOException;
import java.util.ArrayList;
import hestia.backend.NetworkHandler;
import hestia.backend.exceptions.ComFaultException;
import hestia.backend.models.Activator;
import hestia.backend.models.ActivatorState;
import hestia.backend.models.Device;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class ActivatorTest {
    private final String DEFAULT_ID = "0";
    private final String DEFAULT_NAME = "TEST_ACTIVATOR";
    private final Integer DEFAULT_BOOL_RANK = 0;
    private final Boolean DEFAULT_BOOL_VALUE = false;
    private ActivatorState<Boolean> boolActivatorState;
    private Activator boolActivator;

    private final String DEFAULT_BOOL_TYPE = "bool";
    private final Integer DEFAULT_FLOAT_RANK = 1;
    private final Float DEFAULT_FLOAT_VALUE = 0.5f;
    private final String DEFAULT_FLOAT_TYPE = "float";
    private ActivatorState<Float> floatActivatorState;
    private Activator floatActivator;
    private Device device;
    private NetworkHandler handler;

    @Before
    public void setUp() {
        handler = new NetworkHandler("1.1.1.1", 1000);
        device = new Device("test", "test", "test", new ArrayList<Activator>(), handler);

        floatActivatorState = new ActivatorState<>(DEFAULT_FLOAT_VALUE, DEFAULT_FLOAT_TYPE);
        floatActivator = new Activator(DEFAULT_ID, DEFAULT_FLOAT_RANK, floatActivatorState, DEFAULT_NAME);
        floatActivator.setDevice(device);
        floatActivator.setHandler(handler);
        assertNotNull(floatActivator);

        boolActivatorState = new ActivatorState<>(DEFAULT_BOOL_VALUE, DEFAULT_BOOL_TYPE);
        boolActivator = new Activator(DEFAULT_ID, DEFAULT_BOOL_RANK, boolActivatorState, DEFAULT_NAME);
        boolActivator.setDevice(device);
        boolActivator.setHandler(handler);
        assertNotNull(boolActivator);
    }

    @Test
    public void packageNameTest(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.rugged.application.hestia", appContext.getPackageName());
    }

    @Test
    public void setAndGetIdTest() {
        assertEquals(DEFAULT_ID, boolActivator.getId());
        assertEquals(DEFAULT_ID, floatActivator.getId());

        String newId = "testId";
        boolActivator.setId(newId);
        floatActivator.setId(newId);

        assertEquals(newId, boolActivator.getId());
        assertEquals(newId, floatActivator.getId());
        assertNotEquals(DEFAULT_ID, boolActivator.getId());
        assertNotEquals(DEFAULT_ID, floatActivator.getId());
    }

    @Test
    public void setAndGetDeviceTest() {
        assertEquals(device, boolActivator.getDevice());
        assertEquals(device, floatActivator.getDevice());

        // New device
        Device newDevice = new Device(null, null, null, null, null);
        assertNotNull(newDevice);
        assertNotSame(device, newDevice);

        // Test bool activator
        boolActivator.setDevice(newDevice);
        assertEquals(newDevice, boolActivator.getDevice());
        assertNotNull(boolActivator.getDevice());

        // Test float activator
        floatActivator.setDevice(newDevice);
        assertEquals(newDevice, floatActivator.getDevice());
        assertNotNull(floatActivator.getDevice());
    }

    @Test
    public void setAndGetHandlerTest() {
        assertEquals(handler, boolActivator.getHandler());
        assertEquals(handler, floatActivator.getHandler());

        // New Network Handler
        NetworkHandler newHandler = new NetworkHandler(null, null);
        assertNotNull(newHandler);
        assertNotSame(handler, newHandler);

        // Test bool activator
        boolActivator.setHandler(newHandler);
        assertEquals(newHandler, boolActivator.getHandler());
        assertNotNull(boolActivator.getHandler());

        // Test float activator
        floatActivator.setHandler(newHandler);
        assertEquals(newHandler, floatActivator.getHandler());
        assertNotNull(floatActivator.getHandler());
    }

    @Test
    public void setAndGetBoolStateTestSuccess() throws IOException {
        assertEquals(boolActivatorState, boolActivator.getState());

        // mock Network Handler so that the POST method will return a JsonObject similar to
        // the one returned when the request is successful
        NetworkHandler mockHandlerSuccess = mock(NetworkHandler.class);
        when(mockHandlerSuccess.POST(any(JsonObject.class), any(String.class))).thenReturn(new JsonObject());
        boolActivator.setHandler(mockHandlerSuccess);

        // Attempt to change the state
        ActivatorState<Float> newFloatActivatorState = new ActivatorState<>(0.1f, "float");
        assertNotNull(newFloatActivatorState);
        assertNotSame(boolActivatorState, newFloatActivatorState);
        try {
            boolActivator.setState(newFloatActivatorState);
        } catch (Exception exception) {
            fail("Test threw exception: " + exception.toString());
        }
        assertEquals(newFloatActivatorState, boolActivator.getState());

        // Override POST method, now returning null.
        // Will use the boolActivatorState object as input
        when(mockHandlerSuccess.POST(any(JsonObject.class), any(String.class))).thenReturn(null);
        try {
            boolActivator.setState(boolActivatorState);
        } catch (Exception exception){
            Assert.fail("Test threw exception: " + exception.toString());
        }
        assertEquals(boolActivatorState, boolActivator.getState());
    }

    @Test
    public void setAndGetFloatStateTestSuccess() throws IOException {
        assertEquals(floatActivatorState, floatActivator.getState());

        // mock Network Handler so that the POST method will return a JsonObject similar to
        // the one returned when the request is successful
        NetworkHandler mockHandlerSuccess = mock(NetworkHandler.class);
        when(mockHandlerSuccess.POST(any(JsonObject.class), any(String.class))).thenReturn(new JsonObject());
        floatActivator.setHandler(mockHandlerSuccess);

        // Attempt to change the state
        ActivatorState<Float> newFloatActivatorState = new ActivatorState<>(0.1f, "float");
        assertNotNull(newFloatActivatorState);
        assertNotSame(floatActivatorState, newFloatActivatorState);
        try {
            floatActivator.setState(newFloatActivatorState);
        } catch (Exception exception) {
            fail("Test threw exception: " + exception.toString());
        }
        assertEquals(newFloatActivatorState, floatActivator.getState());

        // Override POST method, now returning null.
        // Will use the boolActivatorState object as input
        when(mockHandlerSuccess.POST(any(JsonObject.class), any(String.class))).thenReturn(null);
        try {
            floatActivator.setState(floatActivatorState);
        } catch (Exception exception){
            Assert.fail("Test threw exception: " + exception.toString());
        }
        assertEquals(floatActivatorState, floatActivator.getState());
    }

    @Test(expected = ComFaultException.class)
    public void setAndGetBoolStateTestComFaultException() throws IOException, ComFaultException {
        assertEquals(boolActivatorState, boolActivator.getState());

        // Mock Network Handler so that the POST method will return a JsonObject similar to
        // the one returned when the request failed and a ComFaultException was thrown
        NetworkHandler mockHandlerFail = mock(NetworkHandler.class);
        JsonObject object = new JsonObject(); // Mock a JsonObject containing the error
        object.addProperty("error", "error_field");
        object.addProperty("message", "message_field");
        when(mockHandlerFail.POST(any(JsonObject.class), any(String.class))).thenReturn(object);
        boolActivator.setHandler(mockHandlerFail);

        // Attempt to change the state
        ActivatorState<Float> newFloatActivatorState = new ActivatorState<>(0.1f, "float");
        assertNotNull(newFloatActivatorState);
        assertNotSame(boolActivatorState, newFloatActivatorState);
        boolActivator.setState(newFloatActivatorState);
    }

    @Test(expected = ComFaultException.class)
    public void setAndGetFloatStateTestComFaultException() throws IOException, ComFaultException {
        assertEquals(floatActivatorState, floatActivator.getState());

        // Mock Network Handler so that the POST method will return a JsonObject similar to
        // the one returned when the request failed and a ComFaultException was thrown
        NetworkHandler mockHandlerFail = mock(NetworkHandler.class);
        JsonObject object = new JsonObject(); // Mock a JsonObject containing the error
        object.addProperty("error", "error_field");
        object.addProperty("message", "message_field");
        when(mockHandlerFail.POST(any(JsonObject.class), any(String.class))).thenReturn(object);
        floatActivator.setHandler(mockHandlerFail);

        // Attempt to change the state
        ActivatorState<Float> newFloatActivatorState = new ActivatorState<>(0.1f, "float");
        assertNotNull(newFloatActivatorState);
        assertNotSame(floatActivatorState, newFloatActivatorState);
        floatActivator.setState(newFloatActivatorState);
    }

    @Test(expected = IOException.class)
    public void setAndGetBoolStateTestIOException() throws IOException, ComFaultException {
        assertEquals(boolActivatorState, boolActivator.getState());

        // Mck Network Handler so that the POST method will return a JsonObject similar to
        // the one returned when the request failed and an IOException was thrown
        NetworkHandler mockHandlerFail = mock(NetworkHandler.class);
        when(mockHandlerFail.POST(any(JsonObject.class), any(String.class))).thenThrow(IOException.class);
        boolActivator.setHandler(mockHandlerFail);

        // Attempt to change the state
        ActivatorState<Float> newFloatActivatorState = new ActivatorState<>(0.1f, "float");
        assertNotNull(newFloatActivatorState);
        assertNotSame(boolActivatorState, newFloatActivatorState);
        boolActivator.setState(newFloatActivatorState);
    }

    @Test(expected = IOException.class)
    public void setAndGetFloatStateTestIOException() throws IOException, ComFaultException {
        assertEquals(floatActivatorState, floatActivator.getState());

        // Mck Network Handler so that the POST method will return a JsonObject similar to
        // the one returned when the request failed and an IOException was thrown
        NetworkHandler mockHandlerFail = mock(NetworkHandler.class);
        when(mockHandlerFail.POST(any(JsonObject.class), any(String.class))).thenThrow(IOException.class);
        floatActivator.setHandler(mockHandlerFail);

        // Attempt to change the state
        ActivatorState<Float> newFloatActivatorState = new ActivatorState<>(0.1f, "float");
        assertNotNull(newFloatActivatorState);
        assertNotSame(floatActivatorState, newFloatActivatorState);
        floatActivator.setState(newFloatActivatorState);
    }

    @Test
    public void setAndGetNameTest() {
        assertEquals(DEFAULT_NAME, boolActivator.getName());
        assertEquals(DEFAULT_NAME, floatActivator.getName());

        String newName = "testName";
        boolActivator.setName(newName);
        floatActivator.setName(newName);

        assertEquals(newName, boolActivator.getName());
        assertEquals(newName, floatActivator.getName());
        assertNotEquals(DEFAULT_NAME, boolActivator.getName());
        assertNotEquals(DEFAULT_NAME, floatActivator.getName());
    }

    @Test
    public void setAndGetRankTest() {
        assertEquals(DEFAULT_BOOL_RANK, boolActivator.getRank());
        assertEquals(DEFAULT_FLOAT_RANK, floatActivator.getRank());

        Integer newRank = 10;
        boolActivator.setRank(newRank);
        floatActivator.setRank(newRank);

        assertEquals(newRank, boolActivator.getRank());
        assertEquals(newRank, floatActivator.getRank());
        assertNotEquals(DEFAULT_BOOL_RANK, boolActivator.getRank());
        assertNotEquals(DEFAULT_FLOAT_RANK, floatActivator.getRank());
    }

    @Test
    public void equalsAndHashCodeBoolActivatorSamePropertiesTest() {
        Activator boolActivatorSameProperties = new Activator(DEFAULT_ID, DEFAULT_BOOL_RANK,
                                                              boolActivatorState, DEFAULT_NAME);
        boolActivatorSameProperties.setDevice(device);
        boolActivatorSameProperties.setHandler(handler);
        assertNotNull(boolActivatorSameProperties);
        assertTrue(boolActivator.equals(boolActivatorSameProperties));
        assertEquals(boolActivator.hashCode(), boolActivatorSameProperties.hashCode());
    }

    @Test
    public void equalsAndHashCodeFloatActivatorSamePropertiesTest() {
        Activator floatActivatorSameProperties = new Activator(DEFAULT_ID, DEFAULT_FLOAT_RANK,
                                                               floatActivatorState, DEFAULT_NAME);
        floatActivatorSameProperties.setDevice(device);
        floatActivatorSameProperties.setHandler(handler);
        assertNotNull(floatActivatorSameProperties);
        assertTrue(floatActivator.equals(floatActivatorSameProperties));
        assertEquals(floatActivator.hashCode(), floatActivatorSameProperties.hashCode());
    }

    @Test
    public void equalsNullTest() {
        Activator activatorNull = null;
        assertNull(activatorNull);
        assertFalse(boolActivator.equals(activatorNull));
        assertFalse(floatActivator.equals(activatorNull));
    }

    @Test
    public void equalsAndHashCodeDifferentPropertiesTest() {
        String newId = "newActivatorId";
        Integer newRank = 100;
        String newName = "newActivatorName";
        String newType = "newActivatorType";
        String newRawValue = "newRawValue";
        ActivatorState newState = new ActivatorState(newRawValue, newType);
        Activator activatorDifferentProperties = new Activator(newId, newRank, newState, newName);
        activatorDifferentProperties.setDevice(device);
        activatorDifferentProperties.setHandler(handler);

        assertNotNull(activatorDifferentProperties);
        assertFalse(boolActivator.equals(activatorDifferentProperties));
        assertNotSame(boolActivator.hashCode(), activatorDifferentProperties.hashCode());
        assertFalse(floatActivator.equals(activatorDifferentProperties));
        assertNotSame(floatActivator.hashCode(), activatorDifferentProperties.hashCode());
    }

    @Test
    public void toStringTest() {
        String boolToString = DEFAULT_NAME + " " + boolActivatorState.toString();
        String floatToString = DEFAULT_NAME + " " + floatActivatorState.toString();

        assertEquals(boolToString, boolActivator.toString());
        assertEquals(floatToString, floatActivator.toString());
    }
}
