package ua.yurezcv.bakingapp.data.remote;

import java.util.List;

import ua.yurezcv.bakingapp.data.DataSourceContract;
import ua.yurezcv.bakingapp.data.model.Recipe;

public final class RemoteRepository implements DataSourceContract {

    private final RemoteHttpClient mRemoteHttpClient;

    public RemoteRepository() {
        mRemoteHttpClient = new RemoteHttpClient();
    }

    @Override
    public void getRecipes(final GetRecipesCallback callback) {
        mRemoteHttpClient.getRecipes(callback);
    }

    @Override
    public void saveRecipesJson(List<Recipe> recipes) {
        // local implementation only
    }
}
