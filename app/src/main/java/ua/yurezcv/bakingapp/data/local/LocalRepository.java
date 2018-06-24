package ua.yurezcv.bakingapp.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.List;

import ua.yurezcv.bakingapp.data.DataSourceContract;
import ua.yurezcv.bakingapp.data.model.Recipe;
import ua.yurezcv.bakingapp.utils.Utils;
import ua.yurezcv.bakingapp.utils.threading.AppExecutors;

/* Usage of default SharedPreference's class as a simplest way to store the data locally */
public final class LocalRepository implements DataSourceContract {

    private final static String PREF_KEY_RECIPES = "key_recipes_json";

    private final SharedPreferences mSharedPreferences;
    private final AppExecutors mExecutors;

    public LocalRepository(Context context, AppExecutors executors) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mExecutors = executors;
    }

    @Override
    public void getRecipes(final GetRecipesCallback callback) {
        final String json = mSharedPreferences.getString(PREF_KEY_RECIPES, "");

        // parse json in a background thread
        mExecutors.mainThread().execute(new Runnable() {

            @Override
            public void run() {
                callback.onSuccess(Utils.parseRecipesJson(json));
            }
        });
    }

    @Override
    public void saveRecipesJson(List<Recipe> recipes) {
        mSharedPreferences
                .edit()
                .putString(PREF_KEY_RECIPES, Utils.serializeRecipes(recipes))
                .apply();
    }

    public boolean hasSavedData() {
        return mSharedPreferences.contains(PREF_KEY_RECIPES);
    }
}
