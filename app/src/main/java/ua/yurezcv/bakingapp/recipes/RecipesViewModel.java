package ua.yurezcv.bakingapp.recipes;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Executors;

import ua.yurezcv.bakingapp.data.DataRepository;
import ua.yurezcv.bakingapp.data.DataSourceContract;
import ua.yurezcv.bakingapp.data.model.Recipe;
import ua.yurezcv.bakingapp.utils.threading.AppExecutors;
import ua.yurezcv.bakingapp.utils.threading.DiskIOThreadExecutor;

public class RecipesViewModel extends AndroidViewModel {

    private static String TAG = "RecipesViewModel";

    private MutableLiveData<List<Recipe>> mRecipes;

    public RecipesViewModel(@NonNull Application application) {
        super(application);

        mRecipes = new MutableLiveData<>();

        AppExecutors appExecutors = AppExecutors.getInstance(new DiskIOThreadExecutor(),
                Executors.newFixedThreadPool(AppExecutors.THREAD_COUNT),
                new AppExecutors.MainThreadExecutor());

        DataRepository dataRepository = DataRepository.getInstance(this.getApplication(),
                appExecutors);

        dataRepository.getRecipes(new DataSourceContract.GetRecipesCallback() {
            @Override
            public void onSuccess(List<Recipe> recipes) {
                Log.d(TAG, recipes.toString());
                mRecipes.setValue(recipes);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e(TAG, throwable.getMessage());
            }
        });
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mRecipes;
    }

}
