package ua.yurezcv.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import ua.yurezcv.bakingapp.R;
import ua.yurezcv.bakingapp.data.local.LocalRepository;
import ua.yurezcv.bakingapp.data.model.Ingredient;
import ua.yurezcv.bakingapp.data.model.Recipe;

public class IngredientsWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class IngredientsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;
        private int mAppWidgetId;
        private int mRecipeId;

        private ArrayList<Ingredient> mIngredients = new ArrayList<>();

        IngredientsRemoteViewsFactory(Context context, Intent intent) {
            mContext = context.getApplicationContext();
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            mRecipeId = intent.getIntExtra(IngredientsWidget.EXTRA_RECIPE_ID, -1);
        }

        @Override
        public void onCreate() {
            getIngredients();
        }

        @Override
        public void onDataSetChanged() {
            getIngredients();
        }

        private void getIngredients() {
            LocalRepository localRepository = new LocalRepository(mContext, null);
            List<Recipe> recipes = localRepository.getRecipesSync();
            for (Recipe recipe : recipes) {
                if (recipe.getId() == mRecipeId) {
                    mIngredients.clear();
                    mIngredients.addAll(recipe.getIngredients());
                }
            }
        }

        @Override
        public void onDestroy() {
            mIngredients.clear();
        }

        @Override
        public int getCount() {
            return mIngredients.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.list_item_widget_ingredient);
            rv.setTextViewText(R.id.tv_widget_ingredient, mIngredients.get(position).formatForWidget());
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
