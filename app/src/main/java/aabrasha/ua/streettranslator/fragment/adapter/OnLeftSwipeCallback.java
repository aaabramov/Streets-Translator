package aabrasha.ua.streettranslator.fragment.adapter;

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
 * Created by Andrii Abramov on 9/4/16.
 */
public abstract class OnLeftSwipeCallback extends ItemTouchHelper.SimpleCallback {

    private static final String TAG = OnLeftSwipeCallback.class.getSimpleName();

    private static final int NO_DRAG_DIRECTIONS = 0;
    private static final int SWIPE_DIRECTION_LEFT = ItemTouchHelper.LEFT;

    private Context context;
    private Drawable pencilIcon;
    private Drawable background;

    public OnLeftSwipeCallback() {
        super(NO_DRAG_DIRECTIONS, SWIPE_DIRECTION_LEFT);
        context = StreetsApplication.getContext();

        background = getDrawable(R.drawable.item_swipe_to_edit);
        pencilIcon = getDrawable(R.drawable.ic_pencil);
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
        onLeftSwipe(adapterPosition);
    }

    public abstract void onLeftSwipe(int position);

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                View itemView = viewHolder.itemView;
                drawEditBackground(c, (int) dX, itemView);
                drawPencilIcon(c, itemView);
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    private void drawEditBackground(Canvas c, int dX, View itemView) {
        background.setBounds(itemView.getRight() + dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
        background.draw(c);
    }

    private void drawPencilIcon(Canvas c, View itemView) {
        int verticalShift = (itemView.getHeight() - pencilIcon.getIntrinsicHeight()) / 2;

        int bottom = itemView.getBottom() - verticalShift;
        int top = itemView.getTop() + verticalShift;
        int right = itemView.getRight();
        int left = right - pencilIcon.getIntrinsicWidth();

        pencilIcon.setBounds(left, top, right, bottom);
        pencilIcon.draw(c);
    }
}
