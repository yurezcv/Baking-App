package ua.yurezcv.bakingapp.ui.steps;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ua.yurezcv.bakingapp.R;
import ua.yurezcv.bakingapp.data.model.RecipeStep;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnStepFragmentInteractionListener}
 * interface.
 */
public class StepsFragment extends Fragment {

    private static final String ARG_RECIPE_STEPS = "recipe-steps";
    private static final String ARG_SELECTED_POSITION = "selected-position";
    private static final String ARG_IS_TABLET = "is-tablet";
    private static final String KEY_RECYCLER_VIEW_STATE = "state-steps-recycler-view";
    private static final String KEY_SELECTED_POSITION = "state-last-selected-position";

    private RecyclerView mStepsRecyclerView;
    private StepsRecyclerViewAdapter mAdapter;

    private OnStepFragmentInteractionListener mListener;

    private ArrayList<RecipeStep> mSteps;
    private int mSelectedPosition;
    private boolean isTablet;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StepsFragment() {
    }

    public static StepsFragment newInstance(ArrayList<RecipeStep> steps, int position, boolean isTablet) {
        StepsFragment fragment = new StepsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_RECIPE_STEPS, steps);
        args.putInt(ARG_SELECTED_POSITION, position);
        args.putBoolean(ARG_IS_TABLET, isTablet);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mSteps = getArguments().getParcelableArrayList(ARG_RECIPE_STEPS);
            mSelectedPosition = getArguments().getInt(ARG_SELECTED_POSITION);
            isTablet = getArguments().getBoolean(ARG_IS_TABLET);
        }

        if(savedInstanceState != null && isTablet) {
            mSelectedPosition = savedInstanceState.getInt(KEY_SELECTED_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mStepsRecyclerView = (RecyclerView) view;
            mStepsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            mAdapter = new StepsRecyclerViewAdapter(mSteps, mSelectedPosition ,mListener);
            mStepsRecyclerView.setAdapter(mAdapter);
        }

        if(savedInstanceState != null) {
            Parcelable recyclerViewState = savedInstanceState.getParcelable(KEY_RECYCLER_VIEW_STATE);
            mStepsRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            mStepsRecyclerView.scrollToPosition(mSelectedPosition);
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepFragmentInteractionListener) {
            mListener = (OnStepFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStepFragmentInteractionListener");
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
        // save recycler view state
        outState.putParcelable(KEY_RECYCLER_VIEW_STATE, mStepsRecyclerView.getLayoutManager().onSaveInstanceState());
        outState.putInt(KEY_SELECTED_POSITION, mAdapter.getSelectedPosition());
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
    public interface OnStepFragmentInteractionListener {
        void onListFragmentInteraction(RecipeStep item);
    }
}
