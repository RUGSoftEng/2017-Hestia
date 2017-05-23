package com.rugged.application.hestia.backendTests;

import android.support.test.runner.AndroidJUnit4;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import static org.junit.Assert.*;
import hestia.backend.Activator;
import hestia.backend.ActivatorDeserializer;

@RunWith(AndroidJUnit4.class)
public class DeserializerTest {

    private static final String testBoolJSON =
            "{\"state\": false,\"type\": \"bool\",\"name\": \"On/Off\",\"activatorId\": \"591853a1094c1d2bdcbe9f21\",\"rank\": 0}";

    @Test
    public void deserializeBoolActivatorTest(){

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Activator.class, new ActivatorDeserializer());
        Gson gson = gsonBuilder.create();

        JsonElement testBool = new JsonParser().parse(testBoolJSON);
        Activator deserializedBool = gson.fromJson(testBool, Activator.class);


        JsonObject testBoolJson = (JsonObject) testBool;
        assertTrue(deserializedBool.getId().equals(testBoolJson.get("activatorId").getAsString()));
        assertTrue(deserializedBool.getName().equals(testBoolJson.get("name").getAsString()));


    }

    public String readJSONFile(String target){
        String everything = "";

        try {
            BufferedReader br = new BufferedReader(new FileReader(target));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            everything = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return everything;
    }

}
