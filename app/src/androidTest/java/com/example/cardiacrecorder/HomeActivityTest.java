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
import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)





public class HomeActivityTest {


    @Test
    public void testAll(){
        testAddEntry();
//        testUpdateEntry();
        testDeleteEntry();
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

    @Rule
    public ActivityScenarioRule<HomeActivity> activityRule = new ActivityScenarioRule<>(HomeActivity.class);






    @Test
    public void testDeleteEntry(){
        try {
            Thread.sleep(4000); // Adjust the delay as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, Action.clickChildViewWithId(R.id.deleteButton)));

//        Espresso.onView(withId(R.id.recyclerView))
//                .check(doesNotExist() );
        Espresso.onView(withId(R.id.recyclerView)).check(new RecyclerViewItemCountAssertion(0));
//        onView(withId(R.id.recyclerView)).check(new RecyclerViewItemCountAssertion(0));
    }


    @Test
    public void testUpdateEntry(){
        try {
            Thread.sleep(4000); // Adjust the delay as needed
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, Action.clickChildViewWithId(R.id.updateButton)
                )
        );

        Espresso.onView(ViewMatchers.withId(R.id.systolicPressure)).perform(clearText());
        Espresso.onView(ViewMatchers.withId(R.id.diastolicPressure)).perform(clearText());
        Espresso.onView(ViewMatchers.withId(R.id.heartRate)).perform(clearText());

        onView(withId(R.id.systolicPressure)).perform(ViewActions.typeText("130"));
        onView(withId(R.id.diastolicPressure)).perform(ViewActions.typeText("50"));
        onView(withId(R.id.heartRate)).perform(ViewActions.typeText("90"));
        onView(withId(R.id.saveBtn)).perform(click());

//        onView(withId(R.id.recyclerView)).check(new RecyclerViewItemCountAssertion(1));
        onView(withText("Systolic Pressure")).check(doesNotExist());
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