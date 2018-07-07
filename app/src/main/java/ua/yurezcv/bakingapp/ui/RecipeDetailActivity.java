package ua.yurezcv.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

import ua.yurezcv.bakingapp.R;
import ua.yurezcv.bakingapp.data.model.RecipeStep;
import ua.yurezcv.bakingapp.ui.steps.StepsFragment;

public class RecipeDetailActivity extends AppCompatActivity implements StepsFragment.OnStepFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Get the Intent that started this activity and extract data from the bundle
        Intent intent = getIntent();

        String title = intent.getStringExtra(MainRecipesActivity.EXTRA_RECIPE_TITLE);
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }

        ArrayList<RecipeStep> steps = intent.getParcelableArrayListExtra(MainRecipesActivity.EXTRA_RECIPE_STEPS);

        int fragmentLayoutId = R.id.master_fragment_steps;

        StepsFragment stepsFragment =
                (StepsFragment) getSupportFragmentManager().findFragmentById(fragmentLayoutId);

        // check if fragment exists, init otherwise
        if (stepsFragment == null) {
            stepsFragment = StepsFragment.newInstance(steps);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(fragmentLayoutId, stepsFragment);
            transaction.commit();
        }
    }

    @Override
    public void onListFragmentInteraction(RecipeStep item) {
        Log.d("RecipeDetailActivity", item.getShortDescription());
    }
}
