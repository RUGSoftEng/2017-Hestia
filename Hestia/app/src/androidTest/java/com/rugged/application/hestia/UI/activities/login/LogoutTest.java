package com.rugged.application.hestia.UI.activities.login;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.rugged.application.hestia.R;
import com.rugged.application.hestia.UI.UiTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import hestia.UI.activities.home.HomeActivity;
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
public class LogoutTest extends UiTest {

    @Rule
    public IntentsTestRule<HomeActivity> mIntentsRule =
            new IntentsTestRule<>(HomeActivity.class);

    @Test
    public void logoutTest(){
        onView(withId(context_menu)).perform(click());
        onView(withText(getStr(R.string.logout))).perform(click());
        intended(allOf(
                hasComponent(hasShortClassName(LOGIN_ACTIVITY)),
                toPackage(PACKAGE_NAME)));
    }

}
