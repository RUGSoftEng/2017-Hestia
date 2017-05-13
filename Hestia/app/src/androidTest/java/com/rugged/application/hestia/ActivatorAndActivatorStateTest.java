package com.rugged.application.hestia;

import android.content.Context;
import android.provider.Settings;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import hestia.backend.Activator;
import hestia.backend.ActivatorState;
import hestia.backend.BackendInteractor;
import hestia.backend.Device;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ActivatorAndActivatorStateTest {
    private static final int DEFAULT_ID = 0;
    private static final String DEFAULT_NAME = "TEST_ACTIVATOR";
    private static ActivatorState<Float> floatActivatorState;
    private static ActivatorState<Boolean> boolActivatorState;

    private static Activator testFloatActivator;
    private static Activator testBoolActivator;

    @Before
    public void setUp(){
        floatActivatorState = new ActivatorState<Float>(Float.valueOf("122"),"UNSIGNED_BYTE");
        testFloatActivator = new Activator(1,floatActivatorState,"TEST_SLIDER_255");

        boolActivatorState = new ActivatorState<Boolean>(true,"TOGGLE");
        testBoolActivator = new Activator(1,boolActivatorState,"TEST_SWITCH");
    }

    @Test
    public void activatorStateTest(){
        int returnedFloatState = (int) floatActivatorState.getRawState().floatValue();
        assertEquals(returnedFloatState,122);
        assertEquals("UNSIGNED_BYTE",floatActivatorState.getType());

        boolean returnedBoolState = (boolean) boolActivatorState.getRawState();
        assertEquals(true,returnedBoolState);
        assertEquals("TOGGLE",boolActivatorState.getType());

        // Testing rawState getters and setters
        boolean newBoolState = false;
        boolActivatorState.setRawState(newBoolState);
        assertEquals(newBoolState,boolActivatorState.getRawState());

        float newFloatState = (float) 0.34578;
        floatActivatorState.setRawState(newFloatState);
        double allowedDelta = 0.00000005;
        assertEquals(newFloatState,floatActivatorState.getRawState(),allowedDelta);

        // Testing type setter
        String typeString = "HESTIA_SWITCH";
        boolActivatorState.setType(typeString);
        assertEquals(typeString,boolActivatorState.getType());

    }

    @Test
    public void activatorConstructorTest(){
        assertEquals(floatActivatorState,testFloatActivator.getState());
        assertEquals(boolActivatorState,testBoolActivator.getState());
    }

    @Test
    public void activatorGettersAndSettersTest(){
        // Testing getId, setId
        assertEquals(1,testBoolActivator.getId());
        testBoolActivator.setId(0);
        assertEquals(DEFAULT_ID,testBoolActivator.getId());
        assertNotEquals(DEFAULT_ID,testFloatActivator.getId());

        // Testing setState
        assertNotEquals(boolActivatorState,testFloatActivator.getState());
        testFloatActivator.setState(boolActivatorState);
        assertEquals(boolActivatorState,testFloatActivator.getState());

        // Testing getName, setName
        assertEquals("TEST_SWITCH",testBoolActivator.getName());
        testBoolActivator.setName(DEFAULT_NAME);
        assertEquals(DEFAULT_NAME,testBoolActivator.getName());
    }

    @Test
    public void activatorEqualsAndHashTest(){
        // Testing hashCodes
        assertNotEquals(testBoolActivator.hashCode(),testFloatActivator.hashCode());
        assertEquals(testBoolActivator.hashCode(),testBoolActivator.hashCode());

        // Testing equals method
        assertTrue(testBoolActivator.equals(testBoolActivator));
        assertFalse(testBoolActivator.equals(testFloatActivator));
        assertFalse(testBoolActivator.equals(null));

    }

    @Test
    public void activatorStateEqualsAndHashTest(){
        // Testing hashcodes
        assertNotEquals(boolActivatorState.hashCode(),floatActivatorState.hashCode());
        assertEquals(boolActivatorState.hashCode(),boolActivatorState.hashCode());

        // Testing equals method
        assertTrue(boolActivatorState.equals(boolActivatorState));
        assertFalse(boolActivatorState.equals(floatActivatorState));
        assertFalse(boolActivatorState.equals(null));
    }

}
