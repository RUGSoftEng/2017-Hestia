package com.rugged.application.hestia.backend.models;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.HashMap;
import hestia.backend.models.RequiredInfo;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class RequiredInfoTest {
    private String DEFAULT_COLLECTION;
    private String DEFAULT_PLUGIN;
    private HashMap<String, String> DEFAULT_INFO;
    private RequiredInfo dummyRequiredInfo;

    @Before
    public void setUp() {
        DEFAULT_COLLECTION = "default_collection";
        DEFAULT_PLUGIN = "default_plugin";
        DEFAULT_INFO = new HashMap<>();
        dummyRequiredInfo = new RequiredInfo(DEFAULT_COLLECTION, DEFAULT_PLUGIN, DEFAULT_INFO);
        assertTrue(DEFAULT_INFO.isEmpty());
        assertNotNull(dummyRequiredInfo);
    }

    @Test
    public void packageNameTest(){
        Context appContext = InstrumentationRegistry.getTargetContext();
        Assert.assertEquals("com.rugged.application.hestia", appContext.getPackageName());
    }

    @Test
    public void setAndGetCollectionTest() {
        assertEquals(DEFAULT_COLLECTION, dummyRequiredInfo.getCollection());
        String newCollection = "newCollection";
        dummyRequiredInfo.setCollection(newCollection);
        assertEquals(newCollection, dummyRequiredInfo.getCollection());
    }

    @Test
    public void setAndGetPluginTest() {
        assertEquals(DEFAULT_PLUGIN, dummyRequiredInfo.getPlugin());
        String newPlugin = "newPlugin";
        dummyRequiredInfo.setPlugin(newPlugin);
        assertEquals(newPlugin, dummyRequiredInfo.getPlugin());
    }

    @Test
    public void setAndGetInfoTest() {
        assertEquals(DEFAULT_INFO, dummyRequiredInfo.getInfo());
        HashMap<String, String> newInfo = new HashMap<>();
        newInfo.put("new_key", "new_value");
        assertNotSame(DEFAULT_INFO, newInfo);
        dummyRequiredInfo.setInfo(newInfo);
        assertEquals(newInfo, dummyRequiredInfo.getInfo());
    }
}
