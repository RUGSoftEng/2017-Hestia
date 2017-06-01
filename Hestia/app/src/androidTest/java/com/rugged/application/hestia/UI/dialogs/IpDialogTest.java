package com.rugged.application.hestia.UI.dialogs;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.rugged.application.hestia.R;

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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;


@RunWith(AndroidJUnit4.class)
public class IpDialogTest {
    private final String SET_IP_TEXT = "Set IP ";
    private final String VALID_IP = "192.168.0.1";
    private final String INVALID_IP = "0.3000.283.1";
    private final String INCORR_IP_TOAST="The IP address should be between 0.0.0.0 and 255.255.255.255";

    @Rule
    public ActivityTestRule<HomeActivity> activityTestRule =
            new ActivityTestRule<>(HomeActivity.class);

    public void openDialog() {
        onView(ViewMatchers.withId(R.id.context_menu)).perform(click());
        onView(withText(SET_IP_TEXT)).perform(click());
    }

    @Test
    public void checkValidIp(){
        openDialog();

        onView(withId(R.id.ip)).perform(clearText(),typeText(VALID_IP), closeSoftKeyboard());

        onView(withText("Confirm")).perform(click());

        // Check if the toast appears
        onView(withText(VALID_IP)).inRoot(withDecorView(not(is(activityTestRule.getActivity()
                .getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void checkInvalidIp(){
        openDialog();

        onView(withId(R.id.ip)).perform(clearText(),typeText(INVALID_IP), closeSoftKeyboard());

        onView(withText("Confirm")).perform(click());

        // Check if the toast appears
        onView(withText(INCORR_IP_TOAST)).inRoot(withDecorView(not(is(activityTestRule.getActivity()
                .getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

}
