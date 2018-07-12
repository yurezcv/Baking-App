package ua.yurezcv.bakingapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import java.util.ArrayList;

import ua.yurezcv.bakingapp.R;
import ua.yurezcv.bakingapp.data.model.RecipeStep;
import ua.yurezcv.bakingapp.ui.steps.StepDetailFragment;
import ua.yurezcv.bakingapp.ui.steps.StepViewModel;
import ua.yurezcv.bakingapp.ui.steps.StepsFragment;

public class RecipeDetailActivity extends AppCompatActivity implements StepsFragment.OnStepFragmentInteractionListener {

    public static final String TAG_FRAGMENT_STEP_DETAILS = "TagFragmentStepDetails";
    private static final int ID_MASTER_FRAGMENT = R.id.master_fragment_steps;
    private static final int ID_DETAIL_FRAGMENT = R.id.detail_fragment_steps;

    private StepDetailFragment mDetailFragment;

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

        int selectedPosition = RecyclerView.NO_POSITION;

        if (findViewById(ID_DETAIL_FRAGMENT) != null && findViewById(ID_MASTER_FRAGMENT) != null) {
            isTwoColumn = true;
            selectedPosition = 0;
        }

        ArrayList<RecipeStep> steps = intent.getParcelableArrayListExtra(MainRecipesActivity.EXTRA_RECIPE_STEPS);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {

            StepsFragment masterFragment =
                    (StepsFragment) getSupportFragmentManager().findFragmentById(ID_MASTER_FRAGMENT);

            // check if fragment exists, init otherwise
            if (masterFragment == null) {
                masterFragment = StepsFragment.newInstance(steps, selectedPosition, isTwoColumn);
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(ID_MASTER_FRAGMENT, masterFragment);
                transaction.commit();
            }

            if (isTwoColumn) {
                // select first step
                mStepViewModel.selectStep(steps.get(0));

                mDetailFragment =
                        (StepDetailFragment) getSupportFragmentManager().findFragmentById(ID_DETAIL_FRAGMENT);
                if (mDetailFragment == null) {
                    mDetailFragment = new StepDetailFragment();

                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.add(ID_DETAIL_FRAGMENT, mDetailFragment);
                    transaction.commit();
                }
            }
        }
    }

    @Override
    public void onListFragmentInteraction(RecipeStep item) {
        mStepViewModel.selectStep(item);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (isTwoColumn) {
            mDetailFragment =
                    (StepDetailFragment) getSupportFragmentManager().findFragmentById(ID_DETAIL_FRAGMENT);
            if (mDetailFragment == null) {
                mDetailFragment = new StepDetailFragment();

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(ID_DETAIL_FRAGMENT, mDetailFragment, TAG_FRAGMENT_STEP_DETAILS);
                transaction.commit();
            }
        } else {
            mDetailFragment =
                    (StepDetailFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT_STEP_DETAILS);

            if (mDetailFragment == null) {
                mDetailFragment = new StepDetailFragment();

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(ID_MASTER_FRAGMENT, mDetailFragment, TAG_FRAGMENT_STEP_DETAILS);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }

    }
}
