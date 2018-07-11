package ua.yurezcv.bakingapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import java.util.ArrayList;

import ua.yurezcv.bakingapp.R;
import ua.yurezcv.bakingapp.data.model.RecipeStep;
import ua.yurezcv.bakingapp.ui.steps.StepDetailFragment;
import ua.yurezcv.bakingapp.ui.steps.StepViewModel;
import ua.yurezcv.bakingapp.ui.steps.StepsFragment;

public class RecipeDetailActivity extends AppCompatActivity implements StepsFragment.OnStepFragmentInteractionListener {

    private static final String TAG_FRAGMENT_STEP_DETAILS = "TagFragmentStepDetails";
    private static final int ID_MASTER_FRAGMENT = R.id.master_fragment_steps;
    private static final int ID_DETAIL_FRAGMENT = R.id.detail_fragment_steps;

    private StepViewModel mStepViewModel;

    private boolean isTwoColumn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // init the view model to share between fragment
        mStepViewModel = ViewModelProviders.of(this).get(StepViewModel.class);

        // Get the Intent that started this activity and extract data from the bundle
        Intent intent = getIntent();

        String title = intent.getStringExtra(MainRecipesActivity.EXTRA_RECIPE_TITLE);
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }

        if(findViewById(ID_DETAIL_FRAGMENT) != null && findViewById(ID_MASTER_FRAGMENT) != null) {
            isTwoColumn = true;
        }

        ArrayList<RecipeStep> steps = intent.getParcelableArrayListExtra(MainRecipesActivity.EXTRA_RECIPE_STEPS);

        if (savedInstanceState == null) {
            StepsFragment stepsFragment =
                    (StepsFragment) getSupportFragmentManager().findFragmentById(ID_MASTER_FRAGMENT);

            // check if fragment exists, init otherwise
            if (stepsFragment == null) {
                stepsFragment = StepsFragment.newInstance(steps);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(ID_MASTER_FRAGMENT, stepsFragment);
                transaction.commit();
            }
        }
    }

    @Override
    public void onListFragmentInteraction(RecipeStep item) {
        mStepViewModel.selectStep(item);

        StepDetailFragment detailFragment =
                (StepDetailFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_STEP_DETAILS);

        if (detailFragment == null) {
            detailFragment = new StepDetailFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(ID_MASTER_FRAGMENT, detailFragment, TAG_FRAGMENT_STEP_DETAILS);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
