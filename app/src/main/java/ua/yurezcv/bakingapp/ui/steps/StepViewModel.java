package ua.yurezcv.bakingapp.ui.steps;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import ua.yurezcv.bakingapp.data.model.RecipeStep;

public class StepViewModel extends ViewModel {
    private final MutableLiveData<RecipeStep> mSelectedStep = new MutableLiveData<RecipeStep>();

    public void selectStep(RecipeStep item) {
        mSelectedStep.setValue(item);
    }

    public LiveData<RecipeStep> getSelectedStep() {
        return mSelectedStep;
    }
}
