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
    private String field;
    private String hint;

    @Before
    public void setUp() {
        field="testfield";
        hint= "error hint";
        testException = new SetupFailedException(field,hint);

    }
    @Test
    public void ExceptionTest(){
        assertEquals("error hint",testException.getHint());
        assertEquals("testfield",testException.getField());
    }
}
