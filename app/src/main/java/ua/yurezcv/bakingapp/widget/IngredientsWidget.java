package ua.yurezcv.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import ua.yurezcv.bakingapp.R;
import ua.yurezcv.bakingapp.data.model.Recipe;

public class IngredientsWidget extends AppWidgetProvider {

    public static final String EXTRA_RECIPE_ID = "ua.yurezcv.bakingapp.widget.extra.RECIPE_ID";

    static void startWidgetUpdateService(Context context, int appWidgetId) {
        final int recipeId = IngredientsWidgetConfigureActivity.loadRecipeId(context, appWidgetId);
        GetRecipeService.startActionGetRecipe(context.getApplicationContext(), recipeId, appWidgetId);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // run a service to update all of widgets and their UI
        for (int appWidgetId : appWidgetIds) {
            startWidgetUpdateService(context.getApplicationContext(), appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            IngredientsWidgetConfigureActivity.deleteRecipeId(context.getApplicationContext(), appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void updateIngredientWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Recipe recipe) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        views.setTextViewText(R.id.tv_widget_title, recipe.getName());

        Intent intent = new Intent(context, IngredientsWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.putExtra(EXTRA_RECIPE_ID, recipe.getId());

        views.setRemoteAdapter(R.id.lv_ingredients, intent);

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_ingredients);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

}

