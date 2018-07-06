package ua.yurezcv.bakingapp.ui;

import android.os.Bundle;
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
    }

    @Override
    public void onListFragmentInteraction(Recipe item) {
        Log.d(TAG, "Recipe click " + item.getName());
    }
}
