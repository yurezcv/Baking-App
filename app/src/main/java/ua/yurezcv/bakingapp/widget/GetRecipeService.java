package ua.yurezcv.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import ua.yurezcv.bakingapp.R;
import ua.yurezcv.bakingapp.data.local.LocalRepository;
import ua.yurezcv.bakingapp.data.model.Recipe;

public class GetRecipeService extends IntentService {

    public static final String ACTION_GET_RECIPE = "ua.yurezcv.bakingapp.widget.action.get_recipe";
    public static final String EXTRA_RECIPE_ID = "ua.yurezcv.bakingapp.widget.extra.RECIPE_ID";
    public static final String EXTRA_WIDGET_ID = "ua.yurezcv.bakingapp.widget.extra.WIDGET_ID";

    private static final int INVALID_RECIPE_ID = -1;

    private static final String TAG = "GetRecipeService";

    private int mWidgetId;

    public GetRecipeService() {
        super("GetRecipeService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_RECIPE.equals(action)) {
                mWidgetId = intent.getIntExtra(EXTRA_WIDGET_ID, INVALID_RECIPE_ID);
                final int recipeId = intent.getIntExtra(EXTRA_RECIPE_ID,
                        INVALID_RECIPE_ID);

                getRecipeInfo(recipeId);
            }
        }
    }

    private void getRecipeInfo(int recipeId) {
        LocalRepository localRepository = new LocalRepository(this.getApplicationContext(), null);
        List<Recipe> recipes = localRepository.getRecipesSync();
        for (Recipe recipe : recipes) {
            if (recipe.getId() == recipeId) {
                updateWidget(recipe);
            }
        }
    }

    /**
     * Starts this service to get recipe data with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionGetRecipe(Context context, int recipeId, int widgetId) {
        Intent intent = new Intent(context, GetRecipeService.class);
        intent.setAction(ACTION_GET_RECIPE);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        intent.putExtra(EXTRA_WIDGET_ID, widgetId);
        context.startService(intent);
    }

    private void updateWidget(Recipe recipe) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidget.class));
        // Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_ingredients);

        Log.d(TAG, "updating ingredients for " + recipe.getName());

        // Now update the widget by widget id
        IngredientsWidget.updateIngredientWidget(this.getApplicationContext(), appWidgetManager, mWidgetId, recipe);
    }
}
