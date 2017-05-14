package com.rugged.application.hestia;


import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import hestia.UI.LoginActivity;

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

//@RunWith(AndroidJUnit4.class)
public class LoginTest {
    private static final String PACKAGE_NAME = "com.rugged.application.hestia";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password";
    private static final String TAG = "LOGINTEST";

    /* Instantiate an IntentsTestRule object. */
/*
    @Rule
    public IntentsTestRule<LoginActivity> mIntentsRule =
            new IntentsTestRule<>(LoginActivity.class);

    @Test
    public void verifyMessageSentToMessageActivity() {

        onView(withId(R.id.username))
                .perform(typeText(USERNAME), closeSoftKeyboard());

        onView(withId(R.id.password))
                .perform(typeText(PASSWORD), closeSoftKeyboard());

        onView(withId(R.id.loginButton)).perform(click());

        intended(allOf(
                hasComponent(hasShortClassName("hestia.UI.DeviceListActivity")),
                toPackage(PACKAGE_NAME)));
    }
*/
}
