package ua.yurezcv.bakingapp.ui.steps;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.yurezcv.bakingapp.R;
import ua.yurezcv.bakingapp.data.model.RecipeStep;

public class StepDetailFragment extends Fragment {

    @BindView(R.id.tv_sample)
    TextView mTextView;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StepViewModel stepViewModel = ViewModelProviders.of(getActivity()).get(StepViewModel.class);

        stepViewModel.getSelectedStep().observe(this, new Observer<RecipeStep>() {
            @Override
            public void onChanged(@Nullable RecipeStep recipeStep) {
                updateViews(recipeStep);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    private void updateViews(RecipeStep recipeStep) {
        mTextView.setText(recipeStep.getDescription());
    }

}
