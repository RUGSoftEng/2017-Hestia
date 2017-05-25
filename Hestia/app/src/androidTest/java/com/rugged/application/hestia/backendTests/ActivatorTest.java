package com.rugged.application.hestia.backendTests;

import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import hestia.backend.models.Activator;
import hestia.backend.models.ActivatorState;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ActivatorTest {
    private final String DEFAULT_ID = "0";
    private final String DEFAULT_NAME = "TEST_ACTIVATOR";
    private final Integer DEFAULT_BOOL_RANK = 0;
    private final Boolean DEFAULT_BOOL_VALUE = false;
    private final String DEFAULT_BOOL_TYPE = "bool";
    private final Integer DEFAULT_FLOAT_RANK = 1;
    private final Float DEFAULT_FLOAT_VALUE = (float) 0.5;
    private final String DEFAULT_FLOAT_TYPE = "float";
    private ActivatorState<Float> floatActivatorState;
    private ActivatorState<Boolean> boolActivatorState;
    private Activator testFloatActivator;
    private Activator testBoolActivator;

    @Before
    public void setUp(){
        floatActivatorState = new ActivatorState<>(DEFAULT_FLOAT_VALUE, DEFAULT_FLOAT_TYPE);
        //testFloatActivator = new Activator(DEFAULT_ID, DEFAULT_FLOAT_RANK, floatActivatorState, DEFAULT_NAME);

        boolActivatorState = new ActivatorState<>(DEFAULT_BOOL_VALUE, DEFAULT_BOOL_TYPE);
        //testBoolActivator = new Activator(DEFAULT_ID, DEFAULT_BOOL_RANK, boolActivatorState, DEFAULT_NAME);
    }

    @Test
    public void setAndGetIdTest() {
        assertEquals(DEFAULT_ID, testBoolActivator.getId());
        assertEquals(DEFAULT_ID, testFloatActivator.getId());

        String newId = "testId";
        testBoolActivator.setId(newId);
        testFloatActivator.setId(newId);

        assertEquals(newId,testBoolActivator.getId());
        assertEquals(newId,testFloatActivator.getId());
        assertNotEquals(DEFAULT_ID,testBoolActivator.getId());
        assertNotEquals(DEFAULT_ID,testFloatActivator.getId());
    }

    /**
    @Test
    public void setAndGetStateTest() {
        assertEquals(boolActivatorState, testBoolActivator.getState());
        assertEquals(floatActivatorState, testFloatActivator.getState());

        ActivatorState<Boolean> newBoolState = new ActivatorState<>(true, "bool");
        ActivatorState<Float> newFloatState = new ActivatorState<>((float) 0.3, "float");
        testBoolActivator.setState(newBoolState);
        testFloatActivator.setState(newFloatState);

        assertEquals(newBoolState,testBoolActivator.getState());
        assertEquals(newFloatState,testFloatActivator.getState());
        assertNotEquals(boolActivatorState,testBoolActivator.getId());
        assertNotEquals(floatActivatorState,testFloatActivator.getId());
    }*/

    @Test
    public void setAndGetNameTest() {
        assertEquals(DEFAULT_NAME, testBoolActivator.getName());
        assertEquals(DEFAULT_NAME, testFloatActivator.getName());

        String newName = "testName";
        testBoolActivator.setName(newName);
        testFloatActivator.setName(newName);

        assertEquals(newName,testBoolActivator.getName());
        assertEquals(newName,testFloatActivator.getName());
        assertNotEquals(DEFAULT_NAME,testBoolActivator.getName());
        assertNotEquals(DEFAULT_NAME,testFloatActivator.getName());
    }

    @Test
    public void setAndGetRankTest() {
        assertEquals(DEFAULT_BOOL_RANK, testBoolActivator.getRank());
        assertEquals(DEFAULT_FLOAT_RANK, testFloatActivator.getRank());

        Integer newRank = 10;
        testBoolActivator.setRank(newRank);
        testFloatActivator.setRank(newRank);

        assertEquals(newRank,testBoolActivator.getRank());
        assertEquals(newRank,testFloatActivator.getRank());
        assertNotEquals(DEFAULT_BOOL_RANK,testBoolActivator.getRank());
        assertNotEquals(DEFAULT_FLOAT_RANK,testFloatActivator.getRank());
    }

    @Test
    public void activatorEqualsAndHashTest(){
        // Testing hashCodes
        assertNotEquals(testBoolActivator.hashCode(),testFloatActivator.hashCode());
        assertEquals(testBoolActivator.hashCode(),testBoolActivator.hashCode());

        // Testing equals method
        assertTrue(testBoolActivator.equals(testBoolActivator));
        assertFalse(testBoolActivator.equals(testFloatActivator));
    }

    @Test
    public void toStringTest() {
        String boolToString = DEFAULT_NAME + " " + boolActivatorState.toString();
        String floatToString = DEFAULT_NAME + " " + floatActivatorState.toString();

        assertEquals(boolToString, testBoolActivator.toString());
        assertEquals(floatToString, testFloatActivator.toString());
    }
}
