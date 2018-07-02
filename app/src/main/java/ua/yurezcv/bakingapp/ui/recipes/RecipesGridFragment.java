package ua.yurezcv.bakingapp.ui.recipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import ua.yurezcv.bakingapp.R;
import ua.yurezcv.bakingapp.data.model.Recipe;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RecipesGridFragment extends Fragment {

    private static final String TAG = "RecipesGridFragment";

    // UI elements
    private RecyclerView mRecipesGridRecycleView;
    private ProgressBar mProgressBar;
    private TextView mErrorTextView;

    private OnListFragmentInteractionListener mListener;

    private RecipeRecyclerViewAdapter mRecipesAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipesGridFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecipesAdapter = new RecipeRecyclerViewAdapter(new ArrayList<Recipe>(), mListener);

        setupViewModel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        mRecipesGridRecycleView = view.findViewById(R.id.rv_recipes);
        mProgressBar = view.findViewById(R.id.pb_loading_indicator);
        mErrorTextView = view.findViewById(R.id.tv_error_message);

        // init the recycler view
        mRecipesGridRecycleView.setLayoutManager(
                new GridLayoutManager(view.getContext(), calcNumberOfColumns()));
        mRecipesGridRecycleView.setAdapter(mRecipesAdapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void setupViewModel() {
        RecipesViewModel viewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
        viewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipesEntries) {

                mRecipesAdapter.setData(recipesEntries);

                if (!mRecipesAdapter.isEmpty()) {
                    hideProgressBar();
                }
            }
        });
    }

    private int calcNumberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 720;
        int width = displayMetrics.widthPixels;
        /*int nColumns = width / widthDivider;
        if (nColumns < 2) return 2; // to keep the grid aspect
        return nColumns;*/
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
