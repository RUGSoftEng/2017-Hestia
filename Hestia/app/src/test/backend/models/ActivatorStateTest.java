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
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

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

    /**
     * setRawState() and getRawState() methods tests.
     */
    @Test
    public void setAndGetBoolRawStateTest() {
        assertEquals(DEFAULT_BOOL_RAW_STATE, boolActivatorState.getRawState());
        Boolean newBoolRawState = true;
        assertNotEquals(DEFAULT_BOOL_RAW_STATE, newBoolRawState);
        boolActivatorState.setRawState(newBoolRawState);
        assertEquals(newBoolRawState, boolActivatorState.getRawState());
    }

    @Test
    public void setAndGetFloatRawStateTest() {
        assertEquals(DEFAULT_FLOAT_RAW_STATE, floatActivatorState.getRawState());
        Float newFloatRawState = 0.3f;
        assertNotEquals(DEFAULT_FLOAT_RAW_STATE, newFloatRawState);
        floatActivatorState.setRawState(newFloatRawState);
        assertEquals(newFloatRawState, floatActivatorState.getRawState());
    }

    @Test
    public void setAndGetStringRawStateTest() {
        assertEquals(DEFAULT_STRING_RAW_STATE, stringActivatorState.getRawState());
        String newStringRawState = "newState";
        assertNotEquals(DEFAULT_STRING_RAW_STATE, newStringRawState);
        stringActivatorState.setRawState(newStringRawState);
        assertEquals(newStringRawState, stringActivatorState.getRawState());
    }

    /**
     * setType() and getType() methods tests.
     */
    @Test
    public void setAndGetBoolTypeTest() {
        assertEquals(DEFAULT_BOOL_TYPE, boolActivatorState.getType());
        String newBoolType = "newBoolType";
        assertNotEquals(DEFAULT_BOOL_TYPE, newBoolType);
        boolActivatorState.setType(newBoolType);
        assertEquals(newBoolType, boolActivatorState.getType());
    }

    @Test
    public void setAndGetFloatTypeTest() {
        assertEquals(DEFAULT_FLOAT_TYPE, floatActivatorState.getType());
        String newFloatType = "newFloatType";
        assertNotEquals(DEFAULT_FLOAT_TYPE, newFloatType);
        floatActivatorState.setType(newFloatType);
        assertEquals(newFloatType, floatActivatorState.getType());
    }

    @Test
    public void setAndGetStringTypeTest() {
        assertEquals(DEFAULT_STRING_TYPE, stringActivatorState.getType());
        String newStringType = "newStringType";
        assertNotEquals(DEFAULT_STRING_TYPE, newStringType);
        stringActivatorState.setType(newStringType);
        assertEquals(newStringType, stringActivatorState.getType());
    }

    /**
     * getRawStateJSON() method tests.
     */
    @Test
    public void getBoolRawStateJSONTest() {
        JsonPrimitive boolPrimitive = new JsonPrimitive(DEFAULT_BOOL_RAW_STATE);
        assertNotNull(boolPrimitive);
        assertEquals(boolPrimitive, boolActivatorState.getRawStateJSON());
    }

    @Test
    public void getFloatRawStateJSONTest() {
        JsonPrimitive floatPrimitive = new JsonPrimitive(DEFAULT_FLOAT_RAW_STATE);
        assertNotNull(floatPrimitive);
        assertEquals(floatPrimitive, floatActivatorState.getRawStateJSON());
    }

    @Test
    public void getStringRawStateJSONTest() {
        JsonPrimitive stringPrimitive = new JsonPrimitive(DEFAULT_STRING_RAW_STATE);
        assertNotNull(stringPrimitive);
        assertEquals(stringPrimitive, stringActivatorState.getRawStateJSON());
    }

    /**
     * equals() and hashCode() methods tests.
     */
    @Test
    public void equalsAndHashCodeBoolActivatorStateSamePropertiesTest() {
        ActivatorState<Boolean> newBoolActivatorState = new ActivatorState<>(DEFAULT_BOOL_RAW_STATE, DEFAULT_BOOL_TYPE);
        assertNotNull(newBoolActivatorState);
        assertTrue(newBoolActivatorState.equals(boolActivatorState));
        assertFalse(newBoolActivatorState.equals(stringActivatorState));
        assertFalse(newBoolActivatorState.equals(floatActivatorState));
        assertEquals(newBoolActivatorState.hashCode(), boolActivatorState.hashCode());
    }

    @Test
    public void equalsAndHashCodeFloatActivatorStateSamePropertiesTest() {
        ActivatorState<Float> newFloatActivatorState = new ActivatorState<>(DEFAULT_FLOAT_RAW_STATE, DEFAULT_FLOAT_TYPE);
        assertNotNull(newFloatActivatorState);
        assertTrue(newFloatActivatorState.equals(floatActivatorState));
        assertFalse(newFloatActivatorState.equals(boolActivatorState));
        assertFalse(newFloatActivatorState.equals(stringActivatorState));
        assertEquals(newFloatActivatorState.hashCode(), floatActivatorState.hashCode());
    }

    @Test
    public void equalsAndHashCodeStringActivatorStateSamePropertiesTest() {
        ActivatorState<String> newStringActivatorState = new ActivatorState<>(DEFAULT_STRING_RAW_STATE, DEFAULT_STRING_TYPE);
        assertNotNull(newStringActivatorState);
        assertTrue(newStringActivatorState.equals(stringActivatorState));
        assertFalse(newStringActivatorState.equals(boolActivatorState));
        assertFalse(newStringActivatorState.equals(floatActivatorState));
        assertEquals(newStringActivatorState.hashCode(), stringActivatorState.hashCode());
    }

    @Test
    public void equalsNullTest() {
        ActivatorState activatorStateNull = null;
        assertNull(activatorStateNull);
        assertFalse(boolActivatorState.equals(activatorStateNull));
        assertFalse(floatActivatorState.equals(activatorStateNull));
        assertFalse(stringActivatorState.equals(activatorStateNull));
    }

    @Test
    public void equalsAndHashCodeBoolActivatorStateDifferentPropertiesTest() {
        String newBoolType = "bool";
        Boolean newRawValue = true;
        ActivatorState<Boolean> differentBoolActivatorState = new ActivatorState<>(newRawValue, newBoolType);
        assertNotNull(differentBoolActivatorState);
        assertFalse(boolActivatorState.equals(differentBoolActivatorState));
        assertNotSame(boolActivatorState.hashCode(), differentBoolActivatorState.hashCode());
    }

    @Test
    public void equalsAndHashCodeFloatActivatorStateDifferentPropertiesTest() {
        String newBoolType = "float";
        Float newRawValue = 5.9f;
        ActivatorState<Float> differentFloatActivatorState = new ActivatorState<>(newRawValue, newBoolType);
        assertNotNull(differentFloatActivatorState);
        assertFalse(floatActivatorState.equals(differentFloatActivatorState));
        assertNotSame(floatActivatorState.hashCode(), differentFloatActivatorState.hashCode());
    }

    @Test
    public void equalsAndHashCodeStringActivatorStateDifferentPropertiesTest() {
        String newBoolType = "string";
        String newRawValue = "newRawValueRandomString";
        ActivatorState<String> differentStringActivatorState = new ActivatorState<>(newRawValue, newBoolType);
        assertNotNull(differentStringActivatorState);
        assertFalse(stringActivatorState.equals(differentStringActivatorState));
        assertNotSame(stringActivatorState.hashCode(), differentStringActivatorState.hashCode());
    }

    /**
     * toString() method tests.
     */
    @Test
    public void boolStateToStringTest() {
        String boolStateToString = DEFAULT_BOOL_TYPE + " - " + DEFAULT_BOOL_RAW_STATE;
        assertEquals(boolStateToString, boolActivatorState.toString());
    }

    @Test
    public void floatStateToStringTest() {
        String floatStateToString = DEFAULT_FLOAT_TYPE + " - " + DEFAULT_FLOAT_RAW_STATE;
        assertEquals(floatStateToString, floatActivatorState.toString());
    }

    @Test
    public void stringStateToStringTest() {
        String stringStateToString = DEFAULT_STRING_TYPE + " - " + DEFAULT_STRING_RAW_STATE;
        assertEquals(stringStateToString, stringActivatorState.toString());
    }
}
