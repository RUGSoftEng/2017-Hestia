package com.rugged.application.hestia.UITests;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.rugged.application.hestia.R;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import hestia.UI.DeviceListActivity;
import hestia.UI.SingleFragmentActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.rugged.application.hestia.R.id.context_menu;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class ChangeUserPassTest {
    private final String OLD_USER = "admin";
    private final String OLD_PASS = "password";
    private final String NEW_USER = "testuser";
    private final String NEW_PASS = "testpass";
    private final String LOGOUT_TEXT = SingleFragmentActivity.logoutText;
    private final String PACKAGE_NAME = "com.rugged.application.hestia";
    private final String CHANGE_TEXT = SingleFragmentActivity.changeLoginText;

    @Rule
    public IntentsTestRule<DeviceListActivity> mIntentsRule =
            new IntentsTestRule<>(DeviceListActivity.class);

    @Test
    public void changeUserPassTest(){
        // Change user
        setCredentials(NEW_USER,OLD_PASS,NEW_PASS,NEW_PASS);

        logoutLogin(NEW_USER,NEW_PASS);
    }

    @After
    public void resetCredentials(){
        setCredentials(OLD_USER,NEW_PASS,OLD_PASS,OLD_PASS);
    }

    public void setCredentials(String user, String pass, String nnpass, String npass){
        onView(withId(context_menu)).perform(click());

        onView(withText(CHANGE_TEXT)).perform(click());

        onView(withId(R.id.newUser)).perform(clearText(),typeText(user), closeSoftKeyboard());

        onView(withId(R.id.oldPass)).perform(clearText(),typeText(pass), closeSoftKeyboard());

        onView(withId(R.id.newPass)).perform(clearText(),typeText(npass), closeSoftKeyboard());

        onView(withId(R.id.newPassCheck)).perform(clearText(),typeText(nnpass), closeSoftKeyboard());

        onView(withId(R.id.confirm_button)).perform(click());
    }

    public void logoutLogin(String user, String pass){
        // Logout
        onView(withId(context_menu)).perform(click());

        onView(withText(LOGOUT_TEXT)).perform(click());

        // Login

        onView(withId(R.id.username)).perform(typeText(user), closeSoftKeyboard());

        onView(withId(R.id.password)).perform(typeText(pass), closeSoftKeyboard());

        onView(withId(R.id.loginButton)).perform(click());

        intended(allOf(
                hasComponent(hasShortClassName("hestia.UI.DeviceListActivity")),
                toPackage(PACKAGE_NAME)));
    }

}
