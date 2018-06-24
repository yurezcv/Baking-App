package ua.yurezcv.bakingapp.data;

import java.util.List;

import ua.yurezcv.bakingapp.data.model.Recipe;

public interface DataSourceContract {

    interface GetRecipesCallback {
        void onSuccess(List<Recipe> recipes);
        void onFailure(Throwable throwable);
    }

    void getRecipes(GetRecipesCallback callback);

    void saveRecipesJson(List<Recipe> recipes);

}
