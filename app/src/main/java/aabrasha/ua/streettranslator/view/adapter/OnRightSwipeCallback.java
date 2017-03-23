package aabrasha.ua.streettranslator.view.adapter;

import aabrasha.ua.streettranslator.R;
import aabrasha.ua.streettranslator.StreetsApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

/**
 * @author Andrii Abramov on 9/4/16.
 */
public abstract class OnRightSwipeCallback extends ItemTouchHelper.SimpleCallback {

    private static final String TAG = OnRightSwipeCallback.class.getSimpleName();

    private static final int NO_DRAG_DIRECTIONS = 0;
    private static final int SWIPE_DIRECTION_RIGHT = ItemTouchHelper.RIGHT;

    private Context context;
    private Drawable trashIcon;
    private Drawable background;

    public OnRightSwipeCallback() {
        super(NO_DRAG_DIRECTIONS, SWIPE_DIRECTION_RIGHT);
        context = StreetsApplication.getApplication();

        background = getDrawable(R.drawable.item_swipe_to_remove);
        trashIcon = getDrawable(R.drawable.ic_trash);
    }

    private Drawable getDrawable(int id) {
        return ResourcesCompat.getDrawable(context.getResources(), id, context.getTheme());
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int adapterPosition = viewHolder.getAdapterPosition();
        onRightSwipe(adapterPosition);
    }

    public abstract void onRightSwipe(int position);

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX > 0) {
                View itemView = viewHolder.itemView;
                drawDangerBackground(c, (int) dX, itemView);
                drawTrashIcon(c, itemView);
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    private void drawDangerBackground(Canvas c, int dX, View itemView) {
        background.setBounds(itemView.getLeft(), itemView.getTop(), dX, itemView.getBottom());
        background.draw(c);
    }

    private void drawTrashIcon(Canvas c, View itemView) {
        int verticalShift = (itemView.getHeight() - trashIcon.getIntrinsicHeight()) / 2;

        int bottom = itemView.getBottom() - verticalShift;
        int top = itemView.getTop() + verticalShift;
        int left = itemView.getLeft();
        int right = left + trashIcon.getIntrinsicWidth();

        trashIcon.setBounds(left, top, right, bottom);
        trashIcon.draw(c);
    }
}
