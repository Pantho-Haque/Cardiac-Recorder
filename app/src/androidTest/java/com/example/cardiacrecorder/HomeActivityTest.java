package com.example.cardiacrecorder;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RunWith(AndroidJUnit4.class)

public class HomeActivityTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Rule
    public ActivityScenarioRule<HomeActivity> homeActivityRule = new ActivityScenarioRule<>(HomeActivity.class);

    @Test
    public void testAll() {
        testLogin();
        testAddEntry();
        testUpdateEntry();
        testDeleteEntry();
        testLogout();
    }

    public class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final int expectedCount;

        public RecyclerViewItemCountAssertion(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assertThat(adapter.getItemCount(), is(expectedCount));
        }
    }

    public static ViewAction waitFor(final Matcher<View> viewMatcher) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(View.class);
            }

            @Override
            public String getDescription() {
                return "Wait until view is " + viewMatcher.toString();
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + 5000; // Adjust the timeout as needed
                while (System.currentTimeMillis() < endTime) {
                    if (viewMatcher.matches(view)) {
                        return;
                    }
                    uiController.loopMainThreadForAtLeast(50);
                }
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }


    @Test
    public void testLogin(){
        try {
            Thread.sleep(4000); // Adjust the delay as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create a CountDownLatch with a count of 1
        final CountDownLatch latch = new CountDownLatch(1);



        onView(withId(R.id.loginEmail)).perform(ViewActions.typeText("panthohaque927908@gmail.com"));
        onView(withId(R.id.loginPassword)).perform(ViewActions.typeText("pantho"));
        onView(withId(R.id.loginBtn)).perform(click());

        // Wait for the RecyclerView action to complete
        try {
            latch.await(5, TimeUnit.SECONDS); // Adjust the timeout as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.logoutBtn)).perform(waitFor(isDisplayed()));

    }


    @Test
    public void testLogout() {
        try {
            Thread.sleep(2000); // Adjust the delay as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.logoutBtn)).perform(click());
        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
    }

    @Test
    public void testDeleteEntry(){

        try {
            Thread.sleep(4000); // Adjust the delay as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create a CountDownLatch with a count of 1
        final CountDownLatch latch = new CountDownLatch(1);

        // Perform action on the RecyclerView item
        Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, Action.clickChildViewWithId(R.id.deleteButton)));

        // Wait for the RecyclerView action to complete
        try {
            latch.await(5, TimeUnit.SECONDS); // Adjust the timeout as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Perform assertion after the RecyclerView action is completed
        Espresso.onView(withId(R.id.recyclerView))
                .check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.systolicValue))
                .check(doesNotExist());


        //        try {
//            Thread.sleep(4000); // Adjust the delay as needed
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        onView(withId(R.id.recyclerView)).perform(
//                RecyclerViewActions.actionOnItemAtPosition(0, Action.clickChildViewWithId(R.id.deleteButton)));
//
//        Espresso.onView(withId(R.id.recyclerView))
//                .check(doesNotExist() );
//        Espresso.onView(withId(R.id.recyclerView)).check(new RecyclerViewItemCountAssertion(0));
//        onView(withId(R.id.recyclerView)).check(new RecyclerViewItemCountAssertion(0));
    }


    @Test
    public void testUpdateEntry(){
        try {
            Thread.sleep(4000); // Adjust the delay as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Espresso.onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, Action.clickChildViewWithId(R.id.updateButton)
                )
        );

        Espresso.onView(ViewMatchers.withId(R.id.systolicPressure)).perform(clearText());
        Espresso.onView(ViewMatchers.withId(R.id.diastolicPressure)).perform(clearText());
        Espresso.onView(ViewMatchers.withId(R.id.heartRate)).perform(clearText());

        onView(withId(R.id.systolicPressure)).perform(ViewActions.typeText("110"));
        onView(withId(R.id.diastolicPressure)).perform(ViewActions.typeText("50"));
        onView(withId(R.id.heartRate)).perform(ViewActions.typeText("90"));
        onView(withId(R.id.saveBtn)).perform(click());

        onView(withText("110mmHg")).check(matches(isDisplayed()));
    }

    @Test
    public void testAddEntry(){
        onView(withId(R.id.addTask)).perform(click());
        onView(withId(R.id.systolicPressure)).perform(ViewActions.typeText("120"));
        onView(withId(R.id.diastolicPressure)).perform(ViewActions.typeText("60"));
        onView(withId(R.id.heartRate)).perform(ViewActions.typeText("80"));
        onView(withId(R.id.saveBtn)).perform(click());

        onView(withId(R.id.recyclerView)).check(new RecyclerViewItemCountAssertion(1));
    }
}
