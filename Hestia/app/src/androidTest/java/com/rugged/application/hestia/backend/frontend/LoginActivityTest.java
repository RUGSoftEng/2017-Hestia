package com.rugged.application.hestia.backend.frontend;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import org.junit.After;
import org.junit.Before;

import android.support.test.espresso.matcher.ViewMatchers;
import com.rugged.application.hestia.R;

import org.junit.Rule;
import org.junit.Test;

import hestia.UI.HestiaApplication;
import hestia.UI.activities.login.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

/**
 * Created by lars on 12-6-17.
 */

public class LoginActivityTest {
    @Rule
    public IntentsTestRule<LoginActivity> mIntentsRule =
            new IntentsTestRule<>(LoginActivity.class);

    @Before
    public void setUp() {
        SharedPreferences.Editor editor = mIntentsRule.getActivity().getApplication().getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
        ((HestiaApplication)mIntentsRule.getActivity().getApplication()).getNetworkHandler().setIp("");
    }

    @Test
    public void ServerDiscoveryTest(){
        onView(ViewMatchers.withId(R.id.setServerButton)).perform(click());
        onView(ViewMatchers.withId(R.id.ip)).perform(typeText("192.168.178.30"), closeSoftKeyboard());
        onView(ViewMatchers.withText("Confirm")).perform(click());
        String newIp = ((HestiaApplication)mIntentsRule.getActivity().getApplication()).getNetworkHandler().getIp();
        assertEquals("192.168.178.30", newIp);

    }

    @Test
    public void loginTest(){
        onView(ViewMatchers.withId(R.id.username)).perform(typeText("admin"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.rememberButton)).perform(click());
        onView(withId(R.id.loginButton)).perform(click());
        intended(allOf(hasComponent(hasShortClassName("hestia.UI.activities.home.HomeActivity")),toPackage("com.rugged.application.hestia")));
    }

    @After
    public void resetCredentials() {
        SharedPreferences.Editor editor = mIntentsRule.getActivity().getApplication().getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
        ((HestiaApplication)mIntentsRule.getActivity().getApplication()).getNetworkHandler().setIp("");
    }
}
