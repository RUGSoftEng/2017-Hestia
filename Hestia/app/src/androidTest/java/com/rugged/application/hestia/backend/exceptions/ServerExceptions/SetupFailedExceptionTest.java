package com.rugged.application.hestia.backend.exceptions.ServerExceptions;

import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import hestia.backend.exceptions.ServerExceptions.DatabaseException;
import hestia.backend.exceptions.ServerExceptions.SetupFailedException;
import hestia.backend.models.ActivatorState;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class SetupFailedExceptionTest {
    private SetupFailedException testException;
    private JsonObject testDetails=new JsonObject();
    private String testError;
    private String field;
    private String hint;

    @Before
    public void setUp() {
        testError="testError";
        field="testfield";
        hint= "error hint";
        testDetails.addProperty("field",field);
        testDetails.addProperty("hint",hint);
        testException = new SetupFailedException(testError,testDetails);

    }
    @Test
    public void ExceptionTest(){
        assertEquals("testError",testException.getError());
        assertEquals("error hint",testException.getHint());
        assertEquals("testfield",testException.getField());
    }
}
