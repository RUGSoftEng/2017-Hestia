package com.rugged.application.hestia.backend.models;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.google.gson.JsonPrimitive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import hestia.backend.models.ActivatorState;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class ActivatorStateTest {
    private final Boolean DEFAULT_BOOL_RAW_STATE = false;
    private final String DEFAULT_BOOL_TYPE = "bool";
    private ActivatorState<Boolean> boolActivatorState;

    private final Float DEFAULT_FLOAT_RAW_STATE = 0.5f;
    private final String DEFAULT_FLOAT_TYPE = "float";
    private ActivatorState<Float> floatActivatorState;

    private final String DEFAULT_STRING_RAW_STATE = "default";
    private final String DEFAULT_STRING_TYPE = "string";
    private ActivatorState<String> stringActivatorState;

    @Before
    public void setUp() {
        boolActivatorState = new ActivatorState<>(DEFAULT_BOOL_RAW_STATE, DEFAULT_BOOL_TYPE);
        floatActivatorState = new ActivatorState<>(DEFAULT_FLOAT_RAW_STATE, DEFAULT_FLOAT_TYPE);
        stringActivatorState = new ActivatorState<>(DEFAULT_STRING_RAW_STATE, DEFAULT_STRING_TYPE);
    }

    @Test
    public void packageNameTest(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.rugged.application.hestia", appContext.getPackageName());
    }

    @Test
    public void setAndGetRawStateTest() {
        // Test bool activator state
        assertEquals(DEFAULT_BOOL_RAW_STATE, boolActivatorState.getRawState());
        Boolean newBoolRawState = true;
        assertNotEquals(DEFAULT_BOOL_RAW_STATE, newBoolRawState);
        boolActivatorState.setRawState(newBoolRawState);
        assertEquals(newBoolRawState, boolActivatorState.getRawState());

        // Test float activator state
        assertEquals(DEFAULT_FLOAT_RAW_STATE, floatActivatorState.getRawState());
        Float newFloatRawState = 0.3f;
        assertNotEquals(DEFAULT_FLOAT_RAW_STATE, newFloatRawState);
        floatActivatorState.setRawState(newFloatRawState);
        assertEquals(newFloatRawState, floatActivatorState.getRawState());

        // Test string activator state
        assertEquals(DEFAULT_STRING_RAW_STATE, stringActivatorState.getRawState());
        String newStringRawState = "newState";
        assertNotEquals(DEFAULT_STRING_RAW_STATE, newStringRawState);
        stringActivatorState.setRawState(newStringRawState);
        assertEquals(newStringRawState, stringActivatorState.getRawState());
    }

    @Test
    public void setAndGetTypeTest() {
        // Test bool activator state
        assertEquals(DEFAULT_BOOL_TYPE, boolActivatorState.getType());
        String newBoolType = "newBoolType";
        assertNotEquals(DEFAULT_BOOL_TYPE, newBoolType);
        boolActivatorState.setType(newBoolType);
        assertEquals(newBoolType, boolActivatorState.getType());

        // Test float activator state
        assertEquals(DEFAULT_FLOAT_TYPE, floatActivatorState.getType());
        String newFloatType = "newFloatType";
        assertNotEquals(DEFAULT_FLOAT_TYPE, newFloatType);
        floatActivatorState.setType(newFloatType);
        assertEquals(newFloatType, floatActivatorState.getType());

        // Test string activator state
        assertEquals(DEFAULT_STRING_TYPE, stringActivatorState.getType());
        String newStringType = "newStringType";
        assertNotEquals(DEFAULT_STRING_TYPE, newStringType);
        stringActivatorState.setType(newStringType);
        assertEquals(newStringType, stringActivatorState.getType());
    }

    @Test
    public void getRawStateJSONTest() {
        // Test bool activator state
        JsonPrimitive boolPrimitive = new JsonPrimitive(DEFAULT_BOOL_RAW_STATE);
        assertNotNull(boolPrimitive);
        assertEquals(boolPrimitive, boolActivatorState.getRawStateJSON());

        // Test float activator state
        JsonPrimitive floatPrimitive = new JsonPrimitive(DEFAULT_FLOAT_RAW_STATE);
        assertNotNull(floatPrimitive);
        assertEquals(floatPrimitive, floatActivatorState.getRawStateJSON());

        // Test string activator state
        JsonPrimitive stringPrimitive = new JsonPrimitive(DEFAULT_STRING_RAW_STATE);
        assertNotNull(stringPrimitive);
        assertEquals(stringPrimitive, stringActivatorState.getRawStateJSON());
    }

    @Test
    public void equalsTest() {
        // Test bool activator state
        ActivatorState<Boolean> newBoolActivatorState = new ActivatorState<>(DEFAULT_BOOL_RAW_STATE, DEFAULT_BOOL_TYPE);
        assertNotNull(newBoolActivatorState);
        assertTrue(newBoolActivatorState.equals(boolActivatorState));

        // Test float activator state
        ActivatorState<Float> newFloatActivatorState = new ActivatorState<>(DEFAULT_FLOAT_RAW_STATE, DEFAULT_FLOAT_TYPE);
        assertNotNull(newFloatActivatorState);
        assertTrue(newFloatActivatorState.equals(floatActivatorState));

        // Test string activator state
        ActivatorState<String> newStringActivatorState = new ActivatorState<>(DEFAULT_STRING_RAW_STATE, DEFAULT_STRING_TYPE);
        assertNotNull(newStringActivatorState);
        assertTrue(newStringActivatorState.equals(stringActivatorState));

        // Other tests
        assertFalse(newFloatActivatorState.equals(boolActivatorState));
        assertFalse(newBoolActivatorState.equals(stringActivatorState));
        assertFalse(newFloatActivatorState.equals(stringActivatorState));
    }

    @Test
    public void toStringTest() {
        String boolStateToString = DEFAULT_BOOL_TYPE + " - " + DEFAULT_BOOL_RAW_STATE;
        String floatStateToString = DEFAULT_FLOAT_TYPE + " - " + DEFAULT_FLOAT_RAW_STATE;
        String stringStateToString = DEFAULT_STRING_TYPE + " - " + DEFAULT_STRING_RAW_STATE;

        assertEquals(boolStateToString, boolActivatorState.toString());
        assertEquals(floatStateToString, floatActivatorState.toString());
        assertEquals(stringStateToString, stringActivatorState.toString());
    }
}
