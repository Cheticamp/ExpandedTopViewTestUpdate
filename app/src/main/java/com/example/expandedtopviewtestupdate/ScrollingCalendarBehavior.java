package com.example.expandedtopviewtestupdate;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ScrollingCalendarBehavior extends AppBarLayout.Behavior {

    public ScrollingCalendarBehavior() {
        super();
    }

    public ScrollingCalendarBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        return false;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child,
                                  View target, int dx, int dy, int[] consumed, int type) {
        // Check to see if the app bar is expanded or this scroll will result in full expansion
        // and dispose of excess scroll if scrolling up.
        if (dy <= (child.getBottom() - child.getHeight())) {
            consumed[1] += dy;
            child.setExpanded(true, false);
            Log.d("Behavior", "<<<<dy=" + dy + " consumed[1]=" + consumed[1] + " togo=" + (child.getBottom() - child.getHeight()));
        } else {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        }
    }
}
