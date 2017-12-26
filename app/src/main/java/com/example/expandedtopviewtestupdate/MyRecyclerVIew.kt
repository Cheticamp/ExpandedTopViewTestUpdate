package com.example.expandedtopviewtestupdate

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewConfiguration

/**A RecyclerView that allows temporary pausing of casuing its scroll to affect appBarLayout, based on https://stackoverflow.com/a/45338791/878126 */
class MyRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(context, attrs, defStyle) {
    private var mAppBarTracking: AppBarTracking? = null
    private var mView: View? = null
    private var mTopPos: Int = 0
    private var mLayoutManager: LinearLayoutManager? = null

    interface AppBarTracking {
        fun isAppBarIdle(): Boolean
        fun isAppBarExpanded(): Boolean
    }

    override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?,
                                         type: Int): Boolean {
        if (type == ViewCompat.TYPE_NON_TOUCH && mAppBarTracking!!.isAppBarIdle()
                && isNestedScrollingEnabled) {
            if (dy > 0) {
                if (mAppBarTracking!!.isAppBarExpanded()) {
                    consumed!![1] = dy
                    return true
                }
            } else {
                mTopPos = mLayoutManager!!.findFirstVisibleItemPosition()
                if (mTopPos == 0) {
                    mView = mLayoutManager!!.findViewByPosition(mTopPos)
                    if (-mView!!.top + dy <= 0) {
                        consumed!![1] = dy - mView!!.top
                        return true
                    }
                }
            }
        }
        if (dy < 0 && type == ViewCompat.TYPE_TOUCH && mAppBarTracking!!.isAppBarExpanded()) {
            consumed!![1] = dy
            return true
        }

        val returnValue = super.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type)
        if (offsetInWindow != null && !isNestedScrollingEnabled && offsetInWindow[1] != 0)
            offsetInWindow[1] = 0
        return returnValue
    }

    override fun setLayoutManager(layout: RecyclerView.LayoutManager) {
        super.setLayoutManager(layout)
        mLayoutManager = layoutManager as LinearLayoutManager
    }

    fun setAppBarTracking(appBarTracking: AppBarTracking) {
        mAppBarTracking = appBarTracking
    }

    override fun fling(velocityX: Int, velocityY: Int): Boolean {
        var velocityY = velocityY
        Log.d("Recycler", "<<<<velocity=" + velocityY)
        if (!mAppBarTracking!!.isAppBarIdle()) {
            val vc = ViewConfiguration.get(context)
            velocityY = if (velocityY < 0) -vc.scaledMinimumFlingVelocity
            else vc.scaledMinimumFlingVelocity
        }

        return super.fling(velocityX, velocityY)
    }
}
