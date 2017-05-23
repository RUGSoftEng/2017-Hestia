package com.rugged.application.hestia.UITests;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import com.rugged.application.hestia.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import hestia.UI.DeviceListActivity;
import hestia.backend.Cache;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class SetIpTest {
    private final String SET_IP_TEXT = "Set IP ";
    private final String TEST_IP = "192.168.0.1";

    @Rule
    public IntentsTestRule<DeviceListActivity> mIntentsRule =
            new IntentsTestRule<>(DeviceListActivity.class);

    @Before
    public void checkDialog() {
        onView(ViewMatchers.withId(R.id.context_menu)).perform(click());
        onView(withText(SET_IP_TEXT)).perform(click());
    }

    @Test
    public void checkEnterIp(){
        onView(withId(R.id.ip)).perform(clearText(),typeText(TEST_IP), closeSoftKeyboard());

        onView(withId(R.id.confirm_button)).perform(click());

        assertEquals(Cache.getInstance().getIp(),TEST_IP);
    }


}
