package ua.yurezcv.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.yurezcv.bakingapp.R;
import ua.yurezcv.bakingapp.data.model.Recipe;
import ua.yurezcv.bakingapp.ui.recipes.RecipesViewModel;

/**
 * The configuration screen for the {@link IngredientsWidget IngredientsWidget} AppWidget.
 */
public class IngredientsWidgetConfigureActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "ua.yurezcv.bakingapp.widget.IngredientsWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    private static final int DEFAULT_RECIPE_SELECTION = 0;

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBar;

    @BindView(R.id.rv_widget_recipes)
    RecyclerView mRecipeRecyclerView;

    private RecipeWidgetViewAdapter mAdapter;

/*    EditText mAppWidgetText;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = IngredientsWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            String widgetText = mAppWidgetText.getText().toString();
            saveRecipeId(context, mAppWidgetId, widgetText);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            IngredientsWidget.startWidgetUpdateService(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };*/

    private final List<Recipe> mRecipes = new ArrayList<>();

    public IngredientsWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveRecipeId(Context context, int appWidgetId, int recipeId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, recipeId);
        prefs.apply();
    }

    static int loadRecipeId(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(PREF_PREFIX_KEY + appWidgetId, DEFAULT_RECIPE_SELECTION);
    }

    static void deleteRecipeId(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        RecipesViewModel viewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.ingredients_widget_configure);
        ButterKnife.bind(this);

        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecipeWidgetViewAdapter();
        mRecipeRecyclerView.setAdapter(mAdapter);

        viewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipesEntries) {
                if (!mRecipes.isEmpty() && recipesEntries != null) {
                    mRecipes.clear();
                }
                if (recipesEntries != null) {
                    mRecipes.addAll(recipesEntries);
                    mAdapter.setData(mRecipes);
                    hideProgressBar();
                }
            }
        });

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        mAdapter.setSelectedPosition(loadRecipeId(this, mAppWidgetId));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_widget_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save_widget:
                saveRecipeId(this, mAppWidgetId, mAdapter.getSelectedRecipeId());

                // It is the responsibility of the configuration activity to update the app widget
                // AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                IngredientsWidget.startWidgetUpdateService(this, mAppWidgetId);

                // Make sure we pass back the original appWidgetId
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
                break;
        }
        return true;
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
}

