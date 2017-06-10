package com.rugged.application.hestia.backend.exceptions.ServerExceptions;


import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;

import hestia.backend.exceptions.ServerExceptions.InvalidStateException;
import hestia.backend.exceptions.ServerExceptions.SetupFailedException;

import static org.junit.Assert.assertEquals;

public class InvalidStateExceptionTest {
    private InvalidStateException testException;
    private JsonObject testDetails=new JsonObject();
    private String testError;
    private String testExpectedType;
    private String testActualType;

    @Before
    public void setUp() {
        testError="testError";
        testExpectedType="test";
        testActualType="bool";
        testDetails.addProperty("expected_type",testExpectedType);
        testDetails.addProperty("value_type",testActualType);
        testException = new InvalidStateException(testError,testDetails);
    }
    @Test
    public void ExceptionTest(){
        assertEquals("testError",testException.getError());
        assertEquals("test",testException.getExpectedType());
        assertEquals("bool",testException.getValueType());
    }
}

