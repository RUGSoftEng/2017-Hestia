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
    private JsonObject testDetails=new JsonObject();
    private String testError;
    private String type;
    private String message;

    @Before
    public void setUp() {
        testError="testError";
        type="testType";
        message= "error message";
        testDetails.addProperty("type",type);
        testDetails.addProperty("message",message);
        testException   = new DatabaseException(testError,testDetails);
    }
    @Test
    public void ExceptionTest(){
        assertEquals("testError",testException.getError());
        assertEquals("error message",testException.getMessage());
        assertEquals("testType",testException.getType());
    }
}
