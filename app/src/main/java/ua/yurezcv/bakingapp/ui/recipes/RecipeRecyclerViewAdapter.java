package ua.yurezcv.bakingapp.ui.recipes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.yurezcv.bakingapp.R;
import ua.yurezcv.bakingapp.data.model.Recipe;
import ua.yurezcv.bakingapp.ui.recipes.RecipesGridFragment.OnListFragmentInteractionListener;

public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.ViewHolder> {

    private final List<Recipe> mValues;
    private final OnListFragmentInteractionListener mListener;

    RecipeRecyclerViewAdapter(List<Recipe> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mRecipe = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).getName());

        String servingsText = holder.mServingView.getResources().
                getQuantityString(R.plurals.servings,
                        mValues.get(position).getServings(),
                        mValues.get(position).getServings());

        holder.mServingView.setText(servingsText);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mRecipe);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        @BindView(R.id.tv_list_servings) TextView mServingView;
        @BindView(R.id.tv_list_recipe_title) TextView mContentView;

        public Recipe mRecipe;

        ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public void setData(List<Recipe> recipes) {
        if (mValues != null) {
            mValues.addAll(recipes);
            notifyDataSetChanged();
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }
}
