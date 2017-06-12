package com.rugged.application.hestia.backend.exceptions.ServerExceptions;

import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import hestia.backend.exceptions.ServerExceptions.DatabaseException;
import hestia.backend.models.ActivatorState;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DataBaseExceptionTest {
    private DatabaseException testException;
    private String type;
    private String message;

    @Before
    public void setUp() {
        type="testType";
        message= "error message";
        testException   = new DatabaseException(type,message);
    }
    @Test
    public void ExceptionTest(){
        assertEquals("error message",testException.getMessage());
        assertEquals("testType",testException.getType());
    }
}
