package cc.colorcat.mvp.extension.widget;

import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by cxx on 2017/8/13.
 * xx.ch@outlook.com
 */
public abstract class ChoiceRvAdapter extends RvAdapter {
    @IntDef({ChoiceMode.NONE, ChoiceMode.SINGLE, ChoiceMode.MULTIPLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ChoiceMode {
        /**
         * Does not indicate choices
         */
        int NONE = 0;

        /**
         * Allows up to one choice
         */
        int SINGLE = 1;

        /**
         * Allows multiple choices
         */
        int MULTIPLE = 2;
    }

    @ChoiceMode
    private int mChoiceMode = ChoiceMode.NONE;
    private int mSelectedPosition = RecyclerView.NO_POSITION;
    private OnItemSelectedChangeListener mSelectedListener;
    private RecyclerView mRecyclerView;
    private RvSelectHelper mSelectHelper;

    @Override
    public final void onBindViewHolder(@NonNull RvHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (inChoiceMode() && isSelectable(position)) {
            updateItemView(holder, isSelectedWithChoiceMode(position));
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        if (mSelectHelper == null) {
            mSelectHelper = new RvSelectHelper();
        }
        mRecyclerView.addOnItemTouchListener(mSelectHelper);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRecyclerView.removeOnItemTouchListener(mSelectHelper);
        mRecyclerView = null;
    }

    public void setChoiceMode(@ChoiceMode int choiceMode) {
        if (choiceMode == ChoiceMode.NONE
                || choiceMode == ChoiceMode.SINGLE
                || choiceMode == ChoiceMode.MULTIPLE) {
            mChoiceMode = choiceMode;
        } else {
            throw new IllegalArgumentException("Illegal choiceMode, value = " + choiceMode);
        }
    }

    @ChoiceMode
    public int getChoiceMode() {
        return mChoiceMode;
    }

    public void setOnItemSelectedChangeListener(OnItemSelectedChangeListener listener) {
        mSelectedListener = listener;
    }

    public OnItemSelectedChangeListener getOnItemSelectedChangeListener() {
        return mSelectedListener;
    }

    public void setSelection(int position) {
        if (inChoiceMode()
                && checkPosition(position)
                && isSelectable(position)
                && !isSelectedWithChoiceMode(position)) {
            dispatchSelect(position, true);
        }
    }

    public int getSelection() {
        return mSelectedPosition;
    }

    public void resetSelection() {
        mSelectedPosition = RecyclerView.NO_POSITION;
    }

    protected void updateItemView(@NonNull RvHolder holder, boolean selected) {
        holder.itemView.setSelected(selected);
    }

    protected boolean isSelected(int position) {
        return mChoiceMode == ChoiceMode.SINGLE;
    }

    protected void updateItem(int position, boolean selected) {

    }

    protected boolean isSelectable(int position) {
        return position != RecyclerView.NO_POSITION;
    }

    @LayoutRes
    public abstract int getLayoutResId(int viewType);

    public abstract void bindView(@NonNull RvHolder holder, int position);

    private void dispatchSelect(int position, boolean selected) {
        if (mChoiceMode == ChoiceMode.SINGLE) {
            if (selected) {
                final int last = mSelectedPosition;
                mSelectedPosition = position;
                if (checkPosition(last)) {
                    dispatchSelect(last, false);
                }
                notifySelectedChanged(mSelectedPosition, true);
            } else {
                notifySelectedChanged(position, false);
            }
        } else {
            notifySelectedChanged(position, selected);
        }
    }

    private void notifySelectedChanged(int position, boolean selected) {
        updateItem(position, selected);
        RvHolder holder = (RvHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (holder != null) {
            updateItemView(holder, selected);
        } else {
            notifyItemChanged(position);
        }
        if (mSelectedListener != null) {
            mSelectedListener.onItemSelectedChanged(position, selected);
        }
    }

    private boolean inChoiceMode() {
        return mChoiceMode == ChoiceMode.SINGLE || mChoiceMode == ChoiceMode.MULTIPLE;
    }

    private boolean checkPosition(int position) {
        return position >= 0 && position < getItemCount();
    }

    private boolean isSelectedWithChoiceMode(int position) {
        if (mChoiceMode == ChoiceMode.SINGLE) {
            return mSelectedPosition == position && isSelected(position);
        }
        return mChoiceMode == ChoiceMode.MULTIPLE && isSelected(position);
    }


    private class RvSelectHelper extends RecyclerView.SimpleOnItemTouchListener {
        private GestureDetectorCompat mDetector;
        private RecyclerView mRv;

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            mRv = rv;
            dispatchTouchEvent(e);
            return false;
        }

        private void dispatchTouchEvent(MotionEvent e) {
            if (mDetector == null) {
                mDetector = new GestureDetectorCompat(mRv.getContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        RecyclerView.ViewHolder holder = findViewHolder(e);
                        if (holder != null) {
                            onItemClick(holder);
                        }
                        return false;
                    }
                });
            }
            mDetector.onTouchEvent(e);
        }

        private RecyclerView.ViewHolder findViewHolder(MotionEvent e) {
            View child = mRv.findChildViewUnder(e.getX(), e.getY());
            return child != null ? mRv.getChildViewHolder(child) : null;
        }

        private void onItemClick(RecyclerView.ViewHolder holder) {
            if (inChoiceMode()) {
                final int position = holder.getAdapterPosition();
                if (isSelectable(position)) {
                    boolean selected = isSelectedWithChoiceMode(position);
                    if (mChoiceMode == ChoiceMode.MULTIPLE) {
                        dispatchSelect(position, !selected);
                    } else if (!selected) {
                        dispatchSelect(position, true);
                    }
                }
            }
        }
    }


    public interface OnItemSelectedChangeListener {
        void onItemSelectedChanged(int position, boolean selected);
    }
}
