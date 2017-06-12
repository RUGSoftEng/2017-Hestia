package com.rugged.application.hestia.backend.exceptions.ServerExceptions;


import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;

import hestia.backend.exceptions.ServerExceptions.InvalidStateException;
import hestia.backend.exceptions.ServerExceptions.SetupFailedException;

import static org.junit.Assert.assertEquals;

public class InvalidStateExceptionTest {
    private InvalidStateException testException;
    private String testExpectedType;
    private String testActualType;

    @Before
    public void setUp() {
        testExpectedType="test";
        testActualType="bool";
        testException = new InvalidStateException(testExpectedType,testActualType);
    }
    @Test
    public void ExceptionTest(){
        assertEquals("test",testException.getExpectedType());
        assertEquals("bool",testException.getValueType());
    }
}

