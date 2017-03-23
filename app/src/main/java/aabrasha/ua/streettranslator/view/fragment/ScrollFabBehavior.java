package aabrasha.ua.streettranslator.view.fragment;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Andrii Abramov on 8/28/16.
 */
public class ScrollFabBehavior extends FloatingActionButton.Behavior {

    public ScrollFabBehavior(Context context, AttributeSet attrs) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton fab,
                                       final View directTargetChild, final View target, final int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(final CoordinatorLayout coordinatorLayout, final FloatingActionButton fab,
                               final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        if (dyConsumed != 0 && fab.getVisibility() == View.VISIBLE) {
            fab.hide();
        }
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton fab, View target) {
        super.onStopNestedScroll(coordinatorLayout, fab, target);
        if (!fab.isShown()) {
            fab.show();
        }
    }
}