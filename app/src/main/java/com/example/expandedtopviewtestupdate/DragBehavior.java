package com.example.expandedtopviewtestupdate;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class DragBehavior extends AppBarLayout.ScrollingViewBehavior {

    private AppBarLayout mAppBar;
    private boolean mAppBarIsExpanded = false;

    public DragBehavior() {
    }

    public DragBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent,
                                   View child,
                                   View dependency) {
        if (dependency instanceof AppBarLayout) {
            mAppBar = (AppBarLayout) dependency;
            return true;
        }
        return false;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent,
                                          View child,
                                          View dependency) {
        super.onDependentViewChanged(parent, child, dependency);
        if (!(dependency instanceof AppBarLayout)) {
            return false;
        }

        AppBarLayout appbar = (AppBarLayout) dependency;
        mAppBarIsExpanded = appbar.getBottom() == appbar.getHeight();

        return false;
    }

    private boolean mInterceptingTouch = false;

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent,
                                         View child, MotionEvent event) {
        if (!mAppBarIsExpanded) {
            return false;
        }

        mInterceptingTouch = parent.isPointInChildBounds(child, (int) event.getX(), (int) event.getY());
        return mInterceptingTouch;
    }

    private int _yStart = 0;
    private int _topStart = 0;

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent event) {
        if (!mInterceptingTouch) {
            return false;
        }
        final int Y = (int) event.getY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                Log.d("DragBehavior", "<<<<ACTION_DOWN");
                _topStart = child.getTop();
                _yStart = Y;
                break;
            case MotionEvent.ACTION_UP:
                Log.d("DragBehavior", "<<<<ACTION_UP");
                mInterceptingTouch = false;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.d("DragBehavior", "<<<<ACTION_POINTER_DOWN");
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.d("DragBehavior", "<<<<ACTION_POINTER_UP");
                mInterceptingTouch = false;
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("DragBehavior", "<<<<ACTION_MOVE");
                child.setY(_topStart - (_yStart - Y));
//                parent.dispatchDependentViewsChanged(child);
                Log.d("DragBehavior", "<<<<start,Y=" + (_yStart + ", " + Y));
                break;
        }
        return true;
    }
}
