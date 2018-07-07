package ua.yurezcv.bakingapp.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ua.yurezcv.bakingapp.data.model.Ingredient;
import ua.yurezcv.bakingapp.data.model.Recipe;
import ua.yurezcv.bakingapp.data.model.RecipeStep;

public final class Utils {

    private Utils() {}

    /* Create a type object for gson */
    private static Type getRecipeType() {
        return new TypeToken<ArrayList<Recipe>>(){}.getType();
    }

    /* Parse JSON with recipes with Google gson library */
    public static List<Recipe> parseRecipesJson(String inputJson) {
        Gson gson = new Gson();
        return gson.fromJson(inputJson, getRecipeType());
    }

    /* Serialize back to json in order to save recipes locally */
    public static String serializeRecipes(List<Recipe> recipes) {
        Gson gson = new Gson();
        return gson.toJson(recipes, getRecipeType());
    }

    public static RecipeStep convetIngredientsToStep(List<Ingredient> ingredients) {
        RecipeStep step = new RecipeStep();
        step.setShortDescription("ingredients");
        step.setDescription(ingredients.toString());
        return step;
    }
}
