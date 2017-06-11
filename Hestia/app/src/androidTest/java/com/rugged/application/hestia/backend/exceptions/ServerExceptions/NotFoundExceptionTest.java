package com.rugged.application.hestia.backend.exceptions.ServerExceptions;

import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;

import hestia.backend.exceptions.ServerExceptions.InvalidStateException;
import hestia.backend.exceptions.ServerExceptions.NotFoundException;

import static org.junit.Assert.assertEquals;
public class NotFoundExceptionTest {
    private NotFoundException testException;
    private JsonObject testDetails=new JsonObject();
    private String testError;
    private String testType;

    @Before
    public void setUp() {
        testError="testError";
        testType="bool";
        testDetails.addProperty("type",testType);
        testException = new NotFoundException(testError,testDetails);
    }
    @Test
    public void ExceptionTest(){
        assertEquals("testError",testException.getError());
        assertEquals("bool",testException.getType());
    }
}
