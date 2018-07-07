package ua.yurezcv.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import ua.yurezcv.bakingapp.R;
import ua.yurezcv.bakingapp.data.model.Recipe;
import ua.yurezcv.bakingapp.data.model.RecipeStep;
import ua.yurezcv.bakingapp.ui.recipes.RecipesGridFragment;
import ua.yurezcv.bakingapp.utils.Utils;

public class MainRecipesActivity extends AppCompatActivity implements RecipesGridFragment.OnListFragmentInteractionListener {

    public static final String EXTRA_RECIPE_TITLE = "extra_recipe_steps";
    public static final String EXTRA_RECIPE_STEPS = "extra_recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
    }

    @Override
    public void onListFragmentInteraction(Recipe item) {
        // prepare data for a bundle
        ArrayList<RecipeStep> steps = new ArrayList<>();
        steps.add(Utils.convetIngredientsToStep(item.getIngredients()));
        steps.addAll(item.getSteps());

        // prepare intent and pass the data between activities
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(EXTRA_RECIPE_TITLE, item.getName());
        intent.putParcelableArrayListExtra(EXTRA_RECIPE_STEPS, steps);
        startActivity(intent);
    }
}
