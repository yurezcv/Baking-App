package ua.yurezcv.bakingapp.data;

import android.content.Context;

import java.util.List;

import ua.yurezcv.bakingapp.data.local.LocalRepository;
import ua.yurezcv.bakingapp.data.model.Recipe;
import ua.yurezcv.bakingapp.data.remote.RemoteRepository;
import ua.yurezcv.bakingapp.utils.threading.AppExecutors;

public class DataRepository implements DataSourceContract {

    private static volatile DataRepository sInstance;

    private final LocalRepository mLocalRepository;
    private final RemoteRepository mRemoteRepository;

    private DataRepository(Context context, AppExecutors appExecutors) {

        // Prevent form the reflection api.
        if (sInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single sInstance of this class.");
        }

        mLocalRepository = new LocalRepository(context, appExecutors);
        mRemoteRepository = new RemoteRepository(appExecutors);
    }

    public static DataRepository getInstance(Context context, AppExecutors appExecutors) {
        // making sInstance thread safe
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) sInstance = new DataRepository(context, appExecutors);
            }
        }

        return sInstance;
    }

    @Override
    public void getRecipes(final GetRecipesCallback callback) {
        if (mLocalRepository.hasSavedData()) {
            mLocalRepository.getRecipes(callback);
        } else {
            mRemoteRepository.getRecipes(new GetRecipesCallback() {

                @Override
                public void onSuccess(List<Recipe> recipes) {
                    saveRecipesJson(recipes);
                    callback.onSuccess(recipes);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }
            });
        }
    }

    @Override
    public void saveRecipesJson(List<Recipe> recipes) {
        mLocalRepository.saveRecipesJson(recipes);
    }

}
