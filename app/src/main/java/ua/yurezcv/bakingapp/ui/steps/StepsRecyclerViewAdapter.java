package ua.yurezcv.bakingapp.ui.steps;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ua.yurezcv.bakingapp.R;
import ua.yurezcv.bakingapp.data.model.RecipeStep;


public class StepsRecyclerViewAdapter extends RecyclerView.Adapter<StepsRecyclerViewAdapter.ViewHolder> {

    private final List<RecipeStep> mValues;
    private final StepsFragment.OnStepFragmentInteractionListener mListener;

    private int mSelectedPos;

    StepsRecyclerViewAdapter(List<RecipeStep> items, int selected, StepsFragment.OnStepFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mSelectedPos = selected;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_step, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        int selectedColor = holder.itemView.getResources().getColor(R.color.colorPrimaryDarkTransparent);
        holder.itemView.setBackgroundColor(mSelectedPos == position ? selectedColor : Color.TRANSPARENT);

        String stepTitle = mValues.get(position).getShortDescription();

        // get localized resource for hardcoded step label
        if(stepTitle.equals("Ingredients")) {
            holder.mStepTitle.setText(
                    holder.mStepTitle.getResources().getString(R.string.ingredients));
            holder.mStepNumber.setVisibility(View.GONE);
        } else {
            holder.mStepTitle.setText(stepTitle);
            if(position >= 2) {
                holder.mStepNumber.setVisibility(View.VISIBLE);
                holder.mStepNumber.setText(
                        holder.mStepNumber.getResources().getString(R.string.step, position - 1));
            } else {
                holder.mStepNumber.setVisibility(View.GONE);
            }
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);

                    notifyItemChanged(mSelectedPos);
                    mSelectedPos = holder.getAdapterPosition();
                    notifyItemChanged(mSelectedPos);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public int getSelectedPosition() {
        return mSelectedPos;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;

        @BindView(R.id.tv_step_title) TextView mStepTitle;
        @BindView(R.id.tv_step_number) TextView mStepNumber;

        public RecipeStep mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mStepTitle.getText() + "'";
        }
    }
}
