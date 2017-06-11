package com.rugged.application.hestia.UI.activities.login;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import com.rugged.application.hestia.R;
import com.rugged.application.hestia.UI.UiTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.rugged.application.hestia.R.id.context_menu;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class LoginTest extends UiTest {

    @Rule
    public IntentsTestRule<LoginActivity> mIntentsRule =
            new IntentsTestRule<>(LoginActivity.class);

    @Before
    public void setUp() {
        // Check if you were already logged in. In this case, log out.
        // This can only happen when you previously logged it and selected the "Remember me" box.
        try {
            onView(ViewMatchers.withId(R.id.username)).perform(click());
        } catch(NoMatchingViewException e) {
            onView(withId(context_menu)).perform(click());
            onView(withText(getStr(R.string.logout))).perform(click());
        }
    }

    @Test
    public void checkLogin() {
        onView(ViewMatchers.withId(R.id.username))
                .perform(typeText(getStr(R.string.standardUser)), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText(getStr(R.string.standardPass)), closeSoftKeyboard());
        onView(withId(R.id.rememberButton)).perform(click());
        onView(withId(R.id.loginButton)).perform(click());
        intended(allOf(
                hasComponent(hasShortClassName(HOME_ACTIVITY)),
                toPackage(PACKAGE_NAME)));

    }
}
