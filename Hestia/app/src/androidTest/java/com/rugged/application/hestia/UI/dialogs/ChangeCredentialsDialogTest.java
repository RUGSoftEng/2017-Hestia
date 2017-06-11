package com.rugged.application.hestia.UI.dialogs;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.rugged.application.hestia.R;
import com.rugged.application.hestia.UI.UiTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import hestia.UI.activities.home.HomeActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.rugged.application.hestia.R.id.context_menu;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class ChangeCredentialsDialogTest extends UiTest {
    private final String NEW_USER = "testuser";
    private final String NEW_PASS = "testpass";

    @Rule
    public IntentsTestRule<HomeActivity> mIntentsRule =
            new IntentsTestRule<>(HomeActivity.class);

    @Before
    public void setUp() {
        // To be sure that the correct standard credentials are set, logout and login.
        onView(withId(context_menu)).perform(click());
        onView(withText(getStr(R.string.logout))).perform(click());
        onView(withId(R.id.username)).perform(typeText(getStr(R.string.standardUser)), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(getStr(R.string.standardPass)), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
    }

    @Test
    public void changeUserPassTest() {
        try {
            setCredentials(NEW_USER, getStr(R.string.standardPass), NEW_PASS, NEW_PASS);
            Thread.sleep(1000);
            logoutLogin(NEW_USER, NEW_PASS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void resetCredentials() {
        try {
            setCredentials(getStr(R.string.standardUser), NEW_PASS, getStr(R.string.standardPass)
                    , getStr(R.string.standardPass));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setCredentials(String user, String pass, String nnpass, String npass) throws InterruptedException {
        Thread.sleep(1000);
        onView(withId(context_menu)).perform(click());
        Thread.sleep(1000);
        onView(withText(getStr(R.string.changeUserPass))).perform(click());
        onView(withId(R.id.newUser)).perform(clearText(), typeText(user), closeSoftKeyboard());
        onView(withId(R.id.oldPass)).perform(clearText(), typeText(pass), closeSoftKeyboard());
        onView(withId(R.id.newPass)).perform(clearText(), typeText(npass), closeSoftKeyboard());
        onView(withId(R.id.newPassCheck)).perform(clearText(), typeText(nnpass), closeSoftKeyboard());
        onView(withText("Confirm")).perform(click());
    }

    public void logoutLogin(String user, String pass) throws InterruptedException {
        Thread.sleep(1000);
        onView(withId(context_menu)).perform(click());
        Thread.sleep(1000);
        onView(withText(getStr(R.string.logout))).perform(click());
        onView(withId(R.id.username)).perform(typeText(user), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(pass), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        intending(allOf(
                hasComponent(hasShortClassName(HOME_ACTIVITY)),
                toPackage(PACKAGE_NAME)));
    }

}