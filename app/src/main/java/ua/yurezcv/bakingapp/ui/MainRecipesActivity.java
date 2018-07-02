package ua.yurezcv.bakingapp.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import ua.yurezcv.bakingapp.R;
import ua.yurezcv.bakingapp.data.model.Recipe;
import ua.yurezcv.bakingapp.ui.recipes.RecipesGridFragment;

public class MainRecipesActivity extends AppCompatActivity implements RecipesGridFragment.OnListFragmentInteractionListener {

    private static final String TAG = "MainRecipesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        int fragmentLayoutId = R.id.recipes_fragment_container;

        RecipesGridFragment recipesGridFragment =
                (RecipesGridFragment) getSupportFragmentManager().findFragmentById(fragmentLayoutId);

        // check if fragment exists, init otherwise
        if (recipesGridFragment == null) {
            recipesGridFragment = new RecipesGridFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(fragmentLayoutId, recipesGridFragment);
            transaction.commit();
        }
    }

    @Override
    public void onListFragmentInteraction(Recipe item) {
        Log.d(TAG, "Recipe click " + item.getName());
    }
}
