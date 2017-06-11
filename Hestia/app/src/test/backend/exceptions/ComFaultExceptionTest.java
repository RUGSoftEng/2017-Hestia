package com.rugged.application.hestia.backend.exceptions;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import hestia.backend.exceptions.ComFaultException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;

@RunWith(JUnit4.class)
public class ComFaultExceptionTest {
    private String DEFAULT_ERROR;
    private String DEFAULT_MESSAGE;
    private ComFaultException dummyComFaultException;

    @Before
    public void setUp() {
        DEFAULT_ERROR = "default_error";
        DEFAULT_MESSAGE = "default_message";
        dummyComFaultException = new ComFaultException(DEFAULT_ERROR, DEFAULT_MESSAGE);
        assertNotNull(dummyComFaultException);
    }

    @Test
    public void packageNameTest(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        Assert.assertEquals("com.rugged.application.hestia", appContext.getPackageName());
    }

    @Test
    public void setAndGetErrorTest() {
        assertEquals(DEFAULT_ERROR, dummyComFaultException.getError());
        String newError = "new_error";
        assertNotSame(DEFAULT_ERROR, newError);
        dummyComFaultException.setError(newError);
        assertEquals(newError, dummyComFaultException.getError());
    }

    @Test
    public void setAndGetMessageTest() {
        assertEquals(DEFAULT_MESSAGE, dummyComFaultException.getMessage());
        String newMessage = "new_message";
        assertNotSame(DEFAULT_MESSAGE, newMessage);
        dummyComFaultException.setMessage(newMessage);
        assertEquals(newMessage, dummyComFaultException.getMessage());
    }
}
