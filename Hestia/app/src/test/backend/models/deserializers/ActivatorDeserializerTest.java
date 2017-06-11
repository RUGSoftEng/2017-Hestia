package com.rugged.application.hestia.backend.models.deserializers;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import hestia.backend.models.Activator;
import hestia.backend.models.deserializers.ActivatorDeserializer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class ActivatorDeserializerTest {
    private static final String testBoolJSON =
            "{\"state\": false,\"type\": \"bool\",\"name\": \"On/Off\",\"activatorId\": \"dummyId\",\"rank\": 0}";

    private static final String testFloatJSON =
            "{\"state\": 0.5f,\"type\": \"float\",\"name\": \"Slider\",\"activatorId\": \"dummyId\",\"rank\": 1}";

    @Test
    public void packageNameTest(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("com.rugged.application.hestia", appContext.getPackageName());
    }

    @Test
    public void deserializeBoolActivatorTest() {
        // Initialize Gson object
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Activator.class, new ActivatorDeserializer());
        Gson gson = gsonBuilder.create();

        // Deserialize Bool Activator
        JsonElement testBoolElement = new JsonParser().parse(testBoolJSON);
        assertNotNull(testBoolElement);
        Activator deserializedBoolActivator = gson.fromJson(testBoolElement, Activator.class);
        assertNotNull(deserializedBoolActivator);
        JsonObject testBoolObject = testBoolElement.getAsJsonObject();
        assertNotNull(testBoolObject);
        assertEquals(deserializedBoolActivator.getId(), testBoolObject.get("activatorId").getAsString());
        assertEquals(deserializedBoolActivator.getName(), testBoolObject.get("name").getAsString());
    }

    @Test
    public void deserializeFloatActivatorTest() {
        // Initialize Gson object
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Activator.class, new ActivatorDeserializer());
        Gson gson = gsonBuilder.create();

        // Deserialize Float Activator
        JsonElement testFloatElement = new JsonParser().parse(testFloatJSON);
        assertNotNull(testFloatElement);
        Activator deserializedFloatActivator = gson.fromJson(testFloatElement, Activator.class);
        assertNotNull(deserializedFloatActivator);
        JsonObject testFloatObject = testFloatElement.getAsJsonObject();
        assertNotNull(testFloatObject);
        assertEquals(deserializedFloatActivator.getId(), testFloatObject.get("activatorId").getAsString());
        assertEquals(deserializedFloatActivator.getName(), testFloatObject.get("name").getAsString());
    }
}
