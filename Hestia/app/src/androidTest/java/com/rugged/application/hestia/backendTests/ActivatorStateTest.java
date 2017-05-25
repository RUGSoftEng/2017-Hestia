package com.rugged.application.hestia.backendTests;

import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import hestia.backend.models.ActivatorState;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class ActivatorStateTest {
    private final Boolean DEFAULT_BOOL_RAW_STATE = false;
    private final String DEFAULT_BOOL_TYPE = "bool";
    private final Float DEFAULT_FLOAT_RAW_STATE = (float) 0.5;
    private final String DEFAULT_FLOAT_TYPE = "float";
    private ActivatorState<Float> floatActivatorState;
    private ActivatorState<Boolean> boolActivatorState;

    @Before
    public void setUp() {
        boolActivatorState = new ActivatorState<>(DEFAULT_BOOL_RAW_STATE, DEFAULT_BOOL_TYPE);
        floatActivatorState = new ActivatorState<>(DEFAULT_FLOAT_RAW_STATE, DEFAULT_FLOAT_TYPE);
    }

    @Test
    public void setAndGetRawStateTest() {
        assertEquals(DEFAULT_BOOL_RAW_STATE, boolActivatorState.getRawState());
        assertEquals(DEFAULT_FLOAT_RAW_STATE, floatActivatorState.getRawState());

        Boolean newBoolRawState = true;
        Float newFloatRawState = (float) 0.3;
        boolActivatorState.setRawState(newBoolRawState);
        floatActivatorState.setRawState(newFloatRawState);

        assertEquals(newBoolRawState,boolActivatorState.getRawState());
        assertEquals(newFloatRawState,floatActivatorState.getRawState());
        assertNotEquals(DEFAULT_BOOL_RAW_STATE,boolActivatorState.getRawState());
        assertNotEquals(DEFAULT_FLOAT_RAW_STATE,floatActivatorState.getRawState());
    }

    @Test
    public void setAndGetTypeTest() {
        assertEquals(DEFAULT_BOOL_TYPE, boolActivatorState.getType());
        assertEquals(DEFAULT_FLOAT_TYPE, floatActivatorState.getType());

        String newBoolType = "newBoolType";
        String newFloatType = "newFloatType";
        boolActivatorState.setType(newBoolType);
        floatActivatorState.setType(newFloatType);

        assertEquals(newBoolType,boolActivatorState.getType());
        assertEquals(newFloatType,floatActivatorState.getType());
        assertNotEquals(DEFAULT_BOOL_RAW_STATE,boolActivatorState.getType());
        assertNotEquals(DEFAULT_FLOAT_RAW_STATE,floatActivatorState.getType());
    }

    @Test
    public void toStringTest() {
        String boolStateToString = DEFAULT_BOOL_TYPE + " - " + DEFAULT_BOOL_RAW_STATE;
        String floatStateToString = DEFAULT_FLOAT_TYPE + " - " + DEFAULT_FLOAT_RAW_STATE;

        assertEquals(boolStateToString, boolActivatorState.toString());
        assertEquals(floatStateToString, floatActivatorState.toString());
    }
}
