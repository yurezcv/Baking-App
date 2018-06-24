package ua.yurezcv.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Executors;

import ua.yurezcv.bakingapp.data.DataRepository;
import ua.yurezcv.bakingapp.data.DataSourceContract;
import ua.yurezcv.bakingapp.data.model.Recipe;
import ua.yurezcv.bakingapp.data.remote.RemoteHttpClient;
import ua.yurezcv.bakingapp.utils.threading.AppExecutors;
import ua.yurezcv.bakingapp.utils.threading.DiskIOThreadExecutor;

public class RecipesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        AppExecutors appExecutors = AppExecutors.getInstance(new DiskIOThreadExecutor(),
                Executors.newFixedThreadPool(AppExecutors.THREAD_COUNT),
                new AppExecutors.MainThreadExecutor());

        DataRepository dataRepository = DataRepository.getInstance(getApplicationContext(),
                appExecutors);

        dataRepository.getRecipes(new DataSourceContract.GetRecipesCallback() {
            @Override
            public void onSuccess(List<Recipe> recipes) {
                Log.d("RecipesActivity", recipes.toString());
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("RecipesActivity", throwable.getMessage());
            }
        });
    }

}
