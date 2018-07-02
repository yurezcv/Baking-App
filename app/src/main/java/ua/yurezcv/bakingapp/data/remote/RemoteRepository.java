package ua.yurezcv.bakingapp.data.remote;

import java.util.List;

import ua.yurezcv.bakingapp.data.DataSourceContract;
import ua.yurezcv.bakingapp.data.model.Recipe;
import ua.yurezcv.bakingapp.utils.threading.AppExecutors;

public final class RemoteRepository implements DataSourceContract {

    private final RemoteHttpClient mRemoteHttpClient;

    public RemoteRepository(AppExecutors executors) {
        mRemoteHttpClient = new RemoteHttpClient(executors);
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
