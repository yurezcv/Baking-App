package ua.yurezcv.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ua.yurezcv.bakingapp.ui.MainRecipesActivity;
import ua.yurezcv.bakingapp.ui.recipes.RecipesGridFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ua.yurezcv.bakingapp.Utils.atPosition;


@RunWith(AndroidJUnit4.class)
public class MainRecipesActivityTest {
    @Rule
    public ActivityTestRule<MainRecipesActivity> mActivityTestRule =
            new ActivityTestRule<>(MainRecipesActivity.class);

    private IdlingResource mIdlingResource;

    private boolean isTablet;

    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResource() {
        RecipesGridFragment fragment = (RecipesGridFragment) mActivityTestRule.getActivity().
                getSupportFragmentManager().findFragmentById(R.id.fragment_recipes);

        mIdlingResource = fragment.getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);

        isTablet = mActivityTestRule.getActivity().getResources().getBoolean(R.bool.is_tablet);
    }

    @Test
    public void idlingResourceTest() {
        onView(withId(R.id.rv_recipes))
                .check(matches(atPosition(0, hasDescendant(withText("Nutella Pie")))));

        onView(withId(R.id.rv_recipes))
                .check(matches(atPosition(2, hasDescendant(withText("Yellow Cake")))));
    }

    @Test
    public void performClicksOnTwoRecipeItems() {
        onView(withId(R.id.rv_recipes)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));

        Espresso.pressBack();

        onView(withId(R.id.rv_recipes)).perform(
                RecyclerViewActions.actionOnItemAtPosition(3, click()));

        Espresso.pressBack();
    }

    @Test
    public void clickThroughFirstFiveSteps() {
        onView(withId(R.id.rv_recipes)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, click()));

        for (int i = 0; i < 5; i++) {
            if(!isTablet) {
                onView(withId(R.id.rv_steps))
                        .perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));

                Espresso.pressBack();
            } else {
                onView(withId(R.id.rv_steps))
                        .perform(RecyclerViewActions.actionOnItemAtPosition(i, click()));
            }
        }

    }

    // Remember to unregister resources when not needed to avoid malfunction.
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }



}
