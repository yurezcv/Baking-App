package ua.yurezcv.bakingapp.data.remote;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ua.yurezcv.bakingapp.data.DataSourceContract;
import ua.yurezcv.bakingapp.data.model.Recipe;
import ua.yurezcv.bakingapp.utils.Utils;
import ua.yurezcv.bakingapp.utils.threading.AppExecutors;

public final class RemoteHttpClient {

    private final OkHttpClient mClient;
    private final AppExecutors mExecutors;

    RemoteHttpClient(AppExecutors executors) {
        mClient = new OkHttpClient();
        mExecutors = executors;
    }

    public void getRecipes(final DataSourceContract.GetRecipesCallback callback) {
        Request request = new Request.Builder()
                .url("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json")
                .build();

        mClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getCause());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);

                    final List<Recipe> recipes = Utils.parseRecipesJson(responseBody.string());

                    // return callback to the main thread
                    mExecutors.mainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(recipes);
                        }
                    });
                }
            }
        });
    }
}
