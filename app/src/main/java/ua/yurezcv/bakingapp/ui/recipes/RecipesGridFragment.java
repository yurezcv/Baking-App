package ua.yurezcv.bakingapp.ui.recipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.yurezcv.bakingapp.R;
import ua.yurezcv.bakingapp.data.model.Recipe;
import ua.yurezcv.bakingapp.idlingresource.SimpleIdlingResource;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RecipesGridFragment extends Fragment {

    private static final String KEY_RECYCLER_VIEW_STATE = "recipes-recycler-view";

    // UI elements
    @BindView(R.id.rv_recipes)
    RecyclerView mRecipesGridRecycleView;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBar;

    @BindView(R.id.tv_error_message)
    TextView mErrorTextView;

    private OnListFragmentInteractionListener mListener;

    private RecipeRecyclerViewAdapter mRecipesAdapter;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipesGridFragment() {
    }

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecipesAdapter = new RecipeRecyclerViewAdapter(new ArrayList<Recipe>(), mListener);

        // Get the IdlingResource instance
        getIdlingResource();

        setupViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        ButterKnife.bind(this, view);

        // init the recycler view
        mRecipesGridRecycleView.setLayoutManager(
                new GridLayoutManager(view.getContext(), calcNumberOfColumns()));
        mRecipesGridRecycleView.setAdapter(mRecipesAdapter);

        // restore RecyclerView state if the bundle exists
        if(savedInstanceState != null) {
            Parcelable recyclerViewState = savedInstanceState.getParcelable(KEY_RECYCLER_VIEW_STATE);
            mRecipesGridRecycleView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStepsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // save RecyclerView state
        outState.putParcelable(KEY_RECYCLER_VIEW_STATE, mRecipesGridRecycleView.getLayoutManager().onSaveInstanceState());
    }

    private void setupViewModel() {
        mIdlingResource.setIdleState(false);

        RecipesViewModel viewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
        viewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipesEntries) {

                mRecipesAdapter.setData(recipesEntries);
                mIdlingResource.setIdleState(true);

                if (!mRecipesAdapter.isEmpty()) {
                    hideProgressBar();
                }
            }
        });
    }

    private int calcNumberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 600;
        int width = displayMetrics.widthPixels;
        return width / widthDivider;
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
        mRecipesGridRecycleView.setVisibility(View.VISIBLE);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Recipe item);
    }
}
