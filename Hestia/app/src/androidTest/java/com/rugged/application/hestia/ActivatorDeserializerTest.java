package com.rugged.application.hestia;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Type;

import static org.junit.Assert.*;

import hestia.backend.Activator;
import hestia.backend.ActivatorDeserializer;
import hestia.backend.ActivatorState;

@RunWith(AndroidJUnit4.class)
public class ActivatorDeserializerTest {
    private static ActivatorState<Float> floatActivatorState;
    private static ActivatorState<Boolean> boolActivatorState;

    private static Activator testFloatActivator;
    private static Activator testBoolActivator;
    private static String TAG = "DESERIALT";

    @Test
    public void deserializeBoolActivatorTest(){
        boolActivatorState = new ActivatorState<Boolean>(true,"bool");
        testBoolActivator = new Activator(1,boolActivatorState,"TEST_SWITCH");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Activator.class, new ActivatorDeserializer());
        Gson gson = gsonBuilder.create();

        String boolJson = gson.toJson(testBoolActivator,
                new TypeToken<Activator>(){}.getType());

        JsonElement testBool = new JsonParser().parse(boolJson);

        Activator deserializedBool = gson.fromJson(testBool, Activator.class);
        Log.d(TAG,"testBoolActivator :: " + testBoolActivator);
        Log.d(TAG,"deserializedBool :: " + deserializedBool);

        assertTrue(testBoolActivator.equals(deserializedBool));

    }


}
