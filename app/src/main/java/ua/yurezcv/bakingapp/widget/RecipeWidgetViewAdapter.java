package ua.yurezcv.bakingapp.widget;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.yurezcv.bakingapp.R;
import ua.yurezcv.bakingapp.data.model.Recipe;

public class RecipeWidgetViewAdapter extends RecyclerView.Adapter<RecipeWidgetViewAdapter.ViewHolder> {

    private int mSelectedPosition = 0;
    private List<Recipe> mData;

    RecipeWidgetViewAdapter() {
        mData = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_widget_recipe, parent, false);
        return new RecipeWidgetViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mRecipe = mData.get(position);
        holder.mRecipeName.setText(mData.get(position).getName());

        holder.mSelectedRadioButton.setChecked(position == mSelectedPosition);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(mSelectedPosition);
                mSelectedPosition = holder.getAdapterPosition();
                notifyItemChanged(mSelectedPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mData == null)
            return 0;
        return mData.size();
    }

    public int getSelectedRecipeId() {
        return mData.get(mSelectedPosition).getId();
    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
    }

    public void setData(List<Recipe> recipes) {
        mData.clear();
        mData.addAll(recipes);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final View mView;

        @BindView(R.id.tv_recipe_name)
        TextView mRecipeName;

        @BindView(R.id.rb_widget_selection)
        RadioButton mSelectedRadioButton;

        Recipe mRecipe;

        ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }
    }
}
