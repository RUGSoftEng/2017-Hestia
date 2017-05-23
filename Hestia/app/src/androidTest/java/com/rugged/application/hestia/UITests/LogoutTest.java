package com.rugged.application.hestia.UITests;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import hestia.UI.DeviceListActivity;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.rugged.application.hestia.R.id.context_menu;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class LogoutTest {
    private final String LOGOUT_TEXT = "Logout ";
    private final String PACKAGE_NAME = "com.rugged.application.hestia";


    @Rule
    public IntentsTestRule<DeviceListActivity> mIntentsRule =
            new IntentsTestRule<>(DeviceListActivity.class);

    @Test
    public void logoutTest(){
        // Logout
        onView(withId(context_menu)).perform(click());

        onView(withText(LOGOUT_TEXT)).perform(click());

        intended(allOf(
                hasComponent(hasShortClassName("hestia.UI.LoginActivity")),
                toPackage(PACKAGE_NAME)));
    }

}
