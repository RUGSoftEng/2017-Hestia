package com.rugged.application.hestia.backend.exceptions.ServerExceptions;

import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;

import hestia.backend.exceptions.ServerExceptions.InvalidStateException;
import hestia.backend.exceptions.ServerExceptions.NotFoundException;

import static org.junit.Assert.assertEquals;
public class NotFoundExceptionTest {
    private NotFoundException testException;
    private String testType;

    @Before
    public void setUp() {
        testType="bool";
        testException = new NotFoundException(testType);
    }
    @Test
    public void ExceptionTest(){
        assertEquals("bool",testException.getType());
    }
}
