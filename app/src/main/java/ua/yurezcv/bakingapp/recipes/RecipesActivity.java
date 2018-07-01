package ua.yurezcv.bakingapp.recipes;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import ua.yurezcv.bakingapp.R;

public class RecipesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        // setup view model
        ViewModelProviders.of(this).get(RecipesViewModel.class);

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

}
