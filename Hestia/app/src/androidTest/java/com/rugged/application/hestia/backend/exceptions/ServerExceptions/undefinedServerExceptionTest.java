package com.rugged.application.hestia.backend.exceptions.ServerExceptions;


import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;

import hestia.backend.exceptions.ServerExceptions.InvalidStateException;
import hestia.backend.exceptions.ServerExceptions.UndefinedServerException;

import static org.junit.Assert.assertEquals;

public class UndefinedServerExceptionTest {
    private UndefinedServerException testException;
    private String testError;

    @Before
    public void setUp() {
        testError="testError";
        testException = new UndefinedServerException(testError);

    }
    @Test
    public void ExceptionTest(){
        assertEquals("testError",testException.getError());
    }
}
